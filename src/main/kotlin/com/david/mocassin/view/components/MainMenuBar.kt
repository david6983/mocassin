package com.david.mocassin.view.components

import com.david.mocassin.view.components.wizards.EnumWizard
import javafx.stage.FileChooser
import tornadofx.*

class MainMenuBar : View() {
    val enumIcon = resources.imageview("/icons/enum32.png")

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
            separator()
            item("SANN (Simple Artificial Neural Network)")
        }
        menu("New") {
            item("Enum", keyCombination = "Shortcut+E", graphic = enumIcon).action {
                find<EnumWizard> {
                    onComplete {
                        println(enumModel.item.toJSON())
                        println(enumModel.item)
                        println(enumModel.attributes)
                        //println(enumModel.item.toJSON())
                    }
                    openModal()
                }
            }
            item("union")
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