package com.david.mocassin.model.c_components

import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.*

//TODO JSON

class Cunion(name: String) : CuserType {
    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    val attributesProperty = SimpleListProperty<Cvariable>(mutableListOf<Cvariable>().asObservable())
    var attributes: ObservableList<Cvariable> by attributesProperty
    //private val attributes: ArrayList<Cvariable> = ArrayList()
    init {
        //nameProperty.value = "union $name" //TODO cette merde fait dla merde dans l'interface graphique
    }

    fun add(value: Cvariable) = when(value.type) {
        is CuserStructure -> {
            value.isPointer = true
            attributes.add(value)
        }
        else -> attributes.add(value)
    }

    fun remove(value: Cvariable) = attributes.remove(value)

    fun removeAll() = attributes.clear()

    fun attributesToString(): String {
        val out = StringBuilder()
        for (attr in attributes) {
            out.append("\t${attr.toString()};\n")
        }
        out.deleteCharAt(out.length - 1)
        return out.toString()
    }

    fun toJson(): String {
        val stringBuilder = StringBuilder("{\n")
        for ((index, attr) in attributes.withIndex()) {
            stringBuilder.append("\"$index\": " + attr.toJson() + ", \n")
        }
        stringBuilder.deleteCharAt(stringBuilder.length - 3)
        stringBuilder.append("}")
        return stringBuilder.toString()
    }
}

class CunionModel: ItemViewModel<Cunion>() {
    val name = bind(Cunion::nameProperty)
    val attributes = bind(Cunion::attributesProperty)
}