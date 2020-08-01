package com.david.mocassin.model.c_components.c_variable

import com.david.mocassin.model.c_components.*
import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.model.c_components.c_struct.CuserStructure
import com.david.mocassin.model.c_components.c_union.Cunion
import com.david.mocassin.utils.C_VARIABLE_SYNTAX_REGEX
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import javax.json.JsonObject

class Cvariable(name: String,
                type: CuserType,
                isPointer: Boolean = false,
                isComparable: Boolean = false
                ) : Ctype(type, isPointer), JsonModel {

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
            add("type", getTypeAsString() + getTypeSuffix())
            add("isPointer", isPointer)
            add("isComparable", isComparable)
        }
    }

    override fun updateModel(json: JsonObject) {
        with(json) {
            name = string("name")!!
            isPointer = boolean("isPointer")!!
            isComparable = boolean("isComparable")!!
            string("type")?.let { getTypeFromString(it) }
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

    private fun getTypeSuffix(): String {
        return when(type) {
            is Cunion -> UNION_TYPE_SUFFIX
            is Cenum -> ENUM_TYPE_SUFFIX
            is CuserStructure -> STRUCT_TYPE_SUFFIX
            else -> ""
        }
    }

    private fun getTypeFromString(string: String) {
        val str = string.replace("*", "")

        when {
            str.contains(UNION_TYPE_SUFFIX) -> type = Cunion(str.removeSuffix(UNION_TYPE_SUFFIX))
            str.contains(ENUM_TYPE_SUFFIX) -> type = Cenum(str.removeSuffix(ENUM_TYPE_SUFFIX))
            str.contains(STRUCT_TYPE_SUFFIX) -> type = CuserStructure(str.removeSuffix(STRUCT_TYPE_SUFFIX))
            str !== "not defined" -> type = CtypeEnum.find(str)!!
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

    companion object {
        fun isNameFollowCstandard(name: String): Boolean {
            return name.contains(regex = Regex(C_VARIABLE_SYNTAX_REGEX))
        }

        const val UNION_TYPE_SUFFIX = " (union)"
        const val ENUM_TYPE_SUFFIX = " (enum)"
        const val STRUCT_TYPE_SUFFIX = " (struct)"
    }
}

