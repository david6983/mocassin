package com.david.mocassin.view.components

import com.david.mocassin.view.styles.MainStyle
import tornadofx.*

class MainView: View("Mocassin linked list generator for C programming") {
    override val root = borderpane {
        top<MainMenuBar>()
        left<LeftSideDrawer>()
        right {
            drawer {
                item("") {

                }
            }
        }
        bottom {
            toolbar {
                button("Generate") {
                    addClass("btn-primary", "btn-lg")
                }
            }
        }
        center {
            val fileView = tabpane {}
            fileView.tab("test")
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