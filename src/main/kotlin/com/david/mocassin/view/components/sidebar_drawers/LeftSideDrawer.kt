package com.david.mocassin.view.components.sidebar_drawers

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.model.c_components.c_struct.CuserStructure
import com.david.mocassin.model.c_components.c_union.Cunion
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import tornadofx.*

class LeftSideDrawerController : Controller() {
    val projectController: ProjectController by inject()

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
}

class LeftSideDrawer : View() {
    val controller: LeftSideDrawerController by inject()

    var userStructureTree: TreeView<String> by singleAssign()
    var generatedStructureTree: TreeView<String> by singleAssign()
    var filesTree: TreeView<String> by singleAssign()

    val enumRoot: TreeItem<String> = TreeItem("Enumerations [enum]")
    val unionRoot: TreeItem<String> = TreeItem("Unions [union]")
    val structRoot: TreeItem<String> = TreeItem("Structures [struct]")

    override val root = drawer(multiselect = true) {
        item("User structures") {
            treeview<String> {
                userStructureTree = this

                root = TreeItem(controller.projectController.userModel.packageName)
                root.isExpanded = true

                controller.addEnumNode(enumRoot)

                root.children.add(enumRoot)
                root.children.add(unionRoot)
                root.children.add(structRoot)

                cellFormat { text = it }
            }
        }
        item("Generated structures") {
            treeview<String> {
                generatedStructureTree = this

                root = TreeItem(controller.projectController.userModel.packageName)
                root.isExpanded = true

                root.children.add(TreeItem("Single Linked List [Slist]"))

                cellFormat { text = it }
            }
        }
        item("Files view") {
            treeview<String> {
                filesTree = this

                root = TreeItem(controller.projectController.userModel.packageName)
                root.isExpanded = true

                cellFormat { text = it }
            }
        }
    }
}