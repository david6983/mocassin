package com.david.mocassin.view.components.wizards.user_structures_wizards.struct_wizard

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.c_struct.CuserStructureModel
import com.david.mocassin.utils.isNameReservedWords
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import tornadofx.*

class StructWizardStep1 : View() {
    private val projectController: ProjectController by inject()

    private val structModel: CuserStructureModel by inject()

    override val complete = structModel.valid(structModel.name)

    init {
        title = messages["usw_sw_step1_title "]
    }

    override val root = form {
        fieldset(title) {
            field(messages["name"]) {
                textfield(structModel.name) {
                    validator {
                        if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(it))
                            error(messages["v_not_alphanumeric_error"])
                        else if (!it.isNullOrBlank() && !projectController.isNameUnique(it)) {
                            error(messages["v_already_exist_error"])
                        } else if (!it.isNullOrBlank() && isNameReservedWords(it)) {
                            error(messages["v_reserved_error"])
                        } else null
                    }
                }.required(message = messages["required"])
            }
            field(messages["usw_sw_step1_display_function"]) {
                checkbox(messages["usw_sw_step1_is_generated"])
            }
        }
    }
}