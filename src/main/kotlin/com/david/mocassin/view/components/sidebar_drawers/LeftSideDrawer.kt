package com.david.mocassin.view.components.sidebar_drawers

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.model.c_components.c_struct.CuserStructure
import com.david.mocassin.model.c_components.c_union.Cunion
import com.david.mocassin.view.MainView
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.control.*
import tornadofx.*

class LeftSideDrawerController : Controller() {
    private val projectController: ProjectController by inject()
    val editTabPane: MainView by inject()

    val packageName: String = projectController.userModel.packageName

    fun addEnumNode(root: TreeItem<String>) {
        if (!projectController.userModel.userEnumList.isEmpty()) {
            projectController.userModel.userEnumList.last { enum ->
                val node = TreeItem<String>((enum as Cenum).name)
                enum.attributes.forEach { attribute ->
                    node.children.add(TreeItem<String>("${attribute.name} [${attribute.value}]"))
                }
                root.children.add(node)
            }
        }
    }

    fun addUnionNode(root: TreeItem<String>) {
        if (!projectController.userModel.userUnionList.isEmpty()) {
            projectController.userModel.userUnionList.last { union ->
                val node = TreeItem<String>((union as Cunion).name)
                union.attributes.forEach { attribute ->
                    node.children.add(TreeItem<String>("${attribute.name} [${attribute.getTypeAsString()}]"))
                }
                root.children.add(node)
            }
        }
    }

    fun addStructNode(root: TreeItem<String>) {
        if (!projectController.userModel.userStructureList.isEmpty()) {
            projectController.userModel.userStructureList.last { struct ->
                val node = TreeItem<String>((struct as CuserStructure).name)
                struct.attributes.forEach { attribute ->
                    node.children.add(TreeItem<String>("${attribute.name} [${attribute.getTypeAsString()}]"))
                }
                root.children.add(node)
            }
        }
    }

    fun isSelectedValueValid(value: String?): Boolean {
        return when(value) {
            ENUM -> false
            UNION-> false
            STRUCT -> false
            projectController.userModel.packageName -> false
            null -> false
            else -> true
        }
    }

    fun isValidParent(parent: String?): Boolean {
        return when(parent) {
            ENUM -> true
            UNION -> true
            STRUCT -> true
            else -> false
        }
    }

    fun isPaneNotExist(text: String?): Boolean {
        return editTabPane.centerTabPane.tabs.find { it.text == text } == null
    }

    fun removeSelectedInModel(selectionModel: MultipleSelectionModel<TreeItem<String>>) {
        when(selectionModel.selectedItem.parent.value) {
            ENUM -> {
                projectController.userModel.remove(
                    projectController.userModel.findEnumByName(selectionModel.selectedItem.value)
                )
                selectionModel.selectedItem.parent.children.remove(selectionModel.selectedItem)
            }
            UNION-> {
                projectController.userModel.remove(
                    projectController.userModel.findUnionByName(selectionModel.selectedItem.value)
                )
                selectionModel.selectedItem.parent.children.remove(selectionModel.selectedItem)
            }
            STRUCT -> {
                projectController.userModel.remove(
                    projectController.userModel.findStructByName(selectionModel.selectedItem.value)
                )
                selectionModel.selectedItem.parent.children.remove(selectionModel.selectedItem)
            }
        }
    }

    companion object {
        const val ENUM: String = "Enumerations [enum]"
        const val UNION: String = "Unions [union]"
        const val STRUCT: String = "Structures [struct]"

        const val SLIST: String = "Single Linked List [Slist]"
    }
}

class LeftSideDrawer : View() {
    val controller: LeftSideDrawerController by inject()

    var userStructureTree: TreeView<String> by singleAssign()
    var generatedStructureTree: TreeView<String> by singleAssign()
    var filesTree: TreeView<String> by singleAssign()

    val enumRoot: TreeItem<String> = TreeItem(LeftSideDrawerController.ENUM)
    val unionRoot: TreeItem<String> = TreeItem(LeftSideDrawerController.UNION)
    val structRoot: TreeItem<String> = TreeItem(LeftSideDrawerController.STRUCT)

    override val root = drawer(multiselect = true) {
        item("User structures", expanded = true) {
            treeview<String> {
                userStructureTree = this
                root = TreeItem(controller.packageName)
                root.isExpanded = true

                controller.addEnumNode(enumRoot)

                root.children.add(enumRoot)
                root.children.add(unionRoot)
                root.children.add(structRoot)

                cellFormat { text = it }

                onDoubleClick {
                    if (controller.isSelectedValueValid(selectedValue)
                        && controller.isValidParent(selectionModel.selectedItem.parent.value)
                        && controller.isPaneNotExist(selectedValue)
                    ) {
                        controller.editTabPane.centerTabPane.tab(selectedValue)
                    }
                }

                onUserSelect {
                    contextMenu = if (controller.isSelectedValueValid(selectedValue)
                        && controller.isValidParent(selectionModel.selectedItem.parent.value)) {
                        ContextMenu().apply {
                            item("Delete").action { controller.removeSelectedInModel(selectionModel) }
                        }
                    } else {
                        null
                    }
                }
            }
        }
        item("Generated structures") {
            treeview<String> {
                generatedStructureTree = this

                root = TreeItem(controller.packageName)
                root.isExpanded = true

                root.children.add(TreeItem(LeftSideDrawerController.SLIST))

                cellFormat { text = it }
            }
        }
        item("Files view") {
            treeview<String> {
                filesTree = this

                root = TreeItem("nothing generated")
                //root.isExpanded = true

                cellFormat { text = it }
            }
        }
    }
}