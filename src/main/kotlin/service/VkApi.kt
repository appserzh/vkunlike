package service

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.UserActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import com.vk.api.sdk.queries.likes.LikesType
import controller.ProgressController
import kodein
import model.Model
import mu.KotlinLogging
import org.kodein.di.generic.instance
import kotlin.concurrent.thread
val logger = KotlinLogging.logger {  }
class VkApi {

    val model: Model by kodein.instance()

    val progressController: ProgressController by kodein.instance()

    val vkApiClient: VkApiClient = VkApiClient(HttpTransportClient())

    private fun userActor(): UserActor {
        return UserActor(model.userId, model.accessToken)
    }

    fun run() {
        thread {
            if (model.photosProperty.value) {
                unlikePhotos()
            }
        }

    }

    fun unlikePhotos() {
        progressController.setName("Photos")
        progressController.appendTextArea("---PHOTOS-LIKE---")
        val resp = vkApiClient.fave().getPhotos(userActor()).offset(0).count(1).execute()
        val count = resp.count
        progressController.countProgress = count.toLong()
        logger.info { "count=$count" }

        for (i in 0..count step 50) {
            val resp = vkApiClient.fave().getPhotos(userActor()).offset(i).count(50).execute()

            resp.items.forEach {
                progressController.addProgress()
                progressController.appendTextArea(it.photo2560 ?: it.photo1280 ?: it.photo807 ?: it.photo604 ?: it.photo130 ?: it.photo75 ?: "unknown")
                try {
                    vkApiClient.likes().delete(userActor(), LikesType.PHOTO, it.id).ownerId(it.ownerId).execute()
                } catch (e: Exception) {
                    progressController.appendTextArea("no")
                    logger.debug { e }
                }
                Thread.sleep(600)
            }
        }
    }
}
