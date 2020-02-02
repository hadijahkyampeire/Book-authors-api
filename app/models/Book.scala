package models 
import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.MySQLProfile.api._

case class Book(id: Long, title: String, genre:String, author: String)
case class BookFormData(title: String, genre:String, author: String)

object BookForm {
  val form = Form(
    mapping(
      "title" -> nonEmptyText,
      "genre" -> nonEmptyText,
      "author" -> nonEmptyText
    )(BookFormData.apply)(BookFormData.unapply)
  )
}
class BookTableDef(tag: Tag) extends Table[Book](tag, "book") {
 
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title")
  def genre = column[String]("genre")
  def author = column[String]("author")
 
  override def * = (id, title, genre, author) <> (Book.tupled, Book.unapply)
}
 
 
class BookList @Inject()(
    protected val dbConfigProvider: DatabaseConfigProvider
)(implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile] {
 
  var bookList = TableQuery[BookTableDef]
 
  def add(bookItem: Book): Future[String] = {
    dbConfig.db
      .run(bookList += bookItem)
      .map(res => "Book successfully added")
      .recover {
        case ex: Exception => {
            printf(ex.getMessage())
            ex.getMessage
        }
      }
  }
 
  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(bookList.filter(_.id === id).delete)
  }
 
  def update(bookItem: Book): Future[Int] = {
    dbConfig.db
      .run(bookList.filter(_.id === bookItem.id)
            .map(x => (x.title, x.genre, x.author))
            .update(bookItem.title, bookItem.genre, bookItem.author)
      )
  }
 
  def get(id: Long): Future[Option[Book]] = {
    dbConfig.db.run(bookList.filter(_.id === id).result.headOption)
  }
 
  def listAll: Future[Seq[Book]] = {
    dbConfig.db.run(bookList.result)
  }
}
