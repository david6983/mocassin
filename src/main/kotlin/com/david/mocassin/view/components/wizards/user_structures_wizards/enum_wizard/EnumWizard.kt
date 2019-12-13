package com.david.mocassin.view.components.wizards.user_structures_wizards.enum_wizard

import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.model.c_components.c_enum.CenumModel
import javafx.scene.control.ButtonBar

import tornadofx.*

//TODO add a controller to separate functions

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
        if (canGoBack.value)
            back()
        super.onSave()
    }
}
