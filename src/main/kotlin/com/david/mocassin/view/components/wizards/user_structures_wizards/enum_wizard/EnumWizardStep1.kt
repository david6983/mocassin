package com.david.mocassin.view.components.wizards.user_structures_wizards.enum_wizard

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.c_enum.CenumModel
import com.david.mocassin.utils.isNameReservedWords
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import tornadofx.*
import kotlin.error

class EnumWizardStep1 : View("Enum name") {
    private val projectController: ProjectController by inject()

    private val enumModel: CenumModel by inject()

    override val complete = enumModel.valid(enumModel.name)

    override val root = form {
        fieldset(title) {
            field("Name") {
                textfield(enumModel.name){
                    validator {
                        if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(
                                it
                            )
                        )
                            error("The name is not alphanumeric (Should contains only letters (any case), numbers and underscores)")
                        else if(!it.isNullOrBlank() && !projectController.isNameUnique(it)) {
                            error("The name already exist")
                        }
                        else if(!it.isNullOrBlank() && isNameReservedWords(
                                it
                            )
                        ) {
                            error("The name is reserved for the C language")
                        }
                        else null
                    }
                }.required()
            }
        }
    }
}