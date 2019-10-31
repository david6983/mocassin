package com.david.mocassin.view.components

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