package com.david.mocassin.view.components.fragments

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import com.david.mocassin.view.MainView
import com.david.mocassin.view.components.sidebar_drawers.LeftSideDrawer
import javafx.scene.control.Button
import javafx.scene.control.TextField
import tornadofx.*

class ChangeNameModal: Fragment("Change package name") {
    private val projectController: ProjectController by inject()
    private val leftSideDrawer: LeftSideDrawer by inject()
    private val mainView: MainView by inject()

    private var nameField = TextField(projectController.name)
    private var fieldSet: Fieldset by singleAssign()

    private val context = ValidationContext()

    private val validator = context.addValidator(nameField, nameField.textProperty()) {
        if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(it))
            error("The name is not alphanumeric (Should contains only letters (any case), numbers and underscores)")
        else if (it.isNullOrBlank())
            error("This field should not be blank")
        else
            null
    }

    override val root = form {
        fieldset("Enter a new name"){
            fieldSet = this
        }.add(nameField)

        val updateButton: Button = button("update")
        updateButton.action {
            if (validator.validate()) {
                projectController.name = nameField.text
                leftSideDrawer.userStructureTree.root.value = nameField.text
                leftSideDrawer.generatedStructureTree.root.value = nameField.text
                mainView.title = MainView.TITLE + " [${nameField.text}]"
                information("Project name has been updated", "Project name \"${nameField.text}\" has been updated successfully")
                close()
            }
        }

        fieldSet.children.add(updateButton)
    }
}