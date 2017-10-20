import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.Logger
import oauth2.{KeycloakTokenVerifier, OAuth2Authorization}
import org.keycloak.adapters.KeycloakDeploymentBuilder
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}

object Api extends App {
  val logger = Logger(LoggerFactory.getLogger("api"))

  implicit val config: Config = ConfigFactory.load
  implicit val system: ActorSystem = ActorSystem("api", config)
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  implicit val timeout: Timeout = Timeout(30 seconds)
  val port = 7005

  val oAuth2Authorization = OAuth2Authorization(
    logger, new KeycloakTokenVerifier(
      KeycloakDeploymentBuilder.build(
        getClass.getResourceAsStream("/keycloak.json")
      )
    )
  )

  val router = new Router(oAuth2Authorization)
  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(router.route, "127.0.0.1", port)
}
