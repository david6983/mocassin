package com.david.mocassin.model.c_components

import javafx.beans.property.SimpleMapProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

//TODO finish documentation and write the Unit test

/**
 * This class define a C programming enumeration :
 * We use the property system from javafx for better implementation with the GUI
 *
 * @constructor create a C programming enumeration from a name
 * @param name Name of the enumeration in the Header file
 */
class Cenum(name: String) : CuserType, JsonModel {
    /**
     * name of the enumeration by property
     */
    val nameProperty = SimpleStringProperty()
    var name by nameProperty

    val attributesProperty = SimpleMapProperty<String, Int>()
    var attributes by attributesProperty

    fun add(name: String, value: Int = attributes.count()) {
        attributes[name] = value
    }

    fun remove(name: String, value: Int) = attributes.remove(name, value)

    fun removeAll() = attributes.clear()

    fun replace(name: String, oldValue: Int, newValue: Int) = attributes.replace(name, oldValue, newValue)

    /**
     * This function should return the attributes that follow the C syntax of a enumeration
     * between "{" and "};" with indentation.
     *
     * exemple :
     *
     * enum foo {
     *    KEY0 = VALUE0,
     *    KEY1 = VALUE1
     * };
     *
     * @return the attributes of the enumeration as C format in Header file
     */
    fun attributesToString(): String {
        val out = StringBuilder()
        for (attr in attributes) {
            out.append("\t${attr.key} = ${attr.value},\n")
        }
        out.delete(out.length - 2, out.length)
        return out.toString()
    }

    /**
     * return the object as json format
     *
     * @Deprecated This function doesn't use tornadofx and the name is not saved
     * @return String json string
     */
    fun toJson(): String {
        val stringBuilder = StringBuilder("{")
        for (attr in attributes) {
            stringBuilder.append("\"${attr.key}\": \"${attr.value}\",")
        }
        stringBuilder.deleteCharAt(stringBuilder.length - 1)
        stringBuilder.append("}")
        return stringBuilder.toString()
    }

    fun attributesToJSON(): JsonBuilder {
        val out: JsonBuilder = JsonBuilder()

        for(attr in attributes) {
            out.add(attr.key, attr.value)
        }

        return out
    }

    /**
     * return a json string from the Cenum object
     *
     * @param json json builder to create the json string
     */
    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("name", name)
            add("attributes", attributesToJSON())
        }
    }
    /*
    override fun updateModel(json: JsonObject) {
        with(json) {
            name = string("name")
            val jsonArray = getJsonArray("attributes")
            for(value in jsonArray) {

            }
        }
    }*/

    override fun toString(): String {
        val out = StringBuilder()
        for (attr in attributes) {
            out.append(attr.key + "|")
        }
        out.deleteCharAt(out.length - 1)
        return "$name;${out}"
    }
}

/**
 * view model of Cenum with properties bindings
 */
class CenumModel: ItemViewModel<Cenum>() {
    val name = bind(Cenum::nameProperty, autocommit = true)
    val attributes = bind(Cenum::attributesProperty, autocommit = true)
}