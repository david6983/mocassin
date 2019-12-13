package com.david.mocassin.model.c_components.c_variable

import tornadofx.ItemViewModel

class CvariableModel: ItemViewModel<Cvariable>() {
    val name = bind(Cvariable::nameProperty)
    val type = bind(Cvariable::typeProperty)
    val isPointer = bind(Cvariable::isPointerProperty)
    val isComparable = bind(Cvariable::isComparableProperty)
}