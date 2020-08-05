package com.david.mocassin.controller

import com.david.mocassin.model.DataStructure
import com.david.mocassin.model.DataStructureEnum
import com.david.mocassin.model.user_model.UserModel
import com.david.mocassin.model.c_components.CtypeEnum
import com.david.mocassin.model.c_components.c_struct.CuserStructure
import com.david.mocassin.utils.saveStringToBinaryFile
import com.david.mocassin.view.components.sidebar_drawers.LeftSideDrawer
import edu.upm.david.security.gamalcrypt.cryptography.Cryptorithm
import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.*
import java.io.File
import java.io.IOException
import javax.json.JsonObject

/**
 * A project can be considerate in two ways :
 * - A project is your C main program, so the name of the software
 * should be the name of the project
 * example : for a family tree manager software, the name might be "FAMILY_TREE"
 *
 * - A project is one part (like a subfolder) of a software
 * example : The generated data structures are used to create geometric shapes in a drawing
 * software, so the project name could be "GEOMETRY"
 *
 * @constructor
 * give the projectName
 *
 */
class ProjectController: Controller(), JsonModel {
    // 1 model by project
    private val userModelProperty = SimpleObjectProperty<UserModel>()
    var userModel: UserModel by userModelProperty

    private val userDataStructuresProperty = SimpleListProperty<DataStructure>()
    var userDataStructures: ObservableList<DataStructure> by userDataStructuresProperty

    private val pathProperty = SimpleStringProperty(DEFAULT_PATH)
    var pathName by pathProperty

    val pathMocFileProperty = SimpleStringProperty(DEFAULT_PATH)
    var pathMocFile by pathMocFileProperty

    private val fileListProperty = SimpleListProperty<String>()
    var fileList by fileListProperty

    private val leftSideDrawer: LeftSideDrawer by inject()

    var nameDefault = "untitled"

    var name: String
        get() = userModel.packageName
        set(name) { userModel.packageName = name }

    val cfg = Configuration(Configuration.VERSION_2_3_29)

    // not sure about having only one dataStructureModel or one
    // each time the user wants to create a data structure
    //val dataStructuresModelProperty = SimpleObjectProperty<DataStructureModel>()
    //var dataStructureModel by dataStructuresModelProperty

    init {
        userModel = UserModel(nameDefault)
        userDataStructures = mutableListOf<DataStructure>().toObservable()

        fileList = mutableListOf<String>().toObservable()

        initTemplateConfiguration()
    }

    fun cleanModel() {
        userModel.removeAll()
    }

    private fun initTemplateConfiguration() {
        /* Create and adjust the configuration singleton */
        try {
            cfg.setClassForTemplateLoading(resources::class.java, "/templates/")
        } catch (e: IOException) {
            //TODO replace by a logger
            e.printStackTrace()
        }

        // Recommended settings for new projects:
        cfg.defaultEncoding = "UTF-8"
        cfg.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
        cfg.logTemplateExceptions = false
        cfg.wrapUncheckedExceptions = true
        cfg.fallbackOnNullLoopVariable = false
    }

    fun getListOfAllNamesUsed(): ArrayList<String> {
        val names = ArrayList<String>()
        names.addAll(userModel.getAllNames())
        return names
    }

    fun getListOfAllNamesUsed(type: String): ArrayList<String> {
        val names = ArrayList<String>()
        when(type) {
            ENUM -> names.addAll(userModel.getAllEnumNames())
            UNION -> names.addAll(userModel.getAllUnionNames())
            STRUCT -> names.addAll(userModel.getAllUserStructureNames())
        }
        return names
    }

    fun getListOfAllNamesUsedWithTypes(): ArrayList<String> {
        val names = ArrayList<String>()
        names.addAll(userModel.getAllNamesAndTypes())
        return names
    }

    override fun toJSON(json: JsonBuilder) {
        // merge all json in one moc file
        with(json) {
            add("userModel", userModel.toJSON())
            add("userVariables", userDataStructures.toJSON())
            add("pathGeneratedFiles", pathName)
        }
    }

    override fun updateModel(json: JsonObject) {
        with(json) {
            userModel.updateModel(getJsonObject("userModel"))
            pathName = string("pathGeneratedFiles")

            getJsonArray("userVariables")?.forEach { dataStructure ->
                DataStructure(userModel, DataStructureEnum.SLIST).let { struct ->
                    struct.updateModel(dataStructure.asJsonObject())
                    userDataStructures.add(struct)
                }
            }
        }

        leftSideDrawer.controller.updateEnumTree(leftSideDrawer.enumRoot)
        leftSideDrawer.controller.updateUnionTree(leftSideDrawer.unionRoot)
        leftSideDrawer.controller.updateStructTree(leftSideDrawer.structRoot)

        leftSideDrawer.controller.updateSlistTree(leftSideDrawer.slistRoot)

        if (pathName != DEFAULT_PATH) {
            // it means, we have already generated files, we need to generate the file list
            fileList.clear()
            updateFilesList()
            updateFilesListUiTree()
        }
    }

    fun saveToMocFile(pathDir: String?) {
        if (pathDir != null) {
            pathMocFile = pathDir
            val directory = File(pathDir)

            // the directory doesn't exist
            if (! directory.exists()) {
                // create one
                directory.mkdir()
            }
            // create a new file
            //File("${pathDir}/${name}.moc").writeText(toJSON().toString())
            saveStringToBinaryFile("${pathDir}/${name}.moc", Cryptorithm.encrypt(toJSON().toString()))
            information("Saving success", "the project has been saved successfully in $pathDir as $name.moc")
        }
    }

    fun generate(pathDir: String) {
        pathName = pathDir
        userModel.generate(cfg, pathDir)

        userDataStructures.forEach {
            it.generate(cfg, pathDir)
        }

        updateFilesList()
        updateFilesListUiTree()
    }

    private fun updateFilesList() {
        if (fileList.isEmpty()) {
            fileList.add("${userModel.packageName}_structures.h")

            userModel.userStructureList.forEach {
                fileList.add("${userModel.packageName}_${(it as CuserStructure).name}.c")
            }
            userDataStructures.forEach {
                fileList.add("${userModel.packageName}_${it.type.shortname}.h")
                fileList.add("${userModel.packageName}_${it.type.shortname}.c")
            }
        }
    }

    private fun updateFilesListUiTree() {
        leftSideDrawer.filesList.items.setAll(fileList)
        leftSideDrawer.fileDrawerItem.expanded = true
    }

    // launch Cunit Test (why not using a server image ?)
    fun verifyGeneratedCfiles() {}

    fun getObservableListOfTypes(): ObservableList<String> {
        val items: ObservableList<String> = FXCollections.observableArrayList()
        items.addAll(getListOfAllNamesUsedWithTypes())
        items.addAll(CtypeEnum.toObservableArrayList())
        return items
    }

    /*
    fun saveToWebApp() {}

    fun importFromWebApp() {}
    */

    fun openFromFile() {

    }

    /**
     * Verify if a given name is nalready taken in a project scope
     *
     * @param name name of the variable or attribute
     * @param project current project
     * @return 'true" if the given name doesn't exist in the project
     */
    fun isNameUnique(name: String): Boolean {
        return !userModel.getAllNames().contains(name)
    }

    fun isNameUniqueExcept(name: String, except: List<String>): Boolean {
        val tmpNames = userModel.getAllNames()
        tmpNames.removeAll(except)
        return !tmpNames.contains(name)
    }

    companion object {
        const val ENUM = "enum"
        const val UNION = "union"
        const val STRUCT = "struct"
        const val DEFAULT_PATH = "undefined"
    }
}
