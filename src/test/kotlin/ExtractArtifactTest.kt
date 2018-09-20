
import controller.LoginController
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.kodein.di.generic.with

val accessToken = "5d0b0000000000fd792b83e3b67103126a799520613996b4a2fb28a55a15bbe6865a7c8980970"
val userId = 11111
val uri = " https://oauth.vk.com/blank.html#access_token=$accessToken&expires_in=0&user_id=$userId&state=12dsf"

class ExtractArtifactTest : StringSpec() {
    override fun beforeSpec(description: Description, spec: Spec) {
        super.beforeSpec(description, spec)
        kodein.addConfig {
            constant("APP_ID") with "000000"
        }
    }
    init {
        "LoginController must extract access_token" {
            val loginController = LoginController()
            val extracted = loginController.accessToken(uri)
            extracted.get() shouldBe accessToken
        }
        "LoginController must extract user_id" {
            val loginController = LoginController()
            val extracted = loginController.userId(uri)
            extracted.get() shouldBe userId
        }
    }
}