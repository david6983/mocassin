package com.david.mocassin.view

import com.david.mocassin.controller.MainMenuBarController
import com.david.mocassin.controller.ProjectController
import com.david.mocassin.view.components.sidebar_drawers.LeftSideDrawer
import com.david.mocassin.view.components.MainMenuBar
import com.david.mocassin.view.styles.MainStyle
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TabPane

import tornadofx.*

class MainViewController : Controller() {
    val projectController: ProjectController by inject()
    val mainMenuBarController: MainMenuBarController by inject()

    fun addEditionTab() {

    }
}

class MainView: View(this.TITLE) {
    val controller : MainViewController by inject()
    var centerTabPane: TabPane by singleAssign()

    init {
        title += " [${controller.projectController.name}]"
    }

    fun updatePackageNameInTitle(name: String) {
        title = title.removeRange(title.indexOf("["), title.indexOf("]") + 1)
        title += " [${name}]"
    }

    override val root = borderpane {
        left<LeftSideDrawer>()
        top<MainMenuBar>()
        right {}
        bottom {
            toolbar {
                button("Generate") {
                    addClass("btn-primary", "btn-lg")
                }.action {
                    controller.mainMenuBarController.generateProject()
                }
                button("Test") {
                    addClass("btn-success", "btn-lg")
                }
                button("Save") {
                    addClass("btn-info", "btn-lg")
                }.action {
                    controller.mainMenuBarController.saveProjectLocally()
                }
                button("Open") {
                    addClass("btn-warning", "btn-lg")
                }.action {
                    controller.mainMenuBarController.openFromComputer()
                }
                style {
                    alignment = Pos.CENTER
                }
            }
        }
        center {
            tabpane {
                centerTabPane = this
            }
        }
    }

    init {
        with (root) {
            prefWidth = 1000.0
            prefHeight = 800.0

            addClass(MainStyle.mainView)
        }
    }

    override fun onDock() {
        currentWindow?.setOnCloseRequest { event ->
            println("Closing")
            alert(Alert.AlertType.CONFIRMATION,
                "Quit without saving",
                "Are you sure you want to quit without saving ?").let {
                when {
                    it.result.buttonData.isCancelButton -> event.consume()
                }
            }
        }
    }

    companion object {
        const val TITLE = "Mocassin - generalized data structures generator for C"
    }
}