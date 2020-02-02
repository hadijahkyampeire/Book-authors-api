package services
 
import com.google.inject.Inject
import models.{Book, BookList}
 
import scala.concurrent.Future
 
class BookService @Inject() (items: BookList) {
 
  def addItem(item: Book): Future[String] = {
    items.add(item)
  }
 
  def deleteItem(id: Long): Future[Int] = {
    items.delete(id)
  }
 
  def updateItem(item: Book): Future[Int] = {
    items.update(item)
  }
 
  def getItem(id: Long): Future[Option[Book]] = {
    items.get(id)
  }
 
  def listAllItems: Future[Seq[Book]] = {
    items.listAll
  }
}
