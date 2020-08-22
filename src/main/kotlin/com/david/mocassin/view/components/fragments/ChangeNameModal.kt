package com.david.mocassin.view.components.fragments

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import com.david.mocassin.view.MainView
import com.david.mocassin.view.components.sidebar_drawers.LeftSideDrawer
import javafx.scene.control.Button
import javafx.scene.control.TextField
import tornadofx.*

class ChangeNameModal: Fragment("") {
    private val projectController: ProjectController by inject()
    private val leftSideDrawer: LeftSideDrawer by inject()
    private val mainView: MainView by inject()

    private var nameField = TextField(projectController.name)
    private var fieldSet: Fieldset by singleAssign()

    private val context = ValidationContext()

    private val validator = context.addValidator(nameField, nameField.textProperty()) {
        if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(it))
            error(messages["v_not_alphanumeric_error"])
        else if (it.isNullOrBlank())
            error(messages["v_blank_field_error"])
        else
            null
    }

    init {
        title = messages["cnm_title"]
    }

    override val root = form {
        fieldset(messages["cnm_fieldset"]){
            fieldSet = this
        }.add(nameField)

        val updateButton: Button = button(messages["cnm_update_button"])
        updateButton.action {
            if (validator.validate()) {
                projectController.name = nameField.text
                leftSideDrawer.userStructureTree.root.value = nameField.text
                leftSideDrawer.generatedStructureTree.root.value = nameField.text
                mainView.title = mainView.defaultTitle + " [${nameField.text}]"
                information(
                    messages["cnm_info_header"],
                    "${messages["cnm_info_content_1"]} ${nameField.text}"
                )
                close()
            }
        }

        fieldSet.children.add(updateButton)
    }
}