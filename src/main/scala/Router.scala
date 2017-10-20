import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import oauth2.OAuth2Authorization

class Router(oAuth2Authorization: OAuth2Authorization) extends ApiFormats {

  def route: Route = {
    path("authorizedOnly") {
      oAuth2Authorization.authorizeToken { token =>
        complete(StatusCodes.OK, token)
      }
    } ~ path("forAdminRoleOnly") {
      oAuth2Authorization.authorizeTokenWithRole("admin") { token =>
        complete(StatusCodes.OK, token)
      }
    } ~ path("forUserRoleOnly") {
      oAuth2Authorization.authorizeTokenWithRole("user") { token =>
        complete(StatusCodes.OK, token)
      }
    }
  }
}
