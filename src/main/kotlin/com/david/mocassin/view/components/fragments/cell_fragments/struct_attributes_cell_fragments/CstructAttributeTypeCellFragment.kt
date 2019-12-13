package com.david.mocassin.view.components.fragments.cell_fragments.struct_attributes_cell_fragments


import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.CtypeEnum
import com.david.mocassin.model.c_components.CuserType
import com.david.mocassin.view.components.wizards.user_structures_wizards.struct_wizard.StructWizardStep2
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.ComboBox
import tornadofx.*

class CstructAttributeTypeCellFragment : View("Edit selected type") {
    private val structWizardStep2: StructWizardStep2 by inject()

    private val editorTypeCategory = SimpleBooleanProperty(true)
    private val editorSelectedSimpleType = SimpleStringProperty()
    private var editorVariableSimpleField : ComboBox<String> by singleAssign()
    private val editorSelectedProjectType = SimpleStringProperty("enum")
    private var editorVariableFromProjectField : ComboBox<String> by singleAssign()

    override val root = form {
        fieldset("Choose a new type") {
            field("Type") {
                vbox {
                    togglegroup {
                        radiobutton("simple", this, value = true)
                        radiobutton("from project", this, value = false) {
                            removeWhen(structWizardStep2.isProjectEmpty)
                        }
                        bind(editorTypeCategory)
                    }
                }
            }
            field("Choose a type") {
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
                            removeWhen(structWizardStep2.hasNotEnumInProject)
                        }.action {
                            editorVariableFromProjectField.items =
                                structWizardStep2.projectController.getListOfAllNamesUsed(
                                    ProjectController.ENUM
                                ).asObservable()
                            editorVariableFromProjectField.selectionModel.selectFirst()
                        }
                        radiobutton("union", this, value = "union") {
                            removeWhen(structWizardStep2.hasNotUnionInProject)
                        }.action {
                            editorVariableFromProjectField.items =
                                structWizardStep2.projectController.getListOfAllNamesUsed(
                                    ProjectController.UNION
                                ).asObservable()
                            editorVariableFromProjectField.selectionModel.selectFirst()
                        }
                        radiobutton("struct", this, value = "struct") {
                            removeWhen(structWizardStep2.hasNotStructInProject)
                        }.action {
                            editorVariableFromProjectField.items =
                                structWizardStep2.projectController.getListOfAllNamesUsed(
                                    ProjectController.STRUCT
                                ).asObservable()
                            editorVariableFromProjectField.selectionModel.selectFirst()
                        }
                        bind(editorSelectedProjectType)
                    }
                }
            }
            field("Choose from the list") {
                removeWhen(editorTypeCategory)
                vbox {
                    combobox<String>() {
                        editorVariableFromProjectField = this
                        items = structWizardStep2.projectController.getListOfAllNamesUsed().asObservable()
                    }.selectionModel.selectFirst()
                }
            }
        }
        button("Validate Changes").action {
            if (editorTypeCategory.value) {
                structWizardStep2.selectedType?.type = CtypeEnum.find(editorVariableSimpleField.value) as CuserType
            }
            else {
                when(editorSelectedProjectType.value) {
                    "enum" -> {
                        structWizardStep2.selectedType?.type = structWizardStep2.projectController.userModel.findEnumByName(editorVariableFromProjectField.value)
                    }
                    "union" -> {
                        structWizardStep2.selectedType?.type = structWizardStep2.projectController.userModel.findUnionByName(editorVariableFromProjectField.value)
                    }
                    "struct" -> {
                        structWizardStep2.selectedType?.type = structWizardStep2.projectController.userModel.findStructByName(editorVariableFromProjectField.value)
                    }
                }
            }
            structWizardStep2.attributesTable.refresh()
            close()
        }
    }

    init {
        //println(structWizardStep2.selectedType?.getTypeAsString())
        with(root) {
            prefHeight = 200.0
        }
    }
}