package com.david.mocassin.view.components.wizards.user_structures_wizards.union_wizard

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.c_union.CunionModel
import com.david.mocassin.utils.isNameReservedWords
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import tornadofx.*
import kotlin.error

class UnionWizardStep1 : View("Union name") {
    private val projectController: ProjectController by inject()

    private val unionModel: CunionModel by inject()

    override val complete = unionModel.valid(unionModel.name)

    override val root = form {
        fieldset(title) {
            field("Name") {
                textfield(unionModel.name) {
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