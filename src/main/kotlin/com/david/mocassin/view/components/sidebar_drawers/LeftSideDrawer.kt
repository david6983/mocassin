package com.david.mocassin.view.components.sidebar_drawers

import com.david.mocassin.controller.LeftSideDrawerController
import javafx.scene.control.*
import tornadofx.*

class LeftSideDrawer : View() {
    val controller: LeftSideDrawerController by inject()

    var userStructureTree: TreeView<String> by singleAssign()
    var generatedStructureTree: TreeView<String> by singleAssign()
    var filesList: ListView<String> by singleAssign()

    val userStructureRoot: TreeItem<String> = TreeItem(controller.packageName.value)
    val enumRoot: TreeItem<String> = TreeItem(LeftSideDrawerController.ENUM)
    val unionRoot: TreeItem<String> = TreeItem(LeftSideDrawerController.UNION)
    val structRoot: TreeItem<String> = TreeItem(LeftSideDrawerController.STRUCT)

    val slistRoot: TreeItem<String> = TreeItem(LeftSideDrawerController.SLIST)

    val fileRoot: TreeItem<String> = TreeItem("nothing generated")

    fun clearAll() {
        enumRoot.children.clear()
        unionRoot.children.clear()
        structRoot.children.clear()

        slistRoot.children.clear()

        fileRoot.children.clear()
    }

    override val root = drawer(multiselect = true) {
        item("Generated structures", expanded = true) {
            treeview<String> {
                generatedStructureTree = this

                root = TreeItem(controller.packageName.value)
                root.isExpanded = true

                root.children.add(slistRoot)

                cellFormat { text = it }

                onUserSelect {
                    println(selectedValue)
                    contextMenu = if (controller.isSelectedValueFromDataStructuresValid(selectedValue)) {
                        ContextMenu().apply {
                            item("Delete").action { controller.removeSelectedDataStructureInModel(selectionModel) }
                        }
                    } else {
                        null
                    }
                }
            }
        }
        item("User structures", expanded = true) {
            treeview<String> {
                userStructureTree = this
                root = userStructureRoot
                root.isExpanded = true

                controller.addEnumNode(enumRoot)

                root.children.add(enumRoot)
                root.children.add(unionRoot)
                root.children.add(structRoot)

                cellFormat { text = it }

                onDoubleClick {
                    if (controller.isSelectedValueFromUserStructuresValid(selectedValue)
                        && controller.isValidParent(selectionModel.selectedItem.parent.value)
                        && controller.isPaneNotExist(selectedValue)
                    ) {
                        controller.editTabPane.centerTabPane.tab(selectedValue)
                    }
                }

                onUserSelect {
                    contextMenu = if (controller.isSelectedValueFromUserStructuresValid(selectedValue)
                        && controller.isValidParent(selectionModel.selectedItem.parent.value)) {
                        ContextMenu().apply {
                            item("Delete").action { controller.removeSelectedUserStructureInModel(selectionModel) }
                        }
                    } else {
                        null
                    }
                }
            }
        }
        item("Files view") {
            listview<String> {
                filesList = this

                onDoubleClick {
                    if (selectedItem != null) {
                        controller.editTabPane.centerTabPane.tab(selectedItem)
                    }
                }

                onUserSelect {

                }
            }
        }
    }
}