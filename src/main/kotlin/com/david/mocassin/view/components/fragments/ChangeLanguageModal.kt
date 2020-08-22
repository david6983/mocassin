package com.david.mocassin.view.components.fragments

import com.david.mocassin.utils.lang.SupportedLocale
import javafx.scene.control.ComboBox
import tornadofx.*
import java.util.*

class ChangeLanguageModal: Fragment("") {
    private var langageField : ComboBox<String> by singleAssign()

    init {
        title = messages["change_language"]
    }

    override val root = form {
        fieldset(messages["clm_choose_language"]) {
            field(messages["clm_language"]) {
                vbox {
                    combobox<String>() {
                        langageField = this
                        SupportedLocale.supportedLocals.forEach {
                            items.add("${it.local.displayLanguage} [${it.local.toLanguageTag()}]")
                        }
                    }.selectionModel.selectFirst()
                }
            }
            button(messages["clm_select"]).action {
                val tag = langageField.selectionModel.selectedItem.split("[")[1].removeSuffix("]")
                FX.locale = Locale.forLanguageTag(tag)
                close()
            }
        }
    }
}