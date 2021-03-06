package com.david.mocassin.view.components.wizards.generated_structures_wizards.slist_wizard

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.DataStructure
import com.david.mocassin.model.DataStructureEnum
import com.david.mocassin.model.DataStructureModel

import javafx.scene.control.ButtonBar
import tornadofx.*

class SlistWizard : Wizard() {
    private val projectController: ProjectController by inject()
    val slistModel: DataStructureModel by inject()

    override val canGoNext = currentPageComplete
    override val canFinish = allPagesComplete

    init {
        title = messages["gsw_slw_title"]
        heading = messages["gsw_slw_heading"]

        backButtonTextProperty.value = "< " + messages["back"]
        nextButtonTextProperty.value = messages["next"] + " >"
        cancelButtonTextProperty.value = messages["cancel"]
        finishButtonTextProperty.value = messages["finish"]
        stepsTextProperty.value = messages["steps"]

        graphic = resources.imageview("/icons/slist32.png")
        add(SlistWizardStep1::class)
        add(SlistWizardStep2::class)

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

        slistModel.item = DataStructure(projectController.userModel, DataStructureEnum.SLIST)
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
