package com.david.mocassin.view.components.wizards

import com.david.mocassin.model.c_components.CtypeEnum
import com.david.mocassin.model.c_components.Cunion
import com.david.mocassin.model.c_components.CunionModel
import com.david.mocassin.model.c_components.Cvariable
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.ButtonBar
import javafx.scene.control.ComboBox
import javafx.scene.control.TableView
import javafx.scene.control.TextField

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
                    }
                }
                field("type") {
                    combobox<String>(selectedType){
                        variableTypeField = this

                        for(type in CtypeEnum.values()) {
                            items.add(type.cType)
                        }
                        //TODO add types from userModel
                    }.selectionModel.selectFirst()
                }
                button("Add") {
                    action {
                        /*
                        val attr = CunionAttribute(
                            variableNameField.textProperty().value,
                            variableTypeField.textProperty().value.toInt()
                        )
                        unionModel.attributes.value.add(attr)

                        //form reset
                        variableNameField.textProperty().value = ""
                        variableTypeField.textProperty().value = unionModel.attributes.value.count().toString()
                        */
                    }
                }
            }
        }
        tableview(unionModel.attributes) {
            attributesTable = this
            isEditable = true
            column("Name", Cvariable::name).makeEditable()
            //column("Value", Cvariable::type).makeEditable()

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

        unionModel.item = Cunion("fuck")

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
