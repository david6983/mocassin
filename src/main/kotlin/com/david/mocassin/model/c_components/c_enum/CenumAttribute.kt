package com.david.mocassin.model.c_components.c_enum

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.JsonObject

/**
 * Represents an attribute in an enumeration which is simply a name and a value
 * We didn't used a Map<String, Int> directly in the Cenum object for two reasons :
 * - We can use inheritance with JsonModel to take advantage of json parsing from tornadofx
 * - There is a better integration with the View and TableView in tornadofx
 *
 * This class is singleton / bean with properties
 *
 * @constructor
 * create a CenumAttribute object from a name and a value
 *
 *
 * @param name name of the attribute
 * @param value integer value of the attribute
 */
class CenumAttribute(name: String, value: Int): JsonModel {
    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    val valueProperty = SimpleIntegerProperty(value)
    var value by valueProperty

    override fun updateModel(json: JsonObject) {
        with(json) {
            name = string("name")
            value = int("value")!!
        }
    }

    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("name", name)
            add("value", value)
        }
    }
}
