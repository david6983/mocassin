package com.david.mocassin.view.components.fragments.cell_fragments.enum_attributes_cell_fragments

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.c_enum.CenumAttribute
import com.david.mocassin.model.c_components.c_enum.CenumAttributeModel
import com.david.mocassin.model.c_components.c_enum.CenumModel
import com.david.mocassin.utils.isNameReservedWords
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import tornadofx.*

class CenumAttributeNameCellFragment : TableCellFragment<CenumAttribute, String>() {
    private val projectController: ProjectController by inject()
    private val enumModel: CenumModel by inject()
    // Bind our ItemModel to the rowItemProperty, which points to the current Item
    val model = CenumAttributeModel().bindToRowItem(this)

    override val root = stackpane {
        textfield(model.name) {
            removeWhen(editingProperty.not())
            validator {
                if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(it))
                    error(messages["v_not_alphanumeric_error"])
                else if (it.isNullOrBlank())
                    error(messages["v_blank_field_error"])
                else if(!it.isNullOrBlank() && !projectController.isNameUniqueExcept(it, listOf(enumModel.name.value))) {
                    error(messages["v_already_exist_error"])
                }
                else if(!it.isNullOrBlank() && isNameReservedWords(it)) {
                    error(messages["v_reserved_error"])
                }
                else if(!it.isNullOrBlank() && !enumModel.item.isAttributeUniqueInEnum(it)) {
                    error(messages["v_exist_in_enum_error"])
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