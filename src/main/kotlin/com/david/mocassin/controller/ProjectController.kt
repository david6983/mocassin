package com.david.mocassin.controller

import com.david.mocassin.model.UserModel
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

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
class ProjectController: Controller() {
    // 1 model by project
    val userModelProperty = SimpleObjectProperty<UserModel>()
    var userModel by userModelProperty

    var name = "untitled"

    // not sure about having only one dataStructureModel or one
    // each time the user wants to create a data structure
    //val dataStructuresModelProperty = SimpleObjectProperty<DataStructureModel>()
    //var dataStructureModel by dataStructuresModelProperty

    init {
        userModel = UserModel(name)
    }

    //TODO to finish
    fun getListOfAllNamesUsed(): ArrayList<String> {
        val names = ArrayList<String>()
        names.addAll(userModel.getAllNames())
        // add names in slist also
        return names
    }

    fun saveMocFiles(pathDir: String) {
        userModel.save(pathDir)
    }

    fun generate(pathDir: String) {
        //TODO where the configuration goes to ?
    }

    // launch Cunit Test (why not using a server image ?)
    fun verifyGeneratedCfiles() {}

    /*
    fun saveToWebApp() {}

    fun importFromWebApp() {}
    */

    fun openFromFile() {}

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
}