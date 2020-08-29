package com.david.mocassin.view.components.wizards.user_structures_wizards.union_wizard

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.c_union.CunionModel
import com.david.mocassin.utils.isNameReservedWords
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import tornadofx.*

class UnionWizardStep1 : View() {
    private val projectController: ProjectController by inject()

    private val unionModel: CunionModel by inject()

    override val complete = unionModel.valid(unionModel.name)

    init {
        title = messages["usw_uw_step1_title"]
    }

    override val root = form {
        fieldset(title) {
            field(messages["name"]) {
                textfield(unionModel.name) {
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
        }
    }
}