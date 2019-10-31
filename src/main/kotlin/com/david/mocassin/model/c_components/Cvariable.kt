package com.david.mocassin.model.c_components

class Cvariable(var name: String,
                type: CuserType,
                isPointer: Boolean = false,
                var isComparable: Boolean = false) : Ctype(type, isPointer) {
    fun toJson(): String {
        val stringType = getTypeAsString()
        val content = getContent()
        return "{\n\"name\": \"$name\", \n\"type\": \"$stringType\", \n\"content\": $content,\n\"isPointer\": $isPointer, \n\"isComparable\": $isComparable \n}"
    }

    fun getTypeAsString(): String {
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