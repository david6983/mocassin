package com.david.mocassin.controller

import com.david.mocassin.model.UserModel
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class ProjectController(projectName: String) : Controller() {
    val userModelProperty = SimpleObjectProperty<UserModel>()
    var userModel by userModelProperty

    //val dataStructuresListProperty = SimpleListProperty<DataStructureModel>()

    //TODO to finish
    fun getListOfAllNamesUsed(): ArrayList<String> {
        return arrayListOf("", "")
    }

    fun saveAsMocFile() {}

    fun generate() {}

    fun saveToWebApp() {}

    fun importFromWebApp() {}

    fun openFromFile() {}
}