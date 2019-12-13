package com.david.mocassin.view.components.sidebar_drawers

import tornadofx.*

class LeftSideDrawer : View() {
    override val root = drawer(multiselect = true) {
        item("User structures") {

        }
        item("Generated structures") {

        }
        item("Files view") {

        }
    }
}