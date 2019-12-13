package com.david.mocassin.model.c_components.c_union

import com.david.mocassin.model.c_components.CuserType
import com.david.mocassin.model.c_components.c_struct.CuserStructure
import com.david.mocassin.model.c_components.c_variable.Cvariable
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.*

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

    /**
     * check if an attribute is unique in the union
     *
     * to attributes can't have the same name
     *
     * @param name name of the attribute
     * @return 'true' if unique, 'false' if already exist
     */
    fun isAttributeUniqueInUnion(name: String): Boolean {
        return if (isNameSyntaxFollowCstandard(name)) {
            attributes.indexOfFirst { it.name == name } == -1
        } else {
            false
        }
    }

    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("name", name)
            add("variables", variablesToJSON())
        }
    }
}

