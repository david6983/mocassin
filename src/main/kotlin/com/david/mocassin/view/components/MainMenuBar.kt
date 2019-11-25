package com.david.mocassin.view.components

import com.david.mocassin.model.c_components.Cenum
import com.david.mocassin.model.c_components.Cunion
import com.david.mocassin.view.components.wizards.EnumWizard
import com.david.mocassin.view.components.wizards.UnionWizard
import javafx.stage.FileChooser
import tornadofx.*
//TODO add a controller
class MainMenuBar : View() {
    val enumIcon = resources.imageview("/icons/enum32.png")
    val unionIcon = resources.imageview("/icons/union32.png")

    val tmpEnumList = mutableListOf<Cenum>()
    val tmpUnionList = mutableListOf<Cunion>()

    override val root = menubar {
        menu("File") {
            item("New project")
            item("open").action {
                val ef = arrayOf(FileChooser.ExtensionFilter("Mocassin file (*.moc)", "*.moc"))
                val file = chooseFile("Select a .moc file to open", ef, FileChooserMode.Single)
                println(file)
            }
            item("Save")
            separator()
            item("export")
            item("generate")
        }
        menu("Add") {
            item("SList (Single-chained Linked List)")
            item("DList (Double-chained Linked List)")
            separator()
            item("BTree (Binary Tree)")
            item("BSTree (Binary Search Tree)")
            item("Tree (Multi-node Tree)")
            //separator()
            //item("SANN (Simple Artificial Neural Network)")
        }
        menu("New") {
            item("Enum", keyCombination = "Shortcut+E", graphic = enumIcon).action {
                // we need to create a new wizard each time we add a new enumeration
                val enumWizard = EnumWizard()
                // open the wizard as a modal window
                enumWizard.openModal()
                // when the user click on "finish"
                enumWizard.onComplete {
                    println(enumWizard.enumModel.item.toJSON())
                    // save the enumeration
                    tmpEnumList.add(enumWizard.enumModel.item)

                    // wizard model reset for a next one
                    enumWizard.enumModel.item = Cenum("")
                    enumWizard.enumModel.attributes.value.clear()

                    information("Enumeration successfully added !", tmpEnumList.asObservable().toJSON().toString())

                }
            }
            item("Union", keyCombination = "Shortcut+U", graphic = unionIcon).action {
                val unionWizard = UnionWizard()
                unionWizard.openModal()
                unionWizard.onComplete {
                    println(unionWizard.unionModel.item.toJSON())

                    tmpUnionList.add(unionWizard.unionModel.item)
                    unionWizard.unionModel.attributes.value.clear()

                    information("Union successfully added !", tmpUnionList.asObservable().toJSON().toString())
                }
            }
            item("struct")
        }
        menu("Preferences") {

        }
        menu("Help") {
            item("Documentation")
            separator()
            item("About")
        }
    }
}