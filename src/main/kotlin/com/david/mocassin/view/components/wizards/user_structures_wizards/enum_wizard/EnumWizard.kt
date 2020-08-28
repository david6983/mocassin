package com.david.mocassin.view.components.wizards.user_structures_wizards.enum_wizard

import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.model.c_components.c_enum.CenumModel
import javafx.scene.control.ButtonBar

import tornadofx.*

class EnumWizard : Wizard() {
    val enumModel: CenumModel by inject()

    override val canGoNext = currentPageComplete
    override val canFinish = allPagesComplete

    init {
        title = messages["usw_ew_title"]
        heading = messages["usw_ew_heading"]

        backButtonTextProperty.value = "< " + messages["back"]
        nextButtonTextProperty.value = messages["next"] + " >"
        cancelButtonTextProperty.value = messages["cancel"]
        finishButtonTextProperty.value = messages["finish"]
        stepsTextProperty.value = messages["steps"]

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
