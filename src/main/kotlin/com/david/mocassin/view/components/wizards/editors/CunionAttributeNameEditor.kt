package com.david.mocassin.view.components.wizards.editors

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.CunionModel
import com.david.mocassin.model.c_components.Cvariable
import com.david.mocassin.model.c_components.CvariableModel
import com.david.mocassin.utils.isNameReservedWords
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import tornadofx.*

class CunionAttributeNameEditor : TableCellFragment<Cvariable, String>() {
    private val projectController: ProjectController by inject()
    private val unionModel: CunionModel by inject()
    // Bind our ItemModel to the rowItemProperty, which points to the current Item
    val model = CvariableModel().bindToRowItem(this)

    override val root = stackpane {
        textfield(model.name) {
            removeWhen(editingProperty.not())
            validator {
                if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(it))
                    error("The name is not alphanumeric (Should contains only letters (any case), numbers and underscores)")
                else if (it.isNullOrBlank())
                    error("This field should not be blank")
                else if(!it.isNullOrBlank() && !projectController.isNameUniqueExcept(it, listOf(unionModel.name.value))) {
                    error("The name already exist in another structure in the project")
                }
                else if(!it.isNullOrBlank() && isNameReservedWords(it)) {
                    error("The name is reserved for the C language")
                }
                else if(!it.isNullOrBlank() && !unionModel.item.isAttributeUniqueInUnion(it)) {
                    error("The name already exist in this union")
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