package model

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty

class Model {

    val photosProperty: BooleanProperty = SimpleBooleanProperty(true)
    val videoProperty: BooleanProperty = SimpleBooleanProperty(true)
    val friendsProperty: BooleanProperty = SimpleBooleanProperty(true)
    val wallProperty: BooleanProperty = SimpleBooleanProperty(true)
    var userId: Int = 0
    lateinit var accessToken: String
}