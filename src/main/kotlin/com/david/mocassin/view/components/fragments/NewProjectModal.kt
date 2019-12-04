package com.david.mocassin.view.components.fragments

import tornadofx.*

class NewProjectModal: Fragment("Create a new project") {
    override val root = form {
        fieldset("Enter a name") {
            textfield("untitled")
            button("create")
        }
    }
}