package com.david.mocassin.view.components.wizards

import com.david.mocassin.model.c_components.*
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.ButtonBar
import javafx.scene.control.ComboBox
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

import tornadofx.*

//TODO verification champ attributes
//TODO verifier que le nom de l'union n'existe pas dans l'userModel
//TODO verif nom contient pas d'espace

class UnionWizardStep1 : View("Union name") {
    val unionModel: CunionModel by inject()

    override val complete = unionModel.valid(unionModel.name)

    override val root = form {
        fieldset(title) {
            field("Name") {
                textfield(unionModel.name).required()
            }
        }
    }
}

class UnionWizardStep2 : View("Union attributes") {
    val context = ValidationContext()

    val unionModel: CunionModel by inject()

    var variableNameField : TextField by singleAssign()
    var variableTypeField : ComboBox<String> by singleAssign()

    var attributesTable: TableView<Cvariable> by singleAssign()

    var selectedType = SimpleStringProperty()

    override val root = hbox {
        form {
            fieldset("Add fields inside") {
                field("name") {
                    textfield("") {
                        variableNameField = this
                        //TODO change this to a simple message in red

                        addEventHandler(KeyEvent.KEY_PRESSED) {
                            /*
                            if (it.code == KeyCode.SPACE) {
                                warning("Do not insert space")
                            } */
                            println("changing ${textProperty().value}")
                            //TODO Fix this function
                            textProperty().value.replace(regex = Regex("^[a-zA-Z0-9]+\$"), replacement = "B")
                            println("to ${textProperty().value}")
                        }
                    }
                }
                field("type") {
                    combobox<String>(selectedType){
                        variableTypeField = this
                        items = CtypeEnum.toObservableArrayList()
                        //TODO add types from userModel
                    }.selectionModel.selectFirst()
                }
                button("Add") {
                    action {
                        //TODO gerer les autres types union, enum, struct depuis model
                        val tmpVariable = Cvariable(variableNameField.textProperty().value, CtypeEnum.find(variableTypeField.value) as CuserType)
                        //println(tmpVariable.toJson())
                        unionModel.attributes.value.add(tmpVariable)

                        //form reset
                        variableNameField.textProperty().value = ""
                        variableTypeField.selectionModel.selectFirst()
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

        unionModel.item = Cunion("")

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


    }

    override fun onCancel() {
        confirm("Confirm cancel", "Do you really want to loose your progress?") {
            cancel()
        }
    }

    override fun onSave() {
        back()
        super.onSave()
    }
}
