package com.david.mocassin.view.components.fragments

import javafx.geometry.Pos
import javafx.scene.control.SelectionMode
import javafx.scene.image.ImageView
import tornadofx.*
import java.awt.Color
import java.time.LocalDate

class AboutModal: Fragment("") {
    var img: ImageView by singleAssign()

    init {
        title = messages["mb_about"]
    }

    override val root = vbox {
        hyperlink(messages["created_by"]) {
            paddingBottom = 12

            setOnAction {
                hostServices.showDocument("https://github.com/david6983")
            }

            border = null

            textFill = c("black")

            tooltip("https://github.com/david6983")
        }

        hyperlink("logo designed by @saiko2b") {
            paddingBottom = 12

            setOnAction {
                hostServices.showDocument("https://www.instagram.com/saiko2b/")
            }

            border = null

            textFill = c("black")

            tooltip("https://www.instagram.com/saiko2b/")
        }

        label("Copyright ©${LocalDate.now().year}") {
            paddingBottom = 12
            style {
                fontSize = 14.0.px
            }
        }

        label("Useful links: ") {
            paddingBottom = 12
            style {
                fontSize = 16.0.px
            }
            textFill = c("black")
        }


        listview<String> {
            items.add("Mocassin official repository")
            items.add("Logo designer instagram")
            items.add("Developer github repository")

            selectionModel.selectionMode = SelectionMode.SINGLE

            onDoubleClick {
                when(selectionModel.selectedIndex) {
                    1 -> hostServices.showDocument("https://github.com/david6983/mocassin")
                    2 -> hostServices.showDocument("https://www.instagram.com/saiko2b/")
                    3 -> hostServices.showDocument("https://github.com/david6983/")
                }
            }
        }

        label("*click on the authors name to see more") {
            textFill = c("red")
            paddingTop = 12
            style {
                fontSize = 12.0.px
            }
        }

        label("**double click on a link in the list above") {
            textFill = c("green")
            paddingBottom = 5
            style {
                fontSize = 12.0.px
            }
        }

        style {
            fontSize = 16.0.px
        }

        vboxConstraints {
            paddingTop = 12.0
            paddingBottom = 12.0
            paddingLeft = 12.0
            paddingRight = 12.0
        }

        prefWidth = 250.0
        prefHeight = 286.0
    }
}