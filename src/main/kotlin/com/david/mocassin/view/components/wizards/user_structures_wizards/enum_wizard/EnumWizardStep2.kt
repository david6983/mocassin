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
        title = messages["usw_ew_step2_title"]
    }

    override val root = hbox {
        form {
            fieldset(messages["add_fields_inside"]) {
                field(messages["name"]) {
                    textfield(attributeModel.name) {
                        attributeNameField = this
                        validator {
                            if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(
                                    it
                                )
                            )
                                error(messages["v_not_alphanumeric_error"])
                            else if (it.isNullOrBlank())
                                error(messages["v_blank_field_error"])
                            else if(!it.isNullOrBlank() && !projectController.isNameUniqueExcept(it, listOf(enumModel.name.value))) {
                                error(messages["v_already_exist_error"])
                            }
                            else if(!it.isNullOrBlank() && isNameReservedWords(
                                    it
                                )
                            ) {
                                error(messages["v_reserved_error"])
                            }
                            else if(!it.isNullOrBlank() && !enumModel.item.isAttributeUniqueInEnum(it)) {
                                error(messages["v_exist_in_enum_error"])
                            }
                            else
                                null
                        }
                    }
                }
                field(messages["value"]) {
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
                button(messages["add_button"]) {
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

            column(messages["name"], CenumAttribute::name).cellFragment(
                CenumAttributeNameCellFragment::class)
            column(messages["value"], CenumAttribute::value).cellFragment(
                CenumAttributeValueCellFragment::class)

            // remove attribute from model
            contextMenu = ContextMenu().apply{
                item(messages["delete"]).action {
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

    override fun onDock() {
        attributeModel.value.value = 0
        super.onDock()
    }
}