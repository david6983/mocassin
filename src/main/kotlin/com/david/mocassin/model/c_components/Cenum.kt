package com.david.mocassin.model.c_components

import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.*
import javax.json.JsonObject

//TODO separate classes

/**
 * This class define a C programming enumeration :
 * We use the property system from javafx for better implementation with the GUI
 *
 * validation :
 * - An enumeration can have two attributes with the same value
 * - Two attributes can not have the same name
 * - enum name and attributes name can not have spaces in the string
 * - attribute name or enum name should not have a name that already exist in project
 *
 * @constructor create a C programming enumeration from a name
 * @param name Name of the enumeration in the Header file
 */
class Cenum(name: String) : CuserType, JsonModel {
    /**
     * name of the enumeration by property
     */
    val nameProperty = SimpleStringProperty(name)
    /**
     * name of the enumeration
     */
    var name: String by nameProperty

    /**
     * mutable list of attributes contains in the enumeration by property
     */
    val attributesProperty = SimpleListProperty<CenumAttribute>(mutableListOf<CenumAttribute>().asObservable())
    /**
     * attributes contains in the enumeration
     */
    var attributes: ObservableList<CenumAttribute> by attributesProperty

    /**
     * Add a new attribute to the enumeration and verify before the addition if the name is unique in the enumeration
     * and in the project. This attribute should also follow the C syntax for enumeration.
     *
     * @param name name of the attribute to verify and add on success
     * @param value enumeration value, incremental by default (start at 0)
     * @return 'true' if the addition success, 'false' if the attribute is not valid
     */
    fun add(name: String, value: Int = attributes.count()): Boolean {
        //TODO change the '&&' syntax and throw custom exceptions instead
        return if (isAttributeUniqueInEnum(name) && isNameSyntaxFollowCstandard(name)) {
            attributes?.add(CenumAttribute(name, value))
            true
        } else {
            false
        }
    }

    /**
     * remove an attribute from the name only
     * Because attributes are unique, there is no duplicates
     *
     * @param 'true' if the attribute has been removed succesfully
     */
    fun remove(name: String): Boolean = attributes.remove(attributes.find { it.name == name })

    /**
     * remove all attributess in the enumeration
     */
    fun clear() = attributes.clear()

    fun replaceValue(name: String, newValue: Int) {
        val index = attributes.indexOfFirst { it.name == name }
        attributes[index] = CenumAttribute(name, newValue)
    }

    fun replaceName(value: Int, newName: String) {
        val index = attributes.indexOfFirst { it.value == value }
        if (isNameSyntaxFollowCstandard(newName)) {
            attributes[index] = CenumAttribute(newName, value)
        }
    }

    fun isAttributeUniqueInEnum(name: String): Boolean {
        return if (isNameSyntaxFollowCstandard(name)) {
            attributes.indexOfFirst { it.name == name } == -1
        } else {
            false
        }
    }

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
    fun attributesToCformatString(): String {
        val out = StringBuilder()
        for (attr in attributes) {
            out.append("\t${attr.name} = ${attr.value},\n")
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
            stringBuilder.append("\"${attr.name}\": \"${attr.value}\",")
        }
        stringBuilder.deleteCharAt(stringBuilder.length - 1)
        stringBuilder.append("}")
        return stringBuilder.toString()
    }

    /**
     * return the attributes list as json format
     *
     * @return String json string
     */
    fun attributesToJSON(): JsonBuilder {
        val out = JsonBuilder()
        for(attr in attributes) {
            out.add(attr.name, attr.value)
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
            add("attributes", attributes.toJSON())
        }
    }

    /**
     * export the object to string format
     *
     * This function is used in model and the output string is parsed in template
     *
     * @return "$name;attribute[0]|attribute[1]|..."
     */
    override fun toString(): String {
        val out = StringBuilder()
        for (attr in attributes) {
            out.append(attr.name + "|")
        }
        out.deleteCharAt(out.length - 1)
        return "$name;${out}"
    }

    override fun updateModel(json: JsonObject) {
        with(json) {
            name = string("name").toString()
            // we need to add manually attributes because CenumAttribute has a default constructor with name and value
            // to use attributes.setAll(getJsonArray("attributes").toModel()), the constructor should be empty !
            for(obj in getJsonArray("attributes")) {
                val attr = CenumAttribute("", 0)
                attr.updateModel(obj.asJsonObject())
                attributes.add(attr)
            }
        }
    }
}

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

/**
 * This class is the model of an attribute. This class is used for validation
 */
class CenumAttributeModel: ItemViewModel<CenumAttribute>() {
    val name = bind(CenumAttribute::nameProperty, autocommit = true)
    val value = bind(CenumAttribute::valueProperty, autocommit = true)
}

/**
 * Item view model of Cenum with properties bindings
 *
 * Better integration with TableView and TreeView
 */
class CenumModel: ItemViewModel<Cenum>() {
    val name = bind(Cenum::nameProperty, autocommit = true)
    val attributes = bind(Cenum::attributesProperty, autocommit = true)
}