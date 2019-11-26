package com.david.mocassin.model.c_components

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

//TODO separate classes

class Cvariable(name: String,
                type: CuserType,
                isPointer: Boolean = false,
                isComparable: Boolean = false) : Ctype(type, isPointer), JsonModel {

    val nameProperty = SimpleStringProperty(name)
    var name: String by nameProperty

    val isComparableProperty = SimpleBooleanProperty(isComparable)
    var isComparable by isComparableProperty

    fun toJson(): String {
        val stringType = getTypeAsString()
        val content = getContent()
        return "{\n\"name\": \"$name\", \n\"type\": \"$stringType\", \n\"content\": $content,\n\"isPointer\": $isPointer, \n\"isComparable\": $isComparable \n}"
    }

    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("name", name)
            add("type", getTypeAsString())
        }
    }
    fun getTypeAsString(): String? {
        val type = when(type) {
            is Cunion -> (type as Cunion).name
            is Cenum -> (type as Cenum).name
            is CtypeEnum -> (type as CtypeEnum).cType
            is CuserStructure -> (type as CuserStructure).name
            else -> "not defined"
        }
        return if (isPointer) {
            "$type*"
        } else {
            type
        }
    }

    private fun getContent(): String {
        return when(type) {
            is Cunion -> (type as Cunion).toJson()
            is Cenum -> (type as Cenum).toJson()
            is CtypeEnum -> "\"" + (type as CtypeEnum).enumValue + "\""
            is CuserStructure -> (type as CuserStructure).toJson()
            else -> "not defined"
        }
    }

    override fun toString(): String {
        return getTypeAsString() + " " + name
    }
}

class CvariableModel: ItemViewModel<Cvariable>() {
    val name = bind(Cvariable::nameProperty)
    val type = bind(Cvariable::typeProperty)
    val isPointer = bind(Cvariable::isPointerProperty)
    val isComparable = bind(Cvariable::isComparableProperty)
}