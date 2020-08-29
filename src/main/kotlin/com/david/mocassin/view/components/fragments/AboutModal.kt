package com.david.mocassin.view.components.fragments

import javafx.scene.control.SelectionMode
import javafx.scene.image.ImageView
import tornadofx.*
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

        hyperlink(messages["am_logo_designed_by"]) {
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

        label(messages["am_useful_links"]) {
            paddingBottom = 12
            style {
                fontSize = 16.0.px
            }
            textFill = c("black")
        }


        listview<String> {
            items.add(messages["am_link_1"])
            items.add(messages["am_link_2"])
            items.add(messages["am_link_3"])

            selectionModel.selectionMode = SelectionMode.SINGLE

            onDoubleClick {
                when(selectionModel.selectedIndex) {
                    1 -> hostServices.showDocument("https://github.com/david6983/mocassin")
                    2 -> hostServices.showDocument("https://www.instagram.com/saiko2b/")
                    3 -> hostServices.showDocument("https://github.com/david6983/")
                }
            }
        }

        label(messages["am_help_1"]) {
            textFill = c("red")
            paddingTop = 12
            style {
                fontSize = 12.0.px
            }
        }

        label(messages["am_help_2"]) {
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

        prefWidth = 260.0
        prefHeight = 290.0
    }
}