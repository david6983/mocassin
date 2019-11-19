package com.david.mocassin.view.components.wizards

import tornadofx.*

class EnumWizardStep1 : View("Enum name") {
    override val root = form {

    }
}

class EnumWizard : Wizard("Create a Enum", "Provide Enum information") {

    init {
        //TODO add the icon
        //graphic = resources.imageview("/Enum.png")

        add(EnumWizardStep1::class)

        //TODO find all buttons in button bar and add "addClass("btn-primary", "btn-lg")"
    }

    override fun onCancel() {
        confirm("Confirm cancel", "Do you really want to loose your progress?") {
            cancel()
        }
    }

    override fun onSave() {
        if (isComplete) {
            println("succes")
        }
    }

}