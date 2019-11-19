package com.david.mocassin.model.c_components

import tornadofx.*

class Cenum(name: String) : CuserType {
    // name property
    var name by property(name)
    fun nameProperty() = getProperty(Cenum::name)

    // attributes property
    //val attributes: MutableMap<String, Int> = mutableMapOf()
    var attributes by property<MutableMap<String, Int>>()
    fun attributesProperty() = getProperty(Cenum::attributes)

    init {
        attributes = mutableMapOf()
    }

    fun add(name: String, value: Int = attributes.count()) {
        attributes[name] = value
    }

    fun remove(name: String, value: Int) = attributes.remove(name, value)

    fun removeAll() = attributes.clear()

    fun replace(name: String, oldValue: Int, newValue: Int) = attributes.replace(name, oldValue, newValue)

    fun attributesToString(): String {
        val out = StringBuilder()
        for (attr in attributes) {
            out.append("\t${attr.key} = ${attr.value},\n")
        }
        out.delete(out.length - 2, out.length)
        return out.toString()
    }

    fun toJson(): String {
        val stringBuilder = StringBuilder("{")
        for (attr in attributes) {
            stringBuilder.append("\"${attr.key}\": \"${attr.value}\",")
        }
        stringBuilder.deleteCharAt(stringBuilder.length - 1)
        stringBuilder.append("}")
        return stringBuilder.toString()
    }

    override fun toString(): String {
        val out = StringBuilder()
        for (attr in attributes) {
            out.append(attr.key + "|")
        }
        out.deleteCharAt(out.length - 1)
        return "$name;${out}"
    }
}
