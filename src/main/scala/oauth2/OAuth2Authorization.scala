package oauth2

import akka.http.scaladsl.model.headers.{Authorization, OAuth2BearerToken}
import akka.http.scaladsl.server.Directives.{reject, _}
import akka.http.scaladsl.server._
import com.typesafe.scalalogging.Logger

import scala.collection.JavaConverters._

class OAuth2Authorization(logger: Logger, tokenVerifier: TokenVerifier) {

  def authorizeTokenWithRole(role: String): Directive1[VerifiedToken] = {
    authorizeToken flatMap {
      case t if t.roles.contains(role) => provide(t)
      case _ => reject(AuthorizationFailedRejection).toDirective[Tuple1[VerifiedToken]]
    }
  }

  def authorizeToken: Directive1[VerifiedToken] = {
    bearerToken.flatMap {
      case Some(token) =>
        onComplete(tokenVerifier.verifyToken(token)).flatMap {
          _.map { t =>
            provide(VerifiedToken(token, t.getId, t.getName, t.getPreferredUsername, t.getEmail,
              t.getRealmAccess.getRoles.asScala.toSeq))
          }.recover {
            case ex: Throwable =>
              logger.error("Authorization Token could not be verified", ex)
              reject(AuthorizationFailedRejection).toDirective[Tuple1[VerifiedToken]]
          }.get
        }
      case None =>
        reject(AuthorizationFailedRejection)
    }
  }

  private def bearerToken: Directive1[Option[String]] =
    for {
      authBearerHeader <- optionalHeaderValueByType(classOf[Authorization]).map(extractBearerToken)
      xAuthCookie <- optionalCookie("X-Authorization-Token").map(_.map(_.value))
    } yield authBearerHeader.orElse(xAuthCookie)

  private def extractBearerToken(authHeader: Option[Authorization]): Option[String] =
    authHeader.collect {
      case Authorization(OAuth2BearerToken(token)) => token
    }

}

object OAuth2Authorization {
  def apply(l: Logger, tv: TokenVerifier): OAuth2Authorization = new OAuth2Authorization(l, tv)
}

