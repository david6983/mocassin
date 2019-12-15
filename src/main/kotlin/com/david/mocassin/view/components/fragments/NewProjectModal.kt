package com.david.mocassin.view.components.fragments

import com.david.mocassin.controller.ProjectController
import javafx.scene.control.TextField
import tornadofx.*

class NewProjectModal: Fragment("Create a new project") {
    private val projectController: ProjectController by inject()

    private var nameField: TextField by singleAssign()

    override val root = form {
        fieldset("Enter a name") {
            textfield(projectController.name) {
                nameField = this
            }
            button("create")
        }
    }
}