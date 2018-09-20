package controller

import VkUnlike
import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.web.WebView
import kodein
import model.Model
import mu.KotlinLogging
import org.kodein.di.generic.instance
import java.net.CookieHandler
import java.net.CookieManager
import java.net.URL
import java.util.*


private val logger = KotlinLogging.logger {}

class LoginController : Initializable {

    val model: Model by kodein.instance()

    val vkUnlike: VkUnlike by kodein.instance()

    val apId: String by kodein.instance("APP_ID")

    @FXML
    lateinit var webView: WebView

    val triggerUrl = "https://oauth.vk.com/blank.html"

    val baseUrl = "https://oauth.vk.com/authorize?redirect_uri=$triggerUrl&revoke=1&client_id=$apId&display=page&state=12dsf&v=5.69&response_type=token&scope="


    fun accessToken(uri: String): Optional<String> {
        val regEx = "#access_token=(.*?)&".toRegex()
        val extracted = regEx.find(uri)
        return Optional.ofNullable(extracted?.groupValues?.get(1))
    }

    fun userId(uri: String): Optional<Int> {
        val regex = "user_id=([0-9]*?)(&|$)".toRegex()
        val extracted = regex.find(uri)
        return Optional.ofNullable(extracted?.groupValues?.get(1)?.toInt())
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        val cookieManager = CookieManager()
        CookieHandler.setDefault(cookieManager)
        val webEngine = webView.engine
        //permissionList.add("offline")
        var permissonCode = 0
        if (model.friendsProperty.value) {
            permissonCode += 2
        }
        if (model.photosProperty.value) {
            permissonCode += 4
        }
        if (model.videoProperty.value) {
            permissonCode += 16
        }
        if (model.wallProperty.value) {
            permissonCode += 8192
        }
        val locUri = baseUrl.plus(permissonCode)
        logger.info { locUri }
        webEngine.load(locUri)
        webEngine.locationProperty()
                .addListener { obs: ObservableValue<out String>, oldValue: String, newValue: String ->
                    logger.info { cookieManager.cookieStore.cookies }
                    logger.info { newValue }
                    if (newValue.startsWith(triggerUrl)) {
                        model.accessToken = accessToken(newValue).orElseThrow { RuntimeException() }
                        model.userId = userId(newValue).orElseThrow { RuntimeException() }
                        vkUnlike.toProgress()
                    }
                }
    }
}