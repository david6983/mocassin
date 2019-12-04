package com.david.mocassin.view.components.wizards.editors

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.CenumAttribute
import com.david.mocassin.model.c_components.CenumAttributeModel
import com.david.mocassin.model.c_components.CenumModel
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import tornadofx.*

class CenumAttributeNameEditor : TableCellFragment<CenumAttribute, String>() {
    private val projectController: ProjectController by inject()
    private val enumModel: CenumModel by inject()
    // Bind our ItemModel to the rowItemProperty, which points to the current Item
    val model = CenumAttributeModel().bindToRowItem(this)

    override val root = stackpane {
        textfield(model.name) {
            removeWhen(editingProperty.not())
            validator {
                if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(it))
                    error("The name is not alphanumeric (Should contains only letters (any case), numbers and underscores)")
                else if (it.isNullOrBlank())
                    error("This field should not be blank")
                else if(!it.isNullOrBlank() && !projectController.isNameUniqueExcept(it, listOf(enumModel.name.value))) {
                    error("The name already exist in another structure in the project")
                }
                else
                    null
            }
            // Call cell.commitEdit() only if validation passes
            action {
                if (model.commit()) {
                    cell?.commitEdit(model.name.value)
                }
            }
        }
        // Label is visible when not in edit mode, and always shows committed value (itemProperty)
        label(itemProperty) {
            removeWhen(editingProperty)
        }
    }

    // Make sure we rollback our model to avoid showing the last failed edit
    override fun startEdit() {
        model.rollback()
    }

}