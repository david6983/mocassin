package com.david.mocassin.view.components

import com.david.mocassin.view.components.wizards.EnumWizard
import com.david.mocassin.view.components.wizards.EnumWizardStep1
import tornadofx.*

class MainMenuBar : View() {
    override val root = menubar {
        menu("File") {
            item("New project")
            item("open")
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
            item("Enum").action {
                find<EnumWizard> {
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