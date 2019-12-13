package com.david.mocassin.model.c_components

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

/**
 * Represent a type in C programming.
 *
 * The type could be a simple type (int, float and so on) or a user structure from the project (enum, union, struct)
 *
 * if the type is a pointer a "*" will be added in the variable
 *
 * @constructor
 *
 *
 * @param type could be a CtypeEnum, CuserStructure, Cvariable
 * @param isPointer 'true' if the the type is a pointer
 */
open class Ctype(type: CuserType, isPointer: Boolean) {
    val typeProperty = SimpleObjectProperty<CuserType>(type)
    var type: CuserType by typeProperty

    val isPointerProperty = SimpleBooleanProperty(isPointer)
    var isPointer by isPointerProperty
}


