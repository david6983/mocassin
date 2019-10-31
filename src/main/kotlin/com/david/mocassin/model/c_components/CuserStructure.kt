package com.david.mocassin.model.c_components

import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateException
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.OutputStreamWriter

class CuserStructure(var name: String, var isDisplayFunctionGenerated: Boolean = false) : CuserType {
    private val attributes: ArrayList<Cvariable> = ArrayList()

    fun add(value: Cvariable) = attributes.add(value)

    fun remove(value: Cvariable) = attributes.remove(value)

    fun removeAll() = attributes.clear()

    fun set(index: Int, value: Cvariable) = attributes.set(index, value)

    fun toJson(): String {
        val stringBuilder = StringBuilder("{")
        for ((index, attr) in attributes.withIndex()) {
            stringBuilder.append("\"$index\": " + attr.toJson() + ",")
        }
        stringBuilder.deleteCharAt(stringBuilder.length - 1)
        stringBuilder.append("}")
        return stringBuilder.toString()
    }

    fun attributesToDeclarationString(): String {
        val out = StringBuilder()
        val firstLetter = name.first().toLowerCase()
        for (attr in attributes) {
            out.append("\t$firstLetter.${attr.name} = ${attr.name};\n")
        }
        out.deleteCharAt(out.length - 1)
        return out.toString();
    }

    fun attributesToString(isParameters: Boolean = false): String {
        val out = StringBuilder()
        for (attr in attributes) {
            if (!isParameters) {
                out.append("\t")
            }
            out.append(attr.toString())
            if (isParameters) {
                out.append(", ")
            } else {
                out.append(";\n")
            }
        }
        if (isParameters) {
            out.delete(out.length - 2, out.length)
        }
        return out.toString()
    }

    fun getAllComparableAttribute(): List<String> {
        val out = mutableListOf<String>()
        for (attr in attributes) {
            if (attr.isComparable) {
                out.add(attr.name)
            }
        }
        return out
    }

    fun toCimplementation(config: Configuration, folderPath: String = "out/", packageName: String) {
        var temp: Template? = null
        temp = config.getTemplate("object.ftlh")
        val fileWriter = FileWriter(File("$folderPath/${packageName}_$name.c"));
        val model = mutableMapOf(Pair("object", this))
        //TODO à changer avec du polymorphisme
        model.put("project_name", CuserStructure(packageName))
        temp!!.process(model, fileWriter);
        fileWriter.close();
    }
}

