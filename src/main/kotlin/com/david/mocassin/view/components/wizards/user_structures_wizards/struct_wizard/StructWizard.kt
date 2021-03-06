package com.david.mocassin.view.components.wizards.user_structures_wizards.struct_wizard

import com.david.mocassin.model.c_components.c_struct.CuserStructure
import com.david.mocassin.model.c_components.c_struct.CuserStructureModel
import javafx.scene.control.ButtonBar
import tornadofx.*

class StructWizard : Wizard() {
    val structModel: CuserStructureModel by inject()

    override val canGoNext = currentPageComplete
    override val canFinish = allPagesComplete

    init {
        title = messages["usw_sw_title"]
        heading = messages["usw_sw_heading"]

        backButtonTextProperty.value = "< " + messages["back"]
        nextButtonTextProperty.value = messages["next"] + " >"
        cancelButtonTextProperty.value = messages["cancel"]
        finishButtonTextProperty.value = messages["finish"]
        stepsTextProperty.value = messages["steps"]

        graphic = resources.imageview("/icons/struct32.png")
        add(StructWizardStep1::class)
        add(StructWizardStep2::class)

        // custom style for wizard buttonbar
        super.root.bottom = buttonbar {
            addClass(WizardStyles.buttons)
            button(type = ButtonBar.ButtonData.BACK_PREVIOUS) {
                addClass("btn-primary", "btn")
                textProperty().bind(backButtonTextProperty)
                runLater {
                    enableWhen(canGoBack)
                }
                action { back() }
            }
            button(type = ButtonBar.ButtonData.NEXT_FORWARD) {
                addClass("btn-primary", "btn")
                textProperty().bind(nextButtonTextProperty)
                runLater {
                    enableWhen(canGoNext.and(hasNext).and(currentPageComplete))
                }
                action { next() }
            }
            button(type = ButtonBar.ButtonData.CANCEL_CLOSE) {
                addClass("btn-primary", "btn")
                textProperty().bind(cancelButtonTextProperty)
                action { onCancel() }
            }
            button(type = ButtonBar.ButtonData.FINISH) {
                addClass("btn-primary", "btn")
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

        structModel.item = CuserStructure("")
    }

    override fun onCancel() {
        confirm(messages["confirm_cancel_header"], messages["confirm_cancel_content"]) {
            cancel()
        }
    }

    override fun onSave() {
        if (canGoBack.value)
            back()
        super.onSave()
    }
}
