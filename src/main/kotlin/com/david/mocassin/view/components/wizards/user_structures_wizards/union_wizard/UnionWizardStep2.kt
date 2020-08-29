package com.david.mocassin.view.components.wizards.user_structures_wizards.union_wizard

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.CtypeEnum
import com.david.mocassin.model.c_components.CuserType
import com.david.mocassin.model.c_components.c_union.CunionModel
import com.david.mocassin.model.c_components.c_variable.Cvariable
import com.david.mocassin.model.c_components.c_variable.CvariableModel
import com.david.mocassin.utils.isNameReservedWords
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import com.david.mocassin.view.components.fragments.cell_fragments.union_attributes_cell_fragments.CunionAttributeNameCellFragment
import com.david.mocassin.view.components.fragments.cell_fragments.union_attributes_cell_fragments.CunionAttributeTypeCellFragment
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.*
import tornadofx.*
import kotlin.error

class UnionWizardStep2 : View() {
    val projectController: ProjectController by inject()

    private val attributeModel = CvariableModel()

    val unionModel: CunionModel by inject()

    var variableNameField : TextField by singleAssign()
    private var variableSimpleField : ComboBox<String> by singleAssign()
    private var variableFromProjectField : ComboBox<String> by singleAssign()
    var variablePointerField : CheckBox by singleAssign()
    var variableComparableField : CheckBox by singleAssign()

    var attributesTable: TableView<Cvariable> by singleAssign()

    var selectedSimpleType = SimpleStringProperty()

    var typeCategory = SimpleBooleanProperty(true)
    var isProjectEmpty = SimpleBooleanProperty(false)

    var hasNotEnumInProject = SimpleBooleanProperty(false)
    var hasNotUnionInProject = SimpleBooleanProperty(false)
    var hasNotStructInProject = SimpleBooleanProperty(false)

    var selectedProjectType = SimpleStringProperty("enum")

    var selectedType: Cvariable? = null

    init {
        title = messages["usw_uw_step2_title"]
        attributeModel.isPointer.value = false
        attributeModel.isComparable.value = false
    }

