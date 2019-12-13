package com.david.mocassin.view.components.wizards.user_structures_wizards.enum_wizard

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.c_enum.CenumAttribute
import com.david.mocassin.model.c_components.c_enum.CenumAttributeModel
import com.david.mocassin.model.c_components.c_enum.CenumModel
import com.david.mocassin.utils.isNameReservedWords
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import com.david.mocassin.view.components.fragments.cell_fragments.enum_attributes_cell_fragments.CenumAttributeNameCellFragment
import com.david.mocassin.view.components.fragments.cell_fragments.enum_attributes_cell_fragments.CenumAttributeValueCellFragment
import javafx.scene.control.ContextMenu
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import tornadofx.*
import kotlin.error

class EnumWizardStep2 : View("Enumeration values") {
    private val projectController: ProjectController by inject()

    private val enumModel: CenumModel by inject()

    private val attributeModel = CenumAttributeModel()

    var attributeNameField : TextField by singleAssign()

    var attributesTable: TableView<CenumAttribute> by singleAssign()

    init {
        attributeModel.value.value = 0
    }

    override val root = hbox {
        form {
            fieldset("Add fields inside") {
                field("name") {
                    textfield(attributeModel.name) {
                        attributeNameField = this
                        validator {
                            if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(
                                    it
                                )
                            )
                                error("The name is not alphanumeric (Should contains only letters (any case), numbers and underscores)")
                            else if (it.isNullOrBlank())
                                error("This field should not be blank")
                            else if(!it.isNullOrBlank() && !projectController.isNameUniqueExcept(it, listOf(enumModel.name.value))) {
                                error("The name already exist in another structure in the project")
                            }
                            else if(!it.isNullOrBlank() && isNameReservedWords(
                                    it
                                )
                            ) {
                                error("The name is reserved for the C language")
                            }
                            else if(!it.isNullOrBlank() && !enumModel.item.isAttributeUniqueInEnum(it)) {
                                error("The name already exist in this enum")
                            }
                            else
                                null
                        }
                    }
                }
                field("value") {
                    hbox(spacing= 10) {
                        button("-").action {
                            attributeModel.value.value = attributeModel.value.value.toInt() - 1
                        }
                        label(attributeModel.value)
                        button("+").action {
                            attributeModel.value.value = attributeModel.value.value.toInt() + 1
                        }
                    }
                }
                button("Add") {
                    enableWhen(attributeModel.valid)
                    action {
                        val attr = CenumAttribute(
                            attributeModel.name.value,
                            attributeModel.value.value.toInt()
                        )
                        enumModel.attributes.value.add(attr)

                        //form reset
                        attributeNameField.textProperty().value = ""
                        attributeModel.value.value = enumModel.attributes.value.count()
                    }
                }
            }
        }
        tableview(enumModel.attributes) {
            attributesTable = this
            isEditable = true

            column("Name", CenumAttribute::name).cellFragment(
                CenumAttributeNameCellFragment::class)
            column("Value", CenumAttribute::value).cellFragment(
                CenumAttributeValueCellFragment::class)

            // remove attribute from model
            contextMenu = ContextMenu().apply{
                item("Delete").action {
                    selectedItem?.apply{
                        enumModel.attributes.value.removeIf { it.name == this.name }
                    }
                }
            }

            columnResizePolicy = SmartResize.POLICY
        }
    }

    override fun onSave() {
        attributeModel.name.value = ""
        super.onSave()
    }
}