package actions

import play.api.mvc._
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import play.api.i18n.{MessagesApi, Messages, MessagesProvider}

class UserRequest[A](
    val username: Option[String],
    request: Request[A],
    val messagesApi: MessagesApi
) extends WrappedRequest[A](request) with MessagesProvider {
  override def messages: Messages = messagesApi.preferred(request)
}

class UserAction @Inject() (val parser: BodyParsers.Default, val messagesApi: MessagesApi)
                          (implicit val executionContext: ExecutionContext)
  extends ActionBuilder[UserRequest, AnyContent]
    with ActionTransformer[Request, UserRequest] {

  override def transform[A](request: Request[A]): Future[UserRequest[A]] = Future.successful {
    new UserRequest(request.session.get("username"), request, messagesApi)
  }
}