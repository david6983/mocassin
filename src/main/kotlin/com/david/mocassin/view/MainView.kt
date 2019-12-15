package com.david.mocassin.view

import com.david.mocassin.controller.MainMenuBarController
import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.CtypeEnum
import com.david.mocassin.view.components.sidebar_drawers.LeftSideDrawer
import com.david.mocassin.view.components.MainMenuBar
import com.david.mocassin.view.styles.MainStyle
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.TabPane
import javafx.scene.control.TreeItem

import tornadofx.*

class MainViewController : Controller() {
    val projectController: ProjectController by inject()
    val mainMenuBarController: MainMenuBarController by inject()

    fun addEditionTab() {

    }
}

class MainView: View("Mocassin Generalized Data structure generator for C") {
    val controller : MainViewController by inject()
    var centerTabPane: TabPane by singleAssign()

    init {
        title += " [${controller.projectController.name}]"
    }

    override val root = borderpane {
        left<LeftSideDrawer>()
        top<MainMenuBar>()
        right {}
        bottom {
            toolbar {
                button("Generate") {
                    addClass("btn-primary", "btn-lg")
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
}