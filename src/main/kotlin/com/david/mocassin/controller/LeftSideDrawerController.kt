package com.david.mocassin.controller

import com.david.mocassin.model.DataStructureEnum
import com.david.mocassin.model.c_components.CuserType
import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.model.c_components.c_struct.CuserStructure
import com.david.mocassin.model.c_components.c_union.Cunion
import com.david.mocassin.view.MainView
import com.david.mocassin.view.components.MainMenuBar
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.MultipleSelectionModel
import javafx.scene.control.TreeItem
import tornadofx.Controller
import tornadofx.get
import tornadofx.onChange
import java.util.*

class LeftSideDrawerController : Controller() {
    private val projectController: ProjectController by inject()
    private val mainMenuBar: MainMenuBar by inject()
    val editTabPane: MainView by inject()

    val packageName: SimpleStringProperty =
        SimpleStringProperty(projectController.userModel.packageName)

    private fun addEnumNode(root: TreeItem<String>, enum: CuserType): Boolean {
        val node = TreeItem<String>((enum as Cenum).name)

        enum.attributes.forEach { attribute ->
            node.children.add(TreeItem<String>("${attribute.name} [${attribute.value}]"))
        }

        return root.children.add(node)
    }

    fun addLastEnumNode(root: TreeItem<String>) {
        if (!projectController.userModel.userEnumList.isEmpty()) {
            projectController.userModel.userEnumList.last { enum ->
                addEnumNode(root, enum)
            }
        }
    }

    fun updateEnumTree(root: TreeItem<String>) {
        if (!projectController.userModel.userEnumList.isEmpty()) {
            projectController.userModel.userEnumList.forEach { enum ->
                addEnumNode(root, enum)
            }
        }
    }

    private fun addUnionNode(root: TreeItem<String>, union: CuserType): Boolean {
        val node = TreeItem<String>((union as Cunion).name)
        //TODO fix duplicated code
        if (union.attributes.isNotEmpty()) {
            union.attributes.forEach { attribute ->
                node.children.add(TreeItem<String>("${attribute.name} [${attribute.getTypeAsString()}]"))
            }
        } else {
            node.children.add(TreeItem<String>("empty"))
        }

        return root.children.add(node)
    }

    fun addLastUnionNode(root: TreeItem<String>) {
        if (!projectController.userModel.userUnionList.isEmpty()) {
            projectController.userModel.userUnionList.last { union ->
                addUnionNode(root, union)
            }
        }
    }

    fun updateUnionTree(root: TreeItem<String>) {
        if (!projectController.userModel.userUnionList.isEmpty()) {
            projectController.userModel.userUnionList.forEach { union ->
                addUnionNode(root, union)
            }
        }
    }

    private fun addStructNode(root: TreeItem<String>, struct: CuserType): Boolean {
        val node = TreeItem<String>((struct as CuserStructure).name)

        if (struct.attributes.isNotEmpty()) {
            struct.attributes.forEach { attribute ->
                node.children.add(TreeItem<String>("${attribute.name} [${attribute.getTypeAsString()}]"))
            }
        } else {
            node.children.add(TreeItem<String>("empty"))
        }

        return root.children.add(node)
    }

    fun addLastStructNode(root: TreeItem<String>) {
        if (!projectController.userModel.userStructureList.isEmpty()) {
            projectController.userModel.userStructureList.last { struct ->
                addStructNode(root, struct)
            }
        }
    }

    fun updateStructTree(root: TreeItem<String>) {
        if (!projectController.userModel.userStructureList.isEmpty()) {
            projectController.userModel.userStructureList.forEach { struct ->
                addStructNode(root, struct)
            }
        }
    }

    fun addLastSlistNode(root: TreeItem<String>) {
        if (!projectController.userDataStructures.isEmpty()) {
            projectController.userDataStructures.last { slist ->
                if (slist.userVariables.isNotEmpty()) {
                    return slist.userVariables.forEach { type ->
                        root.children.add(TreeItem(type.toString()))
                    }
                } else {
                    root.children.add(TreeItem("no simple variables added"))
                }
            }
        }
    }

    fun updateSlistTree(root: TreeItem<String>) {
        if (!projectController.userDataStructures.isEmpty()) {
            projectController.userDataStructures.forEach { slist ->
                if (slist.userVariables.isNotEmpty()) {
                    return slist.userVariables.forEach { type ->
                        root.children.add(TreeItem(type.toString()))
                    }
                } else {
                    root.children.add(TreeItem("no simple variables added"))
                }
            }
        }
    }

    fun isSelectedValueFromUserStructuresValid(value: String?, messages: ResourceBundle): Boolean {
        return when(value) {
            messages["ld_enum"] -> false
            messages["ld_union"]-> false
            messages["ld_struct"] -> false
            projectController.userModel.packageName -> false
            null -> false
            else -> true
        }
    }

    fun isSelectedValueFromDataStructuresValid(value: String?, messages: ResourceBundle): Boolean {
        return when(value) {
            messages["ld_slist"] -> true
            null -> false
            else -> false
        }
    }

    fun isValidParent(parent: String?, messages: ResourceBundle): Boolean {
        return when(parent) {
            messages["ld_enum"] -> true
            messages["ld_union"] -> true
            messages["ld_struct"] -> true
            else -> false
        }
    }

    fun isPaneNotExist(text: String?): Boolean {
        return editTabPane.centerTabPane.tabs.find { it.text == text } == null
    }

    fun removeSelectedUserStructureInModel(selectionModel: MultipleSelectionModel<TreeItem<String>>, messages: ResourceBundle) {
        when(selectionModel.selectedItem.parent.value) {
            messages["ld_enum"] -> {
                projectController.userModel.remove(
                    projectController.userModel.findEnumByName(selectionModel.selectedItem.value)
                )
                selectionModel.selectedItem.parent.children.remove(selectionModel.selectedItem)
            }
            messages["ld_union"]-> {
                projectController.userModel.remove(
                    projectController.userModel.findUnionByName(selectionModel.selectedItem.value)
                )
                selectionModel.selectedItem.parent.children.remove(selectionModel.selectedItem)
            }
            messages["ld_struct"] -> {
                projectController.userModel.remove(
                    projectController.userModel.findStructByName(selectionModel.selectedItem.value)
                )
                selectionModel.selectedItem.parent.children.remove(selectionModel.selectedItem)
            }
        }
    }

    fun removeSelectedDataStructureInModel(selectionModel: MultipleSelectionModel<TreeItem<String>>, messages: ResourceBundle) {
        when(selectionModel.selectedItem.value) {
            messages["ld_slist"] -> {
                projectController.userDataStructures.removeIf { it.type == DataStructureEnum.SLIST }
                selectionModel.selectedItem.children.clear()
                mainMenuBar.addSlistItem.isDisable = false
            }
        }
    }
}