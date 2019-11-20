package com.david.mocassin.view.components.wizards

import com.david.mocassin.model.c_components.Cenum
import com.david.mocassin.model.c_components.CenumAttribute
import com.david.mocassin.model.c_components.CenumModel
import javafx.scene.Parent
import javafx.scene.control.ButtonBar
import javafx.scene.control.TableView
import javafx.scene.control.TextField

import tornadofx.*

//TODO verification champ attributes
//TODO verifier que le nom de l'enum n'existe pas dans l'userModel

class EnumWizardStep1 : View("Enum name") {
    val enumModel: CenumModel by inject()

    override val complete = enumModel.valid(enumModel.name)

    override val root = form {
        fieldset(title) {
            field("Name") {
                textfield(enumModel.name).required()
            }
        }
    }


}

class EnumWizardStep2 : View("Enumeration values") {
    val context = ValidationContext()

    val enumModel: CenumModel by inject()

    var attributeNameField : TextField by singleAssign()
    var attributeValueField : TextField by singleAssign()

    var attributesTable: TableView<CenumAttribute> by singleAssign()
    /*
    val nameValidator = context.addValidator(attributeNameField, attributeNameField.textProperty()) {
        if(enumModel.attributes.value.indexOfFirst { it.name == attributeNameField.textProperty().toString() } == -1) {
            error("This attribute already exist !")
        } else {
            null
        }
    }*/

    override val root = hbox {
        form {
            fieldset("Add fields inside") {
                field("name") {
                    textfield("") {
                        attributeNameField = this
                    }
                }
                field("value") {
                    textfield("0") {
                        attributeValueField = this
                    }
                }
                button("Add") {
                    action {
                        val attr = CenumAttribute(
                            attributeNameField.textProperty().value,
                            attributeValueField.textProperty().value.toInt()
                        )
                        enumModel.attributes.value.add(attr)

                        //form reset
                        attributeNameField.textProperty().value = ""
                        attributeValueField.textProperty().value = enumModel.attributes.value.count().toString()

                    }
                }
            }
        }
        tableview(enumModel.attributes) {
            attributesTable = this
            isEditable = true
            column("Name", CenumAttribute::name).makeEditable()
            column("Value", CenumAttribute::value).makeEditable()

            columnResizePolicy = SmartResize.POLICY
        }
    }

    override fun onSave() {
        attributeValueField.textProperty().value = "0"
        super.onSave()
    }
}

class EnumWizard : Wizard("Create a Enum", "Provide Enum information") {
    val enumModel: CenumModel by inject()

    override val canGoNext = currentPageComplete
    override val canFinish = allPagesComplete

    init {
        graphic = resources.imageview("/icons/enum32.png")
        add(EnumWizardStep1::class)
        add(EnumWizardStep2::class)

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

        enumModel.item = Cenum("")
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
