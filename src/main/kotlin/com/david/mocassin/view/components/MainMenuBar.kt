package com.david.mocassin.view.components

import com.david.mocassin.controller.MainMenuBarController
import com.david.mocassin.model.DataStructureEnum
import com.david.mocassin.view.components.fragments.ChangeNameModal
import com.david.mocassin.view.components.fragments.NewProjectModal
import javafx.scene.control.MenuItem
import javafx.scene.paint.Color
import javafx.stage.StageStyle
import tornadofx.*

class MainMenuBar : View() {
    private val controller: MainMenuBarController by inject()

    var addSlistItem: MenuItem by singleAssign()

    private fun newProject() {
        find<NewProjectModal>().openModal(stageStyle = StageStyle.UTILITY)
    }

    private fun changeName() {
        find<ChangeNameModal>().openModal(stageStyle = StageStyle.UTILITY)
    }

    override val root = menubar {
        menu("File") {
            item("New project", keyCombination = "Shortcut+N") {
                graphic = hbox {
                    rectangle {
                        fill = Color.GREEN
                        width = 8.0
                        height = 32.0
                    }
                    imageview(resources["/icons/file32.png"])
                }
            }.action { newProject() }
            separator()
            item("Open from computer", keyCombination = "Shortcut+O") {
                graphic = hbox {
                    rectangle {
                        fill = Color.CORAL
                        width = 8.0
                        height = 32.0
                    }
                    imageview(resources["/icons/open32.png"])
                }
            }.action { controller.openFromComputer() }
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
            item("Test", keyCombination = "Shortcut+X").isDisable = true
            item("Generate", keyCombination = "Shortcut+G").action {
                controller.generateProject()
            }
        }
        menu("Generate") {
            item(DataStructureEnum.SLIST.toString(), keyCombination = "Shortcut+L", graphic = controller.slistIcon){
                addSlistItem = this
            }.action {
                controller.newSlist()
            }
            item(
                DataStructureEnum.DLIST.toString(),
                keyCombination = "Shortcut+D",
                graphic = controller.dlistIcon
            ).isDisable = true
            separator()
            item(DataStructureEnum.BTREE.toString(), keyCombination = "Shortcut+B", graphic = controller.btreeIcon).isDisable = true
            item(DataStructureEnum.BSTREE.toString(), keyCombination = "Shortcut+Y").isDisable = true
            item(DataStructureEnum.TREE.toString(), keyCombination = "Shortcut+T", graphic = controller.treeIcon).isDisable =
                true
            item(DataStructureEnum.QUADTREE.toString(), keyCombination = "Shortcut+Q").isDisable =
                true
            item(DataStructureEnum.RTREE.toString(), keyCombination = "Shortcut+R").isDisable =
                true
            separator()
            item(DataStructureEnum.GRAPH.toString(), keyCombination = "Shortcut+P").isDisable = true
            separator()
            item(DataStructureEnum.HASHTABLE.toString(), keyCombination = "Shortcut+H").isDisable = true
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
        }
        menu("Preferences") {
            item("Change package name").action { changeName() }
        }
        menu("Help") {
            item("Documentation")
            separator()
            item("About")
        }
    }
}