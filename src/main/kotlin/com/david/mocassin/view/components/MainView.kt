package com.david.mocassin.view.components

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
                button("Generate")
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
        }
    }
}