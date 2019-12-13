package com.david.mocassin.view.components.wizards.user_structures_wizards.struct_wizard

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.c_struct.CuserStructureModel
import com.david.mocassin.utils.isNameReservedWords
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import tornadofx.*

class StructWizardStep1 : View("Struct basic information") {
    private val projectController: ProjectController by inject()

    private val structModel: CuserStructureModel by inject()

    override val complete = structModel.valid(structModel.name)

    override val root = form {
        fieldset(title) {
            field("Name") {
                textfield(structModel.name) {
                    validator {
                        if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(it))
                            error("The name is not alphanumeric (Should contains only letters (any case), numbers and underscores)")
                        else if (!it.isNullOrBlank() && !projectController.isNameUnique(it)) {
                            error("The name already exist")
                        } else if (!it.isNullOrBlank() && isNameReservedWords(it)) {
                            error("The name is reserved for the C language")
                        } else null
                    }
                }.required()
            }
            field("Display function") {
                checkbox("is generated")
            }
        }
    }
}