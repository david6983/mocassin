package com.david.mocassin.view.components.wizards

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.*
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.*

import tornadofx.*

//TODO verification champ attributes
//TODO verifier que le nom de l'union n'existe pas dans l'userModel

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
                        else null
                    }
                }.required()
            }
        }
    }
}

class UnionWizardStep2 : View("Union attributes") {
    private val attributeModel = CvariableModel()

    private val unionModel: CunionModel by inject()

    var variableNameField : TextField by singleAssign()
    var variableTypeField : ComboBox<String> by singleAssign()
    var variablePointerField : CheckBox by singleAssign()
    var variableComparableField : CheckBox by singleAssign()

    var attributesTable: TableView<Cvariable> by singleAssign()

    var selectedType = SimpleStringProperty()

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
                                error("This field should not be blank to add it")
                            else null
                        }
                        /*
                        addEventHandler(KeyEvent.KEY_PRESSED) {
                            /*
                            if (it.code == KeyCode.SPACE) {
                                warning("Do not insert space")
                            } */
                            println("changing ${textProperty().value}")
                            //TODO Fix this function
                            textProperty().value.replace(regex = Regex("^[a-zA-Z0-9]+\$"), replacement = "B")
                            println("to ${textProperty().value}")
                        }*/
                    }
                }
                field("Type") {
                    combobox<String>(selectedType){
                        variableTypeField = this
                        items = CtypeEnum.toObservableArrayList()
                        //TODO add types from userModel
                    }.selectionModel.selectFirst()
                }
                field("Pointer type") {
                    checkbox("is a pointer") {
                        variablePointerField = this

                    }
                }
                field("Comparison") {
                    checkbox("is comparable") {
                        variableComparableField = this
                    }
                }
                button("Add") {
                    enableWhen(attributeModel.valid)
                    action {
                        //TODO gerer les autres types union, enum, struct depuis model
                        val tmpVariable = Cvariable(
                            attributeModel.name.value,
                            CtypeEnum.find(variableTypeField.value) as CuserType,
                            attributeModel.isPointer.value,
                            attributeModel.isComparable.value
                        )
                        unionModel.attributes.value.add(tmpVariable)

                        //form reset
                        variableNameField.textProperty().value = ""
                        variableTypeField.selectionModel.selectFirst()
                        variablePointerField.isSelected = false
                        variableComparableField.isSelected = false
                    }
                }
            }
        }
        tableview(unionModel.attributes) {
            attributesTable = this
            isEditable = true
            //TODO gerer l'edition
            column("Name", Cvariable::name).makeEditable()
            column("Value", Cvariable::getTypeAsString).useComboBox(variableTypeField.items)
            column("Pointer", Cvariable::isPointer).makeEditable()
            column("Comparable", Cvariable::isComparable).makeEditable()

            columnResizePolicy = SmartResize.POLICY
        }
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
