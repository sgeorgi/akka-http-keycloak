import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import oauth2.VerifiedToken
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait ApiFormats extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val verifiedTokenFormat: RootJsonFormat[VerifiedToken] = jsonFormat6(VerifiedToken)
}
