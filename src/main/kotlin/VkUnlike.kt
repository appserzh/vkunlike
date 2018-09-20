
import com.vk.api.sdk.client.actors.UserActor
import controller.LoginController
import controller.PermissionChooseController
import controller.ProgressController
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import model.Model
import mu.KotlinLogging
import org.kodein.di.conf.ConfigurableKodein
import org.kodein.di.generic.*
import service.VkApi

private val logger = KotlinLogging.logger {}
var kodein = ConfigurableKodein(mutable = true)

class VkUnlike: Application() {

    val permissionChooseScene: Scene by kodein.instance(arg = M(PermissionChooseController::class.java, "/PermissionChoose.fxml"))
    val loginScene: Scene by kodein.instance(arg = M(LoginController::class.java, "/Login.fxml"))
    val progressScene: Scene by kodein.instance(arg = M(ProgressController::class.java, "/Progress.fxml"))
    val vkApi: VkApi by kodein.instance()
    lateinit var stage: Stage

    fun toLogin() {
        stage.scene = loginScene
    }

    fun toProgress() {
        stage.scene = progressScene
        vkApi.run()
    }

    override fun start(primaryStage: Stage) {
        kodein.addConfig { bind<VkUnlike>() with singleton { this@VkUnlike } }
        stage = primaryStage
        stage.scene = permissionChooseScene
        stage.show()
    }
}

fun main(vararg args : String) {
    kodein.addConfig {
        bind<Model>() with singleton { Model() }
        constant("APP_ID") with "6648187"
        bind<Scene>() with  multiton { clazz: Class<*>, path: String ->
            val loader = FXMLLoader(clazz.getResource(path))
            val root = loader.load<Parent>()
            Scene(root)
        }
        bind<VkApi>() with singleton { VkApi() }
        //val vk = VkApiClient(HttpTransportClient())
        //vk.wall().get(UserActor(0, "")).
        bind<UserActor>() with multiton { userId:Int, accessToken: String ->
            UserActor(userId, accessToken)
        }
    }
    Application.launch(VkUnlike::class.java)
}
