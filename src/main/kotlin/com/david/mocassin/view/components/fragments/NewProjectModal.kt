package com.david.mocassin.view.components.fragments

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.view.MainView
import com.david.mocassin.view.components.sidebar_drawers.LeftSideDrawer
import javafx.scene.control.TextField
import tornadofx.*

class NewProjectModal: Fragment("Create a new project") {
    private val projectController: ProjectController by inject()
    private val leftSideDrawer: LeftSideDrawer by inject()

    private val mainView: MainView by inject()

    private var nameField: TextField by singleAssign()

    override val root = form {
        fieldset("Enter a name") {
            textfield("untitled") {
                nameField = this
            }
            button("create").action {
                // clean up the model and update the package name
                projectController.cleanModel()
                projectController.name = nameField.text

                // clean up the view
                leftSideDrawer.clearAll()
                leftSideDrawer.userStructureRoot.value = nameField.text
                mainView.updatePackageNameInTitle(nameField.text)

                close()
            }
        }
    }
}