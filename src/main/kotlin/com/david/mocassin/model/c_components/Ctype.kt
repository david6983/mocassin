package com.david.mocassin.model.c_components

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

open class Ctype(type: CuserType, isPointer: Boolean) {
    val typeProperty = SimpleObjectProperty<CuserType>(type)
    var type: CuserType by typeProperty

    val isPointerProperty = SimpleBooleanProperty(isPointer)
    var isPointer by isPointerProperty
}


