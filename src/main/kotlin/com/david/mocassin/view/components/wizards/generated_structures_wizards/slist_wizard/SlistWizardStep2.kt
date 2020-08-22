package com.david.mocassin.view.components.wizards.generated_structures_wizards.slist_wizard

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.DataStructureModel
import com.david.mocassin.model.c_components.CtypeEnum
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.*
import tornadofx.*

class SlistWizardStep2 : View() {
    val projectController: ProjectController by inject()

    private val slistModel: DataStructureModel by inject()

    private var simpleTypeField : ComboBox<String> by singleAssign()

    var list: ListView<String> = ListView()

    var selectedSimpleType = SimpleStringProperty()

    init {
        title = messages["gsw_slw_step2_title"]
    }

    override val root = hbox {
        form {
            fieldset(messages["add_fields_inside"]) {
                field(messages["choose_type"]) {
                    hbox {
                        combobox<String>(selectedSimpleType) {
                            simpleTypeField = this
                            items =
                                CtypeEnum.toObservableArrayList()
                        }.selectionModel.selectFirst()

                        button(messages["add_button"]) {
                            enableWhen(slistModel.valid)
                            action {
                                if (simpleTypeField.value != null) {
                                    val tmpType: CtypeEnum? = CtypeEnum.find(simpleTypeField.value)
                                    tmpType?.let { type ->
                                        slistModel.userVariables.value.add(type)
                                        list.items.add(type.toString())
                                        simpleTypeField.items.removeIf { it == type.toString()}
                                    }
                                    //form reset
                                    simpleTypeField.selectionModel.selectFirst()
                                }
                            }
                        }

                        spacing = 10.0
                    }
                }


                listview<String> {
                    list = this

                    contextMenu = ContextMenu().apply{
                        item(messages["delete"]).action {
                            selectedItem?.apply{
                                println(this)
                                println(slistModel.userVariables.value.size)
                                slistModel.userVariables.value.removeIf { it.name == this }
                                println(slistModel.userVariables.value.size)
                                list.items.removeIf { it == this }
                                simpleTypeField.items.add(0, this)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        list.items.clear()
        simpleTypeField.items = CtypeEnum.toObservableArrayList()
        simpleTypeField.selectionModel.selectFirst()
    }
}