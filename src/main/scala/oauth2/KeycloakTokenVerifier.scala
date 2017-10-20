package oauth2

import java.security.spec.X509EncodedKeySpec
import java.security.{KeyFactory, PublicKey}
import java.util.Base64

import org.keycloak.RSATokenVerifier
import org.keycloak.adapters.KeycloakDeployment
import org.keycloak.representations.AccessToken

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class KeycloakTokenVerifier(keycloakDeployment: KeycloakDeployment) extends TokenVerifier {
  def verifyToken(token: String): Future[AccessToken] = {
    Future {
      RSATokenVerifier.verifyToken(
        token,
        decodePublicKey(pemToDer("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApe2Lt1xYiVx2jpuV+7/bkH+329zIjVkCHpyt57UZzQe/ayd+FZhUBoZ0rxqwC6A7nL0J6kk7mY46+XVFdZzvKeeg+tBPay/8qAOHNVffy4SlXGX9Chr7lKJsVTeJ/7gKKsnoGnMx2QmNc/uRPgi3Eug8NWfoZAk1QIcWXMYUsEQXTQNIS884XgmLx8dut35MH3pxc3Jr1zjJUGQt91GdDqrp4rLSuW6fg8VOrqwbcNVnVwATxJJphAr/OVgd7KfGnpQM7KTBx4+jJokDG13kamLCrqgI6OMOnfcZE887fnk4TZbacYTEcjylOWf7kbSxndF2bvFqeca04KAv6qF+1QIDAQAB")),
        keycloakDeployment.getRealmInfoUrl
      )
    }
  }

  def pemToDer(pem: String): Array[Byte] = Base64.getDecoder.decode(stripBeginEnd(pem))

  def stripBeginEnd(pem: String): String = {
    var stripped = pem.replaceAll("-----BEGIN (.*)-----", "")
    stripped = stripped.replaceAll("-----END (.*)----", "")
    stripped = stripped.replaceAll("\r\n", "")
    stripped = stripped.replaceAll("\n", "")
    stripped.trim
  }

  def decodePublicKey(der: Array[Byte]): PublicKey = {
    val spec = new X509EncodedKeySpec(der)
    val kf = KeyFactory.getInstance("RSA")

    kf.generatePublic(spec)
  }
}