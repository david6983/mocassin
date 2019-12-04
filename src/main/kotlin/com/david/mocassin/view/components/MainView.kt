package com.david.mocassin.view.components

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.Cenum
import com.david.mocassin.model.c_components.CtypeEnum
import com.david.mocassin.model.c_components.Cunion
import com.david.mocassin.view.styles.MainStyle
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.geometry.Pos

import tornadofx.*
import java.awt.Color

class MainView: View("Mocassin linked list generator for C programming") {
    //TODO add a controller for MainView that inject projectController inside
    private val projectController: ProjectController by inject()

    init {
        title += " [${projectController.name}]"
    }

    override val root = borderpane {
        left<LeftSideDrawer>()
        top<MainMenuBar>()
        right {
            /*
            drawer {
                item("") {

                }
            }*/
        }
        bottom {
            toolbar {
                button("Generate") {
                    addClass("btn-primary", "btn-lg")
                    action {
                        println(projectController.getListOfAllNamesUsed().toString())
                        //println(projectController.userModel.to)
                    }
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