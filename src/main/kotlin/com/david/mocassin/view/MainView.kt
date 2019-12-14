package com.david.mocassin.view

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.CtypeEnum
import com.david.mocassin.view.components.sidebar_drawers.LeftSideDrawer
import com.david.mocassin.view.components.MainMenuBar
import com.david.mocassin.view.styles.MainStyle
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.TreeItem

import tornadofx.*

class MainView: View("Mocassin Generalized Data structure generator for C") {
    //TODO add a controller for MainView that inject projectController inside
    private val projectController: ProjectController by inject()
    private val leftSideDrawer: LeftSideDrawer by inject()

    init {
        title += " [${projectController.name}]"
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
            val fileView = tabpane {}
            fileView.tab("test") {
                val selectedType = SimpleStringProperty()
                hbox {
                    combobox<String>(selectedType){
                        for(type in CtypeEnum.values()) {
                            items.add(type.cType)
                        }
                        //TODO add types from userModel
                    }.selectionModel.selectFirst()
                    button("click").action {
                        println(selectedType.value)
                    }
                }
                selectedType.onChange { println(CtypeEnum.find(selectedType.value)) }
            }
            fileView.tab("test 2")
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