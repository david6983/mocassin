package com.david.mocassin.model.user_model

import com.david.mocassin.model.c_components.*
import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.model.c_components.c_enum.CenumAttribute
import com.david.mocassin.model.c_components.c_struct.CuserStructure
import com.david.mocassin.model.c_components.c_union.Cunion
import freemarker.template.Configuration
import freemarker.template.Template
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import java.io.File
import java.io.FileWriter
import java.lang.StringBuilder

import tornadofx.*
import javax.json.Json
import javax.json.JsonArrayBuilder
import javax.json.JsonObject
import javax.json.JsonValue

//TODO JSON with tornadofx

class UserModel(packageName: String) : JsonModel {
    val packageNameProperty = SimpleStringProperty(packageName)
    var packageName by packageNameProperty

    val userStructureListProperty = SimpleListProperty<CuserType>(ArrayList<CuserType>().asObservable())
    var userStructureList by userStructureListProperty

    val userUnionListProperty = SimpleListProperty<CuserType>(ArrayList<CuserType>().asObservable())
    var userUnionList by userUnionListProperty

    val userEnumListProperty = SimpleListProperty<CuserType>(ArrayList<CuserType>().asObservable())
    var userEnumList by userEnumListProperty

    fun add(element: CuserType) = when(element) {
        is CuserStructure -> {
            if ( userStructureList?.find { it == element } == null) {
                userStructureList?.add(element)
                true
            } else {
                false
            }
        }
        is Cunion -> {
            if ( userUnionList?.find { it == element } == null) {
                userUnionList?.add(element)
                true
            } else {
                false
            }
        }
        is Cenum -> {
            if ( userEnumList?.find { it == element } == null) {
                userEnumList?.add(element)
                true
            } else {
                false
            }
        }
        else -> false
    }

    fun findEnumByName(name: String?): Cenum {
        return (userEnumList.find { (it as Cenum).name == name } as Cenum)
    }

    fun findUnionByName(name: String?): Cunion {
        return (userUnionList.find { (it as Cunion).name == name } as Cunion)
    }

    fun findStructByName(name: String?): CuserStructure {
        return (userStructureList.find { (it as CuserStructure).name == name } as CuserStructure)
    }

    fun remove(element: CuserType) = when(element) {
        is CuserStructure -> userStructureList?.remove(element)
        is Cunion -> userUnionList?.remove(element)
        is Cenum -> userEnumList?.remove(element)
        else -> false
    }

    fun removeAll() {
        userEnumList.clear()
        userStructureList.clear()
        userUnionList.clear()
    }

    fun getNumberOfDataType(): Int {
        return userStructureList?.size!! + userEnumList?.size!! + userUnionList?.size!!
    }

    fun getAllNamesAsEnumFormat(): String {
        val out = StringBuilder()
        for (name in userStructureList!!) {
            out.append("\t" + (name as CuserStructure).name.decapitalize() + ",\n")
        }
        for (name in userEnumList) {
            out.append("\t" + (name as Cenum).name.decapitalize() + ",\n")
        }
        for (name in userUnionList) {
            out.append("\t" + (name as Cunion).name.decapitalize() + ",\n")
        }
        return out.toString()
    }

    fun getAllNamesAsUnionFormat(): String {
        val out = StringBuilder()
        for (name in userStructureList!!) {
            out.append("\t"
                    + (name as CuserStructure).name
                    + " "
                    + name.name.decapitalize()
                    + ";\n")
        }
        for (name in userEnumList) {
            out.append("\t"
                    + (name as Cenum).name
                    + " "
                    + name.name.decapitalize()
                    + ";\n")
        }
        for (name in userUnionList) {
            out.append("\t"
                    + (name as Cunion).name
                    + " "
                    + name.name.decapitalize()
                    + ";\n")
        }
        return out.toString()
    }

    fun getAllNamesForComparison(): ArrayList<String> {
        val out = ArrayList<String>()
        for (name in userStructureList!!) {
            out.add((name as CuserStructure).name)
        }
        for (name in userEnumList) {
            out.add((name as Cenum).name)
        }
        // don't add union because we can't compare two unions
        return out
    }

    fun getAllNames(): ArrayList<String> {
        val out = ArrayList<String>()
        for (name in userStructureList!!) {
            out.add((name as CuserStructure).name)
        }
        for (name in userEnumList) {
            out.add((name as Cenum).name)
        }
        for (name in userUnionList) {
            out.add((name as Cunion).name)
        }
        return out
    }

    fun getAllNamesAndTypes(): ArrayList<String> {
        val out = ArrayList<String>()
        for (name in userStructureList!!) {
            out.add((name as CuserStructure).name + " [struct]")
        }
        for (name in userEnumList) {
            out.add((name as Cenum).name + " [enum]")
        }
        for (name in userUnionList) {
            out.add((name as Cunion).name + " [union]")
        }
        return out
    }

