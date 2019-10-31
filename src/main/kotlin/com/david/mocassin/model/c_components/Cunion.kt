package com.david.mocassin.model.c_components

class Cunion(var name: String) : CuserType {
    private val attributes: ArrayList<Cvariable> = ArrayList()

    init {
        name = "union $name"
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