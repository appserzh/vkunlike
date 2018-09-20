package controller

import VkUnlike
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.CheckBox
import kodein
import model.Model
import mu.KotlinLogging
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import java.net.URL
import java.util.*

private val logger = KotlinLogging.logger {}
class PermissionChooseController : Initializable {

    private val model: Model by kodein.instance()
    private val vkUnlike: VkUnlike by kodein.instance()

    @FXML
    private lateinit var photos: CheckBox

    @FXML
    private lateinit var video: CheckBox

    @FXML
    private lateinit var posts: CheckBox

    @FXML
    private lateinit var wall: CheckBox

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        kodein.addConfig {
            bind<PermissionChooseController>() with instance(this@PermissionChooseController)
        }
        photos.selectedProperty().bindBidirectional(model.photosProperty)
        video.selectedProperty().bindBidirectional(model.videoProperty)
        posts.selectedProperty().bindBidirectional(model.friendsProperty)
        wall.selectedProperty().bindBidirectional(model.wallProperty)
    }

    fun handleOkButton(event: ActionEvent) {
        vkUnlike.toLogin()
    }
}