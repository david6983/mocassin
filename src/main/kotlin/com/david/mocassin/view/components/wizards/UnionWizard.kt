package com.david.mocassin.view.components.wizards

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.*
import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.utils.isNameReservedWords
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import com.david.mocassin.view.components.wizards.editors.CunionAttributeNameEditor
import com.david.mocassin.view.components.wizards.editors.CunionAttributeTypeEditor
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import javafx.scene.control.*

import tornadofx.*

class UnionWizardStep1 : View("Union name") {
    private val projectController: ProjectController by inject()

    private val unionModel: CunionModel by inject()

    override val complete = unionModel.valid(unionModel.name)

    override val root = form {
        fieldset(title) {
            field("Name") {
                textfield(unionModel.name) {
                    validator {
                        if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(it))
                            error("The name is not alphanumeric (Should contains only letters (any case), numbers and underscores)")
                        else if(!it.isNullOrBlank() && !projectController.isNameUnique(it)) {
                            error("The name already exist")
                        }
                        else if(!it.isNullOrBlank() && isNameReservedWords(it)) {
                            error("The name is reserved for the C language")
                        }
                        else null
                    }
                }.required()
            }
        }
    }
}

class UnionWizardStep2 : View("Union attributes") {
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
        attributeModel.isPointer.value = false
        attributeModel.isComparable.value = false
    }

    override val root = hbox {
        form {
            fieldset("Add fields inside") {
                field("Name") {
                    textfield(attributeModel.name) {
                        variableNameField = this
                        validator {
                            if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(it))
                                error("The name is not alphanumeric (Should contains only letters (any case), numbers and underscores)")
                            else if (it.isNullOrBlank())
                                error("This field should not be blank")
                            else if(!it.isNullOrBlank() && !projectController.isNameUniqueExcept(it, listOf(unionModel.name.value))) {
                                error("The name already exist in another structure in the project")
                            }
                            else if(!it.isNullOrBlank() && isNameReservedWords(it)) {
                                error("The name is reserved for the C language")
                            }
                            else if(!it.isNullOrBlank() && !unionModel.item.isAttributeUniqueInUnion(it)) {
                                error("The name already exist in this union")
                            }
                            else
                                null
                        }
                    }
                }
                field("Type") {
                    vbox {
                        togglegroup {
                            radiobutton("simple", this, value= true)
                            radiobutton("from project", this, value= false) {
                                removeWhen(isProjectEmpty)
                            }
                            bind(typeCategory)
                        }
                    }
                }
                field("Choose a type") {
                    vbox {
                        removeWhen(typeCategory.not())
                        combobox<String>(selectedSimpleType) {
                            variableSimpleField = this
                            items = CtypeEnum.toObservableArrayList()
                        }.selectionModel.selectFirst()
                    }
                    vbox {
                        removeWhen(typeCategory)
                        togglegroup {
                            radiobutton("enum", this, value = "enum"){
                                removeWhen(hasNotEnumInProject)
                            }.action {
                                variableFromProjectField.items = projectController.getListOfAllNamesUsed(ProjectController.ENUM).asObservable()
                                variableFromProjectField.selectionModel.selectFirst()
                            }
                            radiobutton("union", this, value = "union"){
                                removeWhen(hasNotUnionInProject)
                            }.action {
                                variableFromProjectField.items = projectController.getListOfAllNamesUsed(ProjectController.UNION).asObservable()
                                variableFromProjectField.selectionModel.selectFirst()
                            }
                            radiobutton("struct", this, value = "struct"){
                                removeWhen(hasNotStructInProject)
                            }.action {
                                variableFromProjectField.items = projectController.getListOfAllNamesUsed(ProjectController.STRUCT).asObservable()
                                variableFromProjectField.selectionModel.selectFirst()
                            }
                            bind(selectedProjectType)
                        }
                    }
                }
                field("Choose from the list") {
                    removeWhen(typeCategory)
                    vbox {
                        combobox<String>() {
                            variableFromProjectField = this
                            items = projectController.getListOfAllNamesUsed().asObservable()
                        }.selectionModel.selectFirst()
                    }
                }
                field("Pointer type") {
                    checkbox("is a pointer", attributeModel.isPointer) {
                        variablePointerField = this
                    }
                }
                field("Comparison") {
                    checkbox("is comparable", attributeModel.isComparable) {
                        variableComparableField = this
                    }
                }
                button("Add") {
                    enableWhen(attributeModel.valid)
                    action {
                        //TODO gerer les autres types union, enum, struct depuis model
                        val tmpVariable = Cvariable(
                            attributeModel.name.value,
                            CtypeEnum.find(variableSimpleField.value) as CuserType,
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

            column("Name", Cvariable::name).cellFragment(CunionAttributeNameEditor::class)
            column("Type", Cvariable::getTypeAsString)
            column("Pointer", Cvariable::isPointer).cellFormat {
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
            column("Comparable", Cvariable::isComparable).makeEditable()

            // remove attribute from model
            contextMenu = ContextMenu().apply{
                item("Delete").action {
                    selectedItem?.apply{
                        unionModel.attributes.value.removeIf { it.name == this.name }
                    }
                }
            }

            onDoubleClick {
                if (selectedCell?.column == 1) {
                    selectedType = selectedCell?.row?.let { it -> selectedCell?.tableView?.items?.get(it) }
                    find<CunionAttributeTypeEditor>().openModal()
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

class UnionWizard : Wizard("Create a Union", "Provide Union information") {
    val unionModel: CunionModel by inject()

    override val canGoNext = currentPageComplete
    override val canFinish = allPagesComplete

    init {
        graphic = resources.imageview("/icons/union32.png")
        add(UnionWizardStep1::class)
        add(UnionWizardStep2::class)

        // custom style for wizard buttonbar
        super.root.bottom = buttonbar {
            addClass(WizardStyles.buttons)
            button(type = ButtonBar.ButtonData.BACK_PREVIOUS) {
                addClass("btn-primary","btn")
                textProperty().bind(backButtonTextProperty)
                runLater {
                    enableWhen(canGoBack)
                }
                action { back() }
            }
            button(type = ButtonBar.ButtonData.NEXT_FORWARD) {
                addClass("btn-primary","btn")
                textProperty().bind(nextButtonTextProperty)
                runLater {
                    enableWhen(canGoNext.and(hasNext).and(currentPageComplete))
                }
                action { next() }
            }
            button(type = ButtonBar.ButtonData.CANCEL_CLOSE) {
                addClass("btn-primary","btn")
                textProperty().bind(cancelButtonTextProperty)
                action { onCancel() }
            }
            button(type = ButtonBar.ButtonData.FINISH) {
                addClass("btn-primary","btn")
                textProperty().bind(finishButtonTextProperty)
                runLater {
                    enableWhen(canFinish)
                }
                action {
                    currentPage.onSave()
                    if (currentPage.isComplete) {
                        onSave()
                        if (isComplete) close()
                    }
                }
            }
        }

        unionModel.item = Cunion("")
    }

    override fun onCancel() {
        confirm("Confirm cancel", "Do you really want to loose your progress?") {
            cancel()
        }
    }

    override fun onSave() {
        if (canGoBack.value)
            back()
        super.onSave()
    }
}
