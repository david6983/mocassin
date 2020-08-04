package com.david.mocassin.view.components.fragments

import com.david.mocassin.utils.lang.SupportedLocale
import javafx.scene.control.ComboBox
import tornadofx.*
import java.util.*

class ChangeLanguageModal: Fragment("Change language") {
    private var langageField : ComboBox<String> by singleAssign()

    override val root = form {
        fieldset("Choose your language") {
            field("Language") {
                vbox {
                    combobox<String>() {
                        langageField = this
                        SupportedLocale.supportedLocals.forEach {
                            items.add("${it.local.displayLanguage} [${it.local.toLanguageTag()}]")
                        }
                    }.selectionModel.selectFirst()
                }
            }
            button("Select").action {
                val tag = langageField.selectionModel.selectedItem.split("[")[1].removeSuffix("]")
                FX.locale = Locale.forLanguageTag(tag)
                close()
            }
        }
    }
}