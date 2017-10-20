import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import oauth2.OAuth2Authorization

class Router(oAuth2Authorization: OAuth2Authorization) extends ApiFormats {

  def route: Route = {
    path("") {
      oAuth2Authorization.authorizeToken { token =>
        complete(StatusCodes.OK, token)
      }
    } ~ path("adminRole") {
      oAuth2Authorization.authorizeTokenWithRole("admin") { token =>
        complete(StatusCodes.OK, token)
      }
    } ~ path("userRole") {
      oAuth2Authorization.authorizeTokenWithRole("user") { token =>
        complete(StatusCodes.OK, token)
      }
    }
  }
}