    fun getAllUserStructureNames(): ArrayList<String> {
        val out = ArrayList<String>()
        for (name in userStructureList!!) {
            out.add((name as CuserStructure).name)
        }
        return out
    }

    fun getAllEnumNames(): ArrayList<String> {
        val out = ArrayList<String>()
        for (name in userEnumList) {
            out.add((name as Cenum).name)
        }
        return out
    }

    fun getAllUnionNames(): ArrayList<String> {
        val out = ArrayList<String>()
        for (name in userUnionList) {
            out.add((name as Cunion).name)
        }
        return out
    }

    fun getAllEnumAsDisplayFormat(): ArrayList<String> {
        val out = ArrayList<String>()
        for (name in userEnumList) {
            out.add((name as Cenum).toString())
        }
        return out
    }

    fun isEmpty(): Boolean {
        return (userEnumList.isEmpty() && userUnionList.isEmpty() && userStructureList.isEmpty())
    }

    fun generate(config: Configuration, folderPath: String = packageName) {
        val map = mutableMapOf<String, ObservableList<CuserType>>()

        map[MAP_USER_STRUCTURE_ID] = userStructureList
        map[MAP_USER_UNION_ID] = userUnionList
        map[MAP_USER_ENUM_ID] = userEnumList
        map[MAP_USER_PROJECT_NAME] = addPackageNameToMap()
        
        val directory = File(folderPath)
        // the directory doesn't exist
        if (! directory.exists()) {
            // create one
            directory.mkdir()
        }

        val temp: Template? = config.getTemplate("structures.ftlh")
        val fileWriter = FileWriter(File("$folderPath/${packageName}_structures.h"))
        temp!!.process(map, fileWriter)
        fileWriter.close()

        // create .c and .h for user structures
        val userStruct = userStructureList
        if (userStruct != null) {
            for (struct in userStruct) {
                (struct as CuserStructure).toCimplementation(config, folderPath = folderPath, packageName = packageName)
            }
        }
    }

    fun toJson(): String {
        val out = StringBuilder("{\n")
        for (name in userStructureList) {
            out.append("\"struct ${(name as CuserStructure).name}\" : ${name.toJson()},\n")
        }
        for (name in userEnumList) {
            out.append("\"enum ${(name as Cenum).name}\" : ${name.toJson()},\n")
        }
        for (name in userUnionList) {
            out.append("\"union ${(name as Cunion).name}\" : ${name.toJson()},\n")
        }
        out.delete(out.length - 2, out.length)
        out.append("}")
        return out.toString()
    }

    private fun enumsToJSON(): JsonArrayBuilder {
        val out = Json.createArrayBuilder()

        for(enum in userEnumList) {
            out.add((enum as Cenum).toJSON())
        }

        return out
    }

    private fun unionsToJSON(): JsonArrayBuilder {
        val out = Json.createArrayBuilder()

        for(union in userUnionList) {
            out.add((union as Cunion).toJSON())
        }

        return out
    }

    private fun structsToJSON(): JsonArrayBuilder {
        val out = Json.createArrayBuilder()

        for(struct in userStructureList) {
            out.add((struct as CuserStructure).toJSON())
        }

        return out
    }

    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("enums", enumsToJSON())
            add("unions", unionsToJSON())
            add("structs", structsToJSON())
        }
    }

    override fun updateModel(json: JsonObject) {
        with(json) {
            getJsonArray("enums")?.forEach { enum ->
                with(enum.asJsonObject()) {
                    Cenum("").let { e ->
                        e.updateModel(this)
                        userEnumList.add(e)
                    }
                }
            }
            getJsonArray("unions")?.forEach { union ->
                with(union.asJsonObject()) {
                    Cunion("").let { u ->
                        u.updateModel(this)
                        userUnionList.add(u)
                    }
                }
            }
            getJsonArray("structs")?.forEach { struct ->
                with(struct.asJsonObject()) {
                    CuserStructure("").let { s ->
                        s.updateModel(this)
                        userStructureList.add(s)
                    }
                }
            }
        }
    }

    fun save(pathDir: String) {
        val directory = File(pathDir)

        // the directory doesn't exist
        if (! directory.exists()) {
            // create one
            directory.mkdir()
        }
        // create a new file
        File("${pathDir}/${packageName}_userModel.moc").writeText(toJSON().toString())
    }

    private fun addPackageNameToMap(): ObservableList<CuserType> {
        return arrayListOf<CuserType>(PackageName(packageName)).asObservable()
    }

    companion object {
        const val MAP_USER_STRUCTURE_ID = "user_structures"
        const val MAP_USER_UNION_ID = "user_unions"
        const val MAP_USER_ENUM_ID = "user_enums"
        const val MAP_USER_PROJECT_NAME = "user_project_name"
    }
}

