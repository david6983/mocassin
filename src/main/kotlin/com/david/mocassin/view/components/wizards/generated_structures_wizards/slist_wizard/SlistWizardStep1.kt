package com.david.mocassin.view.components.wizards.generated_structures_wizards.slist_wizard

import tornadofx.*

class SlistWizardStep1: View() {
    init {
        title = messages["gsw_slw_step1_title"]
    }

    override val root = vbox {
        text("${messages["gsw_slw_step1_t1"]}\n\n")
        text("- ${messages["gsw_slw_step1_t2"]}\n\n")
        text("- ${messages["gsw_slw_step1_t3"]}")
    }
}