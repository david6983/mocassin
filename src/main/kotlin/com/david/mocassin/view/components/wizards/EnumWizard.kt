package com.david.mocassin.view.components.wizards

import com.david.mocassin.model.c_components.Cenum
import com.david.mocassin.model.c_components.CenumModel
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.util.converter.NumberStringConverter
import tornadofx.*

class EnumWizardStep1 : View("Enum name") {
    val enumModel: CenumModel by inject()

    override val complete = enumModel.valid(enumModel.name)

    override fun onSave() {
        //println(enumModel.itemProperty.value)
        //enumModel.item = Cenum("")
        //println(enumModel.item.name)
    }

    override val root = form {
        fieldset(title) {
            field("Name") {
                textfield(enumModel.name).required()
            }
        }
    }


}

class EnumWizardStep2 : View("Enumeration values") {
    val enumModel: CenumModel by inject()

    val key_input = SimpleStringProperty()
    val value_input = SimpleIntegerProperty()

    //override val complete = enum.valid(enum.attributes)

    override val root = hbox {
        form {
            fieldset("Add fields inside") {
                field("name") {
                    textfield().textProperty().bindBidirectional(key_input)
                }
                field("value") {
                    textfield().textProperty().bindBidirectional(value_input, NumberStringConverter())
                }
                button("Add") {
                    action {
                        println(key_input.value)
                        println(value_input.value)

                        enumModel.attributes.value[key_input.value] = value_input.value

                        key_input.value = ""
                        value_input.value = 0
                    }
                }
            }
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

        enumModel.item = Cenum("")
        //TODO find all buttons in button bar and add "addClass("btn-primary", "btn-lg")"
    }

    override fun onCancel() {
        confirm("Confirm cancel", "Do you really want to loose your progress?") {
            cancel()
        }
    }
}