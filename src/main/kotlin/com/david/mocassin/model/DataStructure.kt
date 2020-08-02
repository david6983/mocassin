package com.david.mocassin.model

import com.david.mocassin.model.c_components.CtypeEnum
import com.david.mocassin.model.user_model.UserModel
import freemarker.template.Configuration
import freemarker.template.Template
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.asObservable
import java.io.File
import java.io.FileWriter

import tornadofx.*
import javax.json.Json
import javax.json.JsonArray
import javax.json.JsonArrayBuilder
import javax.json.JsonObject

/**
 * DataStructure represent either a Slist, Dlist, Tree, BinaryTree and so on
 *
 * @property userModel 
 */
class DataStructure(userModel: UserModel, type: DataStructureEnum): JsonModel {
    private val map = mutableMapOf<String, String>()

    val userVariablesProperty = SimpleListProperty(ArrayList<CtypeEnum>().asObservable())
    var userVariables by userVariablesProperty

    val userModelProperty = SimpleObjectProperty(userModel)
    var userModel by userModelProperty

    val typeProperty = SimpleObjectProperty(type)
    var type by typeProperty

    fun addVariable(type: CtypeEnum): Boolean {
        return if (userVariables.find {type == it} == null) {
            userVariables.add(type)
            true
        } else {
            false
        }
    }

    fun removeVariable(type : CtypeEnum) {
        userVariables.remove(type)
    }

    fun generate(config: Configuration, folderPath: String = userModel.packageName) {
        updateModel()

        val directory = File(folderPath)
        // the directory doesn't exist
        if (! directory.exists()) {
            // create one
            directory.mkdir()
        }
        toCheader(config, folderPath)
        toCimplementation(config, folderPath)
    }

    private fun updateModel() {
        map["nb_data_type"] = (userModel.getNumberOfDataType() + userVariables.size).toString()
        map["slist_data_type"] = userModel.getAllNamesAsEnumFormat() + getAllVariableNamesAsEnumFormat()
        map["slist_single_data"] = userModel.getAllNamesAsUnionFormat() + getAllVariableNamesAsUnionFormat()
        map["slist_data_names"] = userModel.getAllNamesForComparison().toString()
        map["slist_data_types"] = (userModel.getAllNamesForComparison() + getAllVariableNames()).toString()
        map["slist_data_variables"] = getAllVariableForDisplayFormat().toString()
        map["slist_data_user_structs"] = userModel.getAllUserStructureNames().toString()
        map["slist_data_enums_name"] = userModel.getAllEnumAsDisplayFormat().toString()
        map["project_name"] = userModel.packageName
    }

    private fun getAllVariableNamesAsEnumFormat(): String {
        val out = StringBuilder("")
        if (userVariables.isNotEmpty()) {
            userVariables.forEach { type ->
                out.append("\t" + type.enumValue + ",\n")
            }
            out.deleteCharAt(out.length - 1)
        }
        return out.toString()
    }

    private fun getAllVariableNamesAsUnionFormat(): String {
        val out = StringBuilder("")
        if (userVariables.isNotEmpty()) {
            userVariables.forEach { type ->
                out.append("\t${type.cType} ${type.enumValue};\n")
            }
            out.deleteCharAt(out.length - 1)
        }
        return out.toString()
    }

    private fun getAllVariableNames(): ArrayList<String> {
        val out = ArrayList<String>()
        if (userVariables.isNotEmpty()) {
            userVariables.forEach { type ->
                out.add(type.enumValue)
            }
        }
        return out
    }

    private fun getAllVariableForDisplayFormat(): ArrayList<String> {
        val out = ArrayList<String>()
        for (type in userVariables) {
            out.add(type.enumValue + ";" + type.displaySymbol)
        }
        return out
    }

    private fun getAllVariableTypes(): ArrayList<String> {
        val out = ArrayList<String>()
        if (userVariables.isNotEmpty()) {
            userVariables.forEach { type ->
                out.add(type.cType)
            }
        }
        return out
    }


    private fun toCheader(config: Configuration, folderPath: String = userModel.packageName) {
        //TODO change according to the data structure type
        val temp: Template? = config.getTemplate("${type.shortname}_header.ftlh")

        val fileWriter = FileWriter(File("$folderPath/${userModel.packageName}_${type.shortname}.h"))
        temp!!.process(map, fileWriter)
        fileWriter.close()
    }

    private fun toCimplementation(config: Configuration, folderPath: String = userModel.packageName) {
        val temp: Template? = config.getTemplate("${type.shortname}_imp.ftlh")

        val fileWriter = FileWriter(File("$folderPath/${userModel.packageName}_${type.shortname}.c"))
        temp!!.process(map, fileWriter)
        fileWriter.close()
    }

    fun toJson(): String {
        val out = StringBuilder("{\n")
        out.append("\"userModel\": ${userModel.toJson()},")
        out.append("\"userVariables\": {\n")
        for ((index, variable) in userVariables.withIndex()) {
            out.append("\"${index}\": \"${variable}\",")
        }
        out.delete(out.length - 1, out.length)
        out.append("}")
        out.append("}")
        return out.toString()
    }

    private fun variablesToJSON(): JsonArrayBuilder? {
        val out = Json.createArrayBuilder()

        for(attr in userVariables) {
            out.add(attr.toString())
        }

        return out
    }

    override fun toJSON(json: JsonBuilder) {
        with(json) {
            add("name", type.shortname)
            add("variables", variablesToJSON())
        }
    }

    override fun updateModel(json: JsonObject) {
        with(json) {
            type = string("name")?.let { DataStructureEnum.find(it) }
            getJsonArray("variables")?.forEach { variable ->
                CtypeEnum.find(variable.toString().removeSurrounding("\"", "\""))?.let { enum ->
                    userVariables.add(enum)
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
        File("${pathDir}/${userModel.packageName}_slistModel.moc").writeText(toJson())
    }
}