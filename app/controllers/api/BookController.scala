package controllers.api
 
import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.{Book, BookForm}
import play.api.data.FormError

import services.BookService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
 import java.lang.ProcessBuilder.Redirect
 
class BookController @Inject()(
    cc: ControllerComponents,
    bookService: BookService
) extends AbstractController(cc) {
 
    implicit val bookFormat = Json.format[Book]
 
    def getAll = Action.async { implicit request: Request[AnyContent] =>
        bookService.listAllItems map { item => 
        Ok(Json.toJson(item))}
    }

    def getById(id: Long) = Action.async { implicit request: Request[AnyContent] =>
        bookService.getItem(id) map { item =>
          Ok(Json.toJson(item))
        }
      }

    def add() = Action.async { implicit request: Request[AnyContent] => 
        BookForm.form.bindFromRequest.fold(
            errorForm => {
                errorForm.errors.foreach(println)
                Future.successful(BadRequest("Error!"))
          },
          data => {
            val newBookItem = Book(0, data.title, data.genre, data.author)
            bookService.addItem(newBookItem).map( _ => Redirect(routes.BookController.getAll))
          }
        )
    }

    def update(id: Long) = Action.async { implicit request: Request[AnyContent] =>
        BookForm.form.bindFromRequest.fold(
            errorForm => {
                errorForm.errors.foreach(println)
                Future.successful(BadRequest("Error!"))
            },
            data => {
                val bookItem = Book(id, data.title, data.genre, data.author)
                bookService.updateItem(bookItem).map(_ => Redirect(routes.BookController.getAll))
            }
        )
    }

    def delete(id: Long) = Action.async { implicit request: Request[AnyContent] =>
        bookService.deleteItem(id) map { res => 
            Redirect(routes.BookController.getAll)}
    }
}
