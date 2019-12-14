package com.david.mocassin.view.components

import com.david.mocassin.controller.MainMenuBarController
import com.david.mocassin.view.components.sidebar_drawers.LeftSideDrawer
import javafx.scene.paint.Color
import tornadofx.*

class MainMenuBar : View() {
    private val controller: MainMenuBarController by inject()

    override val root = menubar {
        menu("File") {
            item("New project", keyCombination = "Shortcut+N").action { controller.newProject() }
            separator()
            item("Open from computer", keyCombination = "Shortcut+O").action { controller.openFromComputer() }
            item("Save project locally", keyCombination = "Shortcut+S") {
                graphic = hbox {
                    rectangle {
                        fill = Color.BLUE
                        width = 8.0
                        height = 32.0
                    }
                    imageview(resources["/icons/enum32.png"])
                }
            }.action { controller.saveProjectLocally() }
            separator()
            item("Save to Web application", keyCombination = "Shortcut+W").isDisable = true
            item("Import from Web application", keyCombination = "Shortcut+I").isDisable = true
            separator()
            item("Export", keyCombination = "Shortcut+X")
            item("Generate", keyCombination = "Shortcut+G")
        }
        menu("Add") {
            item("SList (Single-chained Linked List)", keyCombination = "Shortcut+L", graphic = controller.slistIcon)
            item(
                "DList (Double-chained Linked List)",
                keyCombination = "Shortcut+D",
                graphic = controller.dlistIcon
            ).isDisable = true
            separator()
            item("BTree (Binary Tree)", keyCombination = "Shortcut+B", graphic = controller.btreeIcon).isDisable = true
            item("BSTree (Binary Search Tree)", keyCombination = "Shortcut+Y").isDisable = true
            item("Tree (Multi-node Tree)", keyCombination = "Shortcut+T", graphic = controller.treeIcon).isDisable =
                true
            separator()
            item("Graph", keyCombination = "Shortcut+P").isDisable = true
            separator()
            item("Hash table", keyCombination = "Shortcut+H").isDisable = true
        }
        menu("New") {
            item("Enum", keyCombination = "Shortcut+E", graphic = controller.enumIcon).action {
                controller.newEnum()
            }
            item("Union", keyCombination = "Shortcut+U", graphic = controller.unionIcon).action {
                controller.newUnion()
            }
            item("Struct", keyCombination = "Shortcut+R", graphic = controller.structIcon).action {
                controller.newStruct()
            }
            separator()
            item("Pseudo-object").isDisable = true
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