package com.david.mocassin.view.components.wizards

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.Cenum
import com.david.mocassin.model.c_components.CenumAttribute
import com.david.mocassin.model.c_components.CenumAttributeModel
import com.david.mocassin.model.c_components.CenumModel
import com.david.mocassin.utils.isNameSyntaxFollowCstandard
import com.david.mocassin.view.components.wizards.editors.CenumAttributeNameEditor
import com.david.mocassin.view.components.wizards.editors.CenumAttributeValueEditor
import javafx.scene.control.ButtonBar
import javafx.scene.control.TableView
import javafx.scene.control.TextField

import tornadofx.*

//TODO add a controller to separate functions

class EnumWizardStep1 : View("Enum name") {
    private val projectController: ProjectController by inject()

    private val enumModel: CenumModel by inject()

    override val complete = enumModel.valid(enumModel.name)

    override val root = form {
        fieldset(title) {
            field("Name") {
                textfield(enumModel.name){
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
                            if (!it.isNullOrBlank() && !isNameSyntaxFollowCstandard(it))
                                error("The name is not alphanumeric (Should contains only letters (any case), numbers and underscores)")
                            else if (it.isNullOrBlank())
                                error("This field should not be blank")
                            else if(!it.isNullOrBlank() && !projectController.isNameUniqueExcept(it, listOf(enumModel.name.value))) {
                                error("The name already exist in another structure in the project")
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
            //TODO change editing method
            column("Name", CenumAttribute::name).cellFragment(CenumAttributeNameEditor::class)
            column("Value", CenumAttribute::value).cellFragment(CenumAttributeValueEditor::class)

            columnResizePolicy = SmartResize.POLICY
        }
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
            //this.root.
            cancel()
        }
    }

    override fun onSave() {
        if (canGoBack.value)
            back()
        super.onSave()
    }
}
