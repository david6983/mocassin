package com.david.mocassin.view.components.sidebar_drawers

import com.david.mocassin.controller.LeftSideDrawerController
import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.CuserType
import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.model.c_components.c_enum.CenumAttribute
import com.david.mocassin.view.components.fragments.cell_fragments.enum_attributes_cell_fragments.CenumAttributeNameCellFragment
import com.david.mocassin.view.components.fragments.cell_fragments.enum_attributes_cell_fragments.CenumAttributeValueCellFragment
import javafx.scene.control.*
import tornadofx.*

class LeftSideDrawer : View() {
    val controller: LeftSideDrawerController by inject()
    val projectController: ProjectController by inject()

    var userStructureTree: TreeView<String> by singleAssign()
    var generatedStructureTree: TreeView<String> by singleAssign()
    var filesList: ListView<String> by singleAssign()

    val userStructureRoot: TreeItem<String> = TreeItem(controller.packageName.value)
    val generatedStructureRoot: TreeItem<String> = TreeItem(controller.packageName.value)

    val enumRoot: TreeItem<String> = TreeItem(messages["ld_enum"])
    val unionRoot: TreeItem<String> = TreeItem(messages["ld_union"])
    val structRoot: TreeItem<String> = TreeItem(messages["ld_struct"])

    val slistRoot: TreeItem<String> = TreeItem(messages["ld_slist"])

    var fileDrawerItem: DrawerItem by singleAssign()

    init {
        controller.packageName.onChange {
            userStructureRoot.value = it
            generatedStructureRoot.value = it
        }
    }

    fun clearAll() {
        enumRoot.children.clear()
        unionRoot.children.clear()
        structRoot.children.clear()

        slistRoot.children.clear()

        filesList.items.clear()
        fileDrawerItem.expanded = false
    }

    override val root = drawer(multiselect = true) {
        item(messages["ld_generated_struct"], expanded = true) {
            treeview<String> {
                generatedStructureTree = this

                root = generatedStructureRoot
                root.isExpanded = true

                root.children.add(slistRoot)

                cellFormat { text = it }

                onUserSelect {
                    println(selectedValue)
                    contextMenu = if (controller.isSelectedValueFromDataStructuresValid(selectedValue, messages)) {
                        ContextMenu().apply {
                            item(messages["delete"]).action { controller.removeSelectedDataStructureInModel(selectionModel, messages) }
                        }
                    } else {
                        null
                    }
                }
            }
        }
        item(messages["ld_user_structures"], expanded = true) {
            treeview<String> {
                userStructureTree = this
                root = userStructureRoot
                root.isExpanded = true

                controller.addLastEnumNode(enumRoot)

                root.children.add(enumRoot)
                root.children.add(unionRoot)
                root.children.add(structRoot)

                cellFormat { text = it }

                onDoubleClick {
                    if (controller.isSelectedValueFromUserStructuresValid(selectedValue, messages)
                        && controller.isValidParent(selectionModel.selectedItem.parent.value, messages)
                    ) {
                        val type = selectionModel.selectedItem.parent.value
                            .split("[")[1].removeSuffix("]")
                        when(type) {
                            "enum" -> {
                                val e: CuserType? = projectController.userModel.userEnumList.last {
                                    (it as Cenum).name == selectedValue
                                }
                                if (e != null) {
                                    println((e as Cenum).name)
                                }
                                controller.editEnum((e as Cenum))
                            }
                        }

                    }
                }

                onUserSelect {
                    contextMenu = if (controller.isSelectedValueFromUserStructuresValid(selectedValue, messages)
                        && controller.isValidParent(selectionModel.selectedItem.parent.value, messages)) {
                        ContextMenu().apply {
                            item(messages["delete"]).action { controller.removeSelectedUserStructureInModel(selectionModel, messages) }
                        }
                    } else {
                        null
                    }
                }
            }
        }
        item(messages["ld_files_view"]) {
            fileDrawerItem = this
            listview<String> {
                filesList = this

                onDoubleClick {
                    println(this.selectedItem)
                }
            }
        }
    }
}