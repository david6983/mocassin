package com.david.mocassin.view.components.fragments.cell_fragments.union_attributes_cell_fragments

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.CtypeEnum
import com.david.mocassin.model.c_components.CuserType
import com.david.mocassin.view.components.wizards.user_structures_wizards.union_wizard.UnionWizardStep2
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.ComboBox
import tornadofx.*

class CunionAttributeTypeCellFragment : View("") {
    private val unionWizardStep2: UnionWizardStep2 by inject()

    private val editorTypeCategory = SimpleBooleanProperty(true)
    private val editorSelectedSimpleType = SimpleStringProperty()
    private var editorVariableSimpleField : ComboBox<String> by singleAssign()
    private val editorSelectedProjectType = SimpleStringProperty("enum")
    private var editorVariableFromProjectField : ComboBox<String> by singleAssign()

    init {
        title = messages["edit_selected_type"]
    }

    override val root = form {
        fieldset(messages["choose_new_type"]) {
            field(messages["type"]) {
                vbox {
                    togglegroup {
                        radiobutton(messages["tcf_simple"], this, value = true)
                        radiobutton(messages["tcf_from_project"], this, value = false) {
                            removeWhen(unionWizardStep2.isProjectEmpty)
                        }
                        bind(editorTypeCategory)
                    }
                }
            }
            field(messages["choose_type"]) {
                vbox {
                    removeWhen(editorTypeCategory.not())
                    combobox<String>(editorSelectedSimpleType) {
                        editorVariableSimpleField = this
                        items = CtypeEnum.toObservableArrayList()
                    }.selectionModel.selectFirst()
                }
                vbox {
                    removeWhen(editorTypeCategory)
                    togglegroup {
                        radiobutton("enum", this, value = "enum") {
                            removeWhen(unionWizardStep2.hasNotEnumInProject)
                        }.action {
                            editorVariableFromProjectField.items =
                                unionWizardStep2.projectController.getListOfAllNamesUsed(
                                    ProjectController.ENUM
                                ).asObservable()
                            editorVariableFromProjectField.selectionModel.selectFirst()
                        }
                        radiobutton("union", this, value = "union") {
                            removeWhen(unionWizardStep2.hasNotUnionInProject)
                        }.action {
                            editorVariableFromProjectField.items =
                                unionWizardStep2.projectController.getListOfAllNamesUsed(
                                    ProjectController.UNION
                                ).asObservable()
                            editorVariableFromProjectField.selectionModel.selectFirst()
                        }
                        radiobutton("struct", this, value = "struct") {
                            removeWhen(unionWizardStep2.hasNotStructInProject)
                        }.action {
                            editorVariableFromProjectField.items =
                                unionWizardStep2.projectController.getListOfAllNamesUsed(
                                    ProjectController.STRUCT
                                ).asObservable()
                            editorVariableFromProjectField.selectionModel.selectFirst()
                        }
                        bind(editorSelectedProjectType)
                    }
                }
            }
            field(messages["tcf_choose_from_list"]) {
                removeWhen(editorTypeCategory)
                vbox {
                    combobox<String>() {
                        editorVariableFromProjectField = this
                        items = unionWizardStep2.projectController.getListOfAllNamesUsed().asObservable()
                    }.selectionModel.selectFirst()
                }
            }
        }
        button(messages["tcf_validate_changes"]).action {
            if (editorTypeCategory.value) {
                unionWizardStep2.selectedType?.type = CtypeEnum.find(editorVariableSimpleField.value) as CuserType
            }
            else {
                when(editorSelectedProjectType.value) {
                    "enum" -> {
                        unionWizardStep2.selectedType?.type = unionWizardStep2.projectController.userModel.findEnumByName(editorVariableFromProjectField.value)
                    }
                    "union" -> {
                        unionWizardStep2.selectedType?.type = unionWizardStep2.projectController.userModel.findUnionByName(editorVariableFromProjectField.value)
                    }
                    "struct" -> {
                        unionWizardStep2.selectedType?.type = unionWizardStep2.projectController.userModel.findStructByName(editorVariableFromProjectField.value)
                    }
                }
            }
            unionWizardStep2.attributesTable.refresh()
            close()
        }
    }

    init {
        //println(unionWizardStep2.selectedType?.getTypeAsString())
        with(root) {
            prefHeight = 200.0
        }
    }
}