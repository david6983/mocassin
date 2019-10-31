package com.david.mocassin.model

import com.david.mocassin.model.c_components.Cenum
import com.david.mocassin.model.c_components.Cunion
import com.david.mocassin.model.c_components.CuserStructure
import com.david.mocassin.model.c_components.CuserType
import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateException
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.lang.StringBuilder


class UserModel(var packageName: String) {
    val map = mutableMapOf<String, ArrayList<CuserType>>()

    init {
        map[MAP_USER_STRUCTURE_ID] = ArrayList()
        map[MAP_USER_UNION_ID] = ArrayList()
        map[MAP_USER_ENUM_ID] = ArrayList()
        map[MAP_USER_PROJECT_NAME] = ArrayList()
    }

    fun add(element: CuserType) = when(element) {
        is CuserStructure -> {
            if ( map[MAP_USER_STRUCTURE_ID]?.find { it == element } == null) {
                map[MAP_USER_STRUCTURE_ID]?.add(element)
                true
            } else {
                false
            }
        }
        is Cunion -> {
            if ( map[MAP_USER_UNION_ID]?.find { it == element } == null) {
                map[MAP_USER_UNION_ID]?.add(element)
                true
            } else {
                false
            }
        }
        is Cenum -> {
            if ( map[MAP_USER_ENUM_ID]?.find { it == element } == null) {
                map[MAP_USER_ENUM_ID]?.add(element)
                true
            } else {
                false
            }
        }
        else -> false
    }

    fun remove(element: CuserType) = when(element) {
        is CuserStructure -> map[MAP_USER_STRUCTURE_ID]?.remove(element)
        is Cunion -> map[MAP_USER_UNION_ID]?.remove(element)
        is Cenum -> map[MAP_USER_ENUM_ID]?.remove(element)
        else -> false
    }

    fun getNumberOfDataTypeForSlist(): Int {
        return map[MAP_USER_STRUCTURE_ID]?.size!! + map[MAP_USER_ENUM_ID]?.size!!
    }

    fun getAllNamesAsEnumFormat(): String {
        val out = StringBuilder()
        for (name in map[MAP_USER_STRUCTURE_ID]!!) {
            out.append("\t" + (name as CuserStructure).name.decapitalize() + ",\n")
        }
        for (name in map[MAP_USER_ENUM_ID]!!) {
            out.append("\t" + (name as Cenum).name.decapitalize() + ",\n")
        }
        return out.toString()
    }

    fun getAllNamesAsUnionFormat(): String {
        val out = StringBuilder()
        for (name in map[MAP_USER_STRUCTURE_ID]!!) {
            out.append("\t"
                    + (name as CuserStructure).name
                    + " "
                    + name.name.decapitalize()
                    + ";\n")
        }
        for (name in map[MAP_USER_ENUM_ID]!!) {
            out.append("\t"
                    + (name as Cenum).name
                    + " "
                    + name.name.decapitalize()
                    + ";\n")
        }
        return out.toString()
    }

    fun getAllNames(): ArrayList<String> {
        val out = ArrayList<String>()
        for (name in map[MAP_USER_STRUCTURE_ID]!!) {
            out.add((name as CuserStructure).name)
        }
        for (name in map[MAP_USER_ENUM_ID]!!) {
            out.add((name as Cenum).name)
        }
        return out
    }

    fun getAllUserStructureNames(): ArrayList<String> {
        val out = ArrayList<String>()
        for (name in map[MAP_USER_STRUCTURE_ID]!!) {
            out.add((name as CuserStructure).name)
        }
        return out
    }

    fun getAllEnumNames(): ArrayList<String> {
        val out = ArrayList<String>()
        for (name in map[MAP_USER_ENUM_ID]!!) {
            out.add((name as Cenum).name)
        }
        return out
    }

    fun getAllEnumAsDisplayFormat(): ArrayList<String> {
        val out = ArrayList<String>()
        for (name in map[MAP_USER_ENUM_ID]!!) {
            out.add((name as Cenum).toString())
        }
        return out
    }

    fun generate(config: Configuration, folderPath: String = packageName) {
        addpackageNameToMap()

        var temp: Template? = null
        temp = config.getTemplate("structures.ftlh")
        val fileWriter = FileWriter(File("$folderPath/${packageName}_structures.h"))
        temp!!.process(map, fileWriter)
        fileWriter.close()

        val userStruct = map[MAP_USER_STRUCTURE_ID]
        if (userStruct != null) {
            for (struct in userStruct) {
                (struct as CuserStructure).toCimplementation(config, folderPath = "out/", packageName = packageName)
            }
        }
    }

    fun toJson(): String {
        val out = StringBuilder("{\n")
        for (name in map[MAP_USER_STRUCTURE_ID]!!) {
            out.append("\"struct ${(name as CuserStructure).name}\" : ${name.toJson()},\n")
        }
        for (name in map[MAP_USER_ENUM_ID]!!) {
            out.append("\"enum ${(name as Cenum).name}\" : ${name.toJson()},\n")
        }
        for (name in map[MAP_USER_UNION_ID]!!) {
            out.append("\"${(name as Cunion).name}\" : ${name.toJson()},\n")
        }
        out.delete(out.length - 2, out.length)
        out.append("}")
        return out.toString()
    }

    fun save(pathDir: String) = File("${pathDir}/${packageName}_userModel.moc").writeText(toJson())

    private fun addpackageNameToMap() {
        //TODO extract this class to add more parameters
        class packageName(val name: String): CuserType
        val name = packageName(packageName)
        map[MAP_USER_PROJECT_NAME]?.add(name)
    }

    companion object {
        const val MAP_USER_STRUCTURE_ID = "user_structures"
        const val MAP_USER_UNION_ID = "user_unions"
        const val MAP_USER_ENUM_ID = "user_enums"
        const val MAP_USER_PROJECT_NAME = "user_project_name"

    }
}