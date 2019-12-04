package com.david.mocassin.model.c_components

import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.*
import javax.json.JsonObject

//TODO JSON

class Cunion(name: String) : CuserType, JsonModel  {
    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    val attributesProperty = SimpleListProperty<Cvariable>(mutableListOf<Cvariable>().asObservable())
    var attributes: ObservableList<Cvariable> by attributesProperty

    fun add(value: Cvariable): Boolean = when(value.type) {
        is CuserStructure -> {
            value.isPointer = true
            attributes.add(value)
            true
        }
        is Cunion -> {
            if (value.name != name) {
                attributes.add(value)
                true
            } else {
                false
            }
        }
        else -> {
            attributes.add(value)
            true
        }
    }

    fun remove(name: String): Boolean = attributes.remove(attributes.find { it.name == name })

    fun clear() = attributes.clear()

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

    fun variablesToJSON(): JsonBuilder {
        val out = JsonBuilder()

        for(attr in attributes) {
            out.add(attr.name, attr.toJSON())
        }

        return out
    }

    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("name", name)
            add("variables", variablesToJSON())
        }
    }
}

class CunionModel: ItemViewModel<Cunion>() {
    val name = bind(Cunion::nameProperty, autocommit = true)
    val attributes = bind(Cunion::attributesProperty, autocommit = true)
}