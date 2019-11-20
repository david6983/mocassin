package com.david.mocassin.view.components.wizards

import com.david.mocassin.model.c_components.Cenum
import com.david.mocassin.model.c_components.CenumAttribute
import com.david.mocassin.model.c_components.CenumModel
import javafx.beans.property.ReadOnlyStringWrapper

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.util.converter.IntegerStringConverter
import javafx.util.converter.NumberStringConverter

import tornadofx.*

//TODO verification champ
//TODO affichage de la tableview et edition
//TODO reset du model et du wizard

typealias Row = Map<String, String>

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

    //val attributes = enumModel.attributes.value

    var attributeNameField : TextField by singleAssign()
    var attributeValueField : TextField by singleAssign()

    //var prevSelection: CenumAttribute? = null
    var attributesTable: TableView<CenumAttribute> by singleAssign()

    init {
        //attributeNameField.textProperty().bindBidirectional()
    }

    override val root = hbox {
        form {
            fieldset("Add fields inside") {
                field("name") {
                    textfield("") {
                        attributeNameField = this
                    }
                }
                field("value") {
                    textfield("0") {
                        attributeValueField = this
                    }
                }
                button("Add") {
                    action {
                        val attr = CenumAttribute(
                            attributeNameField.textProperty().value,
                            attributeValueField.textProperty().value.toInt()
                        )

                        println(attr.name)
                        println(attr.value)

                        enumModel.attributes.value.add(attr)

                        //form reset
                        attributeNameField.textProperty().value = ""
                        attributeValueField.textProperty().value = enumModel.attributes.value.count().toString()

                        println(enumModel.attributes.toString())
                    }
                }
            }
        }
        tableview(enumModel.attributes) {
            attributesTable = this
            column("Name", CenumAttribute::name)
            column("Value", CenumAttribute::value)
        }
    }
    /*
    private fun editAttribute(attribute: CenumAttribute?) {
        if (attribute != null) {
            prevSelection?.apply {
                nameProperty.unbindBidirectional(attributeNameField.textProperty())
                valueProperty.unbindBidirectional(attributeNameField.textProperty() as Integer!)
            }
            attributeNameField.bind(attribute.nameProperty)
            attributeNameField.bind(attribute.valueProperty)
            prevSelection = attribute
        }
    }*/
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