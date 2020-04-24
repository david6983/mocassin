package com.david.mocassin.controller

import com.david.mocassin.model.DataStructureEnum
import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.model.c_components.c_struct.CuserStructure
import com.david.mocassin.model.c_components.c_union.Cunion
import com.david.mocassin.view.MainView
import com.david.mocassin.view.components.MainMenuBar
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.MultipleSelectionModel
import javafx.scene.control.TreeItem
import tornadofx.Controller

class LeftSideDrawerController : Controller() {
    private val projectController: ProjectController by inject()
    private val mainMenuBar: MainMenuBar by inject()
    val editTabPane: MainView by inject()

    val packageName: SimpleStringProperty =
        SimpleStringProperty(projectController.userModel.packageName)

    fun addEnumNode(root: TreeItem<String>) {
        if (!projectController.userModel.userEnumList.isEmpty()) {
            projectController.userModel.userEnumList.last { enum ->
                val node =
                    TreeItem<String>((enum as Cenum).name)
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
                val node =
                    TreeItem<String>((union as Cunion).name)
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
                val node =
                    TreeItem<String>((struct as CuserStructure).name)
                struct.attributes.forEach { attribute ->
                    node.children.add(TreeItem<String>("${attribute.name} [${attribute.getTypeAsString()}]"))
                }
                root.children.add(node)
            }
        }
    }

    fun addSlistNode(root: TreeItem<String>) {
        if (!projectController.userDataStructures.isEmpty()) {
            projectController.userDataStructures.last { slist ->
                return slist.userVariables.forEach { type ->
                    root.children.add(TreeItem(type.toString()))
                }
            }
        }
    }

    fun isSelectedValueFromUserStructuresValid(value: String?): Boolean {
        return when(value) {
            ENUM -> false
            UNION-> false
            STRUCT -> false
            projectController.userModel.packageName -> false
            null -> false
            else -> true
        }
    }

    fun isSelectedValueFromDataStructuresValid(value: String?): Boolean {
        return when(value) {
            SLIST -> true
            null -> false
            else -> false
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

    fun removeSelectedUserStructureInModel(selectionModel: MultipleSelectionModel<TreeItem<String>>) {
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

    fun removeSelectedDataStructureInModel(selectionModel: MultipleSelectionModel<TreeItem<String>>) {
        when(selectionModel.selectedItem.value) {
            SLIST -> {
                projectController.userDataStructures.removeIf { it.type == DataStructureEnum.SLIST }
                selectionModel.selectedItem.children.clear()
                mainMenuBar.addSlistItem.isDisable = false
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