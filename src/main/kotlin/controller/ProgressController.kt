package controller

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.control.TextArea
import kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import java.net.URL
import java.util.*
import kotlin.properties.Delegates

class ProgressController: Initializable {

    @FXML
    private lateinit var textArea: TextArea

    @FXML
    private lateinit var nameLabel: Label

    @FXML
    private lateinit var currentNumberLabel: Label

    @FXML
    private lateinit var countLabel: Label

    @FXML
    private lateinit var progressBar: ProgressBar

    var countProgress: Long by Delegates.observable(0L) { _, _, newValue ->
        Platform.runLater {
            countLabel.text = newValue.toString()
        }
        currentNumberProgress = 0
    }

    var currentNumberProgress: Long by Delegates.observable(0L) { _, _, newValue ->
        val progress = newValue / countProgress.toDouble()
        Platform.runLater {
            progressBar.progress = progress
            currentNumberLabel.text = newValue.toString()
        }
    }

    fun addProgress() {
        currentNumberProgress += 1
    }

    fun setName(name: String) {
        Platform.runLater {
            nameLabel.text = name
        }
    }

    fun appendTextArea(str: String, newLine: Boolean = true) {
        Platform.runLater {
            textArea.appendText(str)
            if (newLine) {
                textArea.appendText("\n")
            }
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        kodein.addConfig {
            bind<ProgressController>() with singleton { this@ProgressController }
        }
    }
}