    override val root = hbox {
        form {
            fieldset(messages["add_fields_inside"]) {
                field(messages["name"]) {
                    textfield(attributeModel.name) {
                        variableNameField = this
                        validator {
                            if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(it))
                                error(messages["v_not_alphanumeric_error"])
                            else if (it.isNullOrBlank())
                                error(messages["v_blank_field_error"])
                            else if(!it.isNullOrBlank() && !projectController.isNameUniqueExcept(it, listOf(unionModel.name.value))) {
                                error(messages["v_already_exist_error"])
                            }
                            else if(!it.isNullOrBlank() && isNameReservedWords(it)) {
                                error(messages["v_reserved_error"])
                            }
                            else if(!it.isNullOrBlank() && !unionModel.item.isAttributeUniqueInUnion(it)) {
                                error(messages["v_exist_in_union_error"])
                            }
                            else
                                null
                        }
                    }
                }
                field(messages["type"]) {
                    vbox {
                        togglegroup {
                            radiobutton(messages["tcf_simple"], this, value= true)
                            radiobutton(messages["tcf_from_project"], this, value= false) {
                                removeWhen(isProjectEmpty)
                            }
                            bind(typeCategory)
                        }
                    }
                }
                field(messages["choose_type"]) {
                    vbox {
                        removeWhen(typeCategory.not())
                        combobox<String>(selectedSimpleType) {
                            variableSimpleField = this
                            items =
                                CtypeEnum.toObservableArrayList()
                        }.selectionModel.selectFirst()
                    }
                    vbox {
                        removeWhen(typeCategory)
                        togglegroup {
                            radiobutton("enum", this, value = "enum"){
                                removeWhen(hasNotEnumInProject)
                            }.action {
                                variableFromProjectField.items = projectController.getListOfAllNamesUsed(
                                    ProjectController.ENUM
                                ).asObservable()
                                variableFromProjectField.selectionModel.selectFirst()
                            }
                            radiobutton("union", this, value = "union"){
                                removeWhen(hasNotUnionInProject)
                            }.action {
                                variableFromProjectField.items = projectController.getListOfAllNamesUsed(
                                    ProjectController.UNION
                                ).asObservable()
                                variableFromProjectField.selectionModel.selectFirst()
                            }
                            radiobutton("struct", this, value = "struct"){
                                removeWhen(hasNotStructInProject)
                            }.action {
                                variableFromProjectField.items = projectController.getListOfAllNamesUsed(
                                    ProjectController.STRUCT
                                ).asObservable()
                                variableFromProjectField.selectionModel.selectFirst()
                            }
                            bind(selectedProjectType)
                        }
                    }
                }
                field(messages["choose_from_list"]) {
                    removeWhen(typeCategory)
                    vbox {
                        combobox<String>() {
                            variableFromProjectField = this
                            items = projectController.getListOfAllNamesUsed().asObservable()
                        }.selectionModel.selectFirst()
                    }
                }
                field(messages["pointer_type"]) {
                    checkbox(messages["is_pointer"], attributeModel.isPointer) {
                        variablePointerField = this
                    }
                }
                field(messages["comparison"]) {
                    checkbox(messages["is_comparable"], attributeModel.isComparable) {
                        variableComparableField = this
                    }
                }
                button(messages["add_button"]) {
                    enableWhen(attributeModel.valid)
                    action {
                        val tmpVariable = Cvariable(
                            attributeModel.name.value,
                            CtypeEnum.find(
                                variableSimpleField.value
                            ) as CuserType,
                            attributeModel.isPointer.value,
                            attributeModel.isComparable.value
                        )
                        if (!typeCategory.value) {
                            when(selectedProjectType.value) {
                                "enum" -> {
                                    tmpVariable.type = projectController.userModel.findEnumByName(variableFromProjectField.value)
                                }
                                "union" -> {
                                    tmpVariable.type = projectController.userModel.findUnionByName(variableFromProjectField.value)
                                }
                                "struct" -> {
                                    tmpVariable.type = projectController.userModel.findStructByName(variableFromProjectField.value)
                                }
                            }
                        }
                        unionModel.attributes.value.add(tmpVariable)

                        //form reset
                        variableNameField.textProperty().value = ""
                        variableSimpleField.selectionModel.selectFirst()
                        variableFromProjectField.selectionModel.selectFirst()
                        typeCategory.value = true
                        selectedProjectType.value = "enum"
                        variablePointerField.isSelected = false
                        variableComparableField.isSelected = false
                    }
                }
            }
        }
        tableview(unionModel.attributes) {
            attributesTable = this
            isEditable = true

            column(messages["name"], Cvariable::name).cellFragment(
                CunionAttributeNameCellFragment::class)
            column(messages["type"], Cvariable::getTypeAsString)
            column(messages["pointer"], Cvariable::isPointer).cellFormat {
                text = this.rowItem.isPointer.toString()

                onDoubleClick {
                    if (text == "true") {
                        text = "false"
                        this.rowItem.isPointer = false
                    } else {
                        text = "true"
                        this.rowItem.isPointer = true
                    }
                    attributesTable.refresh()
                }
            }
            column(messages["comparable"], Cvariable::isComparable).makeEditable()

            // remove attribute from model
            contextMenu = ContextMenu().apply{
                item(messages["delete"]).action {
                    selectedItem?.apply{
                        unionModel.attributes.value.removeIf { it.name == this.name }
                    }
                }
            }

            onDoubleClick {
                if (selectedCell?.column == 1) {
                    selectedType = selectedCell?.row?.let { it -> selectedCell?.tableView?.items?.get(it) }
                    find<CunionAttributeTypeCellFragment>().openModal()
                }
            }

            columnResizePolicy = SmartResize.POLICY
        }
    }

    override fun onDock() {
        // init the fields
        variableNameField.text = ""
        variablePointerField.isSelected = false
        variableComparableField.isSelected = false

        // fill type combobox with enums
        variableFromProjectField.items = projectController.getListOfAllNamesUsed(ProjectController.ENUM).asObservable()
        variableFromProjectField.selectionModel.selectFirst()
        isProjectEmpty.value = projectController.userModel.isEmpty()
        //TODO to simplify and add unit test
        hasNotEnumInProject.value = projectController.userModel.userEnumList.isEmpty()
        hasNotUnionInProject.value = projectController.userModel.userUnionList.isEmpty()
        hasNotStructInProject.value = projectController.userModel.userStructureList.isEmpty()

        super.onDock()
    }
}