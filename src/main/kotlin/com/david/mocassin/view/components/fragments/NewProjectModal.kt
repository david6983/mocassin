package com.david.mocassin.view.components.fragments

import com.david.mocassin.controller.MainMenuBarController
import com.david.mocassin.controller.ProjectController
import com.david.mocassin.view.MainView
import com.david.mocassin.view.components.sidebar_drawers.LeftSideDrawer
import javafx.scene.control.TextField
import tornadofx.*

class NewProjectModal: Fragment("") {
    private val projectController: ProjectController by inject()
    private val leftSideDrawer: LeftSideDrawer by inject()
    private val mainMenuBarController: MainMenuBarController by inject()

    private val mainView: MainView by inject()

    private var nameField: TextField by singleAssign()

    init {
        title = messages["npm_title"]
    }

    override val root = form {
        fieldset(messages["npm_fieldset"]) {
            textfield(messages["untitled"]) {
                nameField = this
            }
            button(messages["npm_create_btn"]).action {
                // clean up the model and update the package name
                projectController.cleanModel()
                projectController.name = nameField.text
                projectController.pathName = ProjectController.DEFAULT_PATH

                // clean up the view
                leftSideDrawer.clearAll()
                leftSideDrawer.userStructureRoot.value = nameField.text
                leftSideDrawer.generatedStructureRoot.value = nameField.text
                mainView.updatePackageNameInTitle(nameField.text)
                mainMenuBarController.enableSlist()

                close()
            }
        }
    }
}