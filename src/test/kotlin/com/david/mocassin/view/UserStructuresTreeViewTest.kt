package com.david.mocassin.view

import com.david.mocassin.model.UserModel
import com.david.mocassin.model.c_components.*
import javafx.scene.control.TreeItem
import tornadofx.*

class UserStructuresTreeViewTestController : Controller() {
    val model = UserModel("project name")
    val e1 = Cenum("MyEnum1")
    val e2 = Cenum("enum 2")

    val enums = mutableListOf(e1, e2)

    init {
        e1.add("T")
        e1.add("T2")
        e2.add("R")
        e2.add("f")
    }
}

class UserStructuresTreeViewTestView : View() {
    val controller: UserStructuresTreeViewTestController by inject()

    init {
        println(controller.enums.map { it.name })
    }

    override val root = hbox {
        val p = tabpane {

        }
        val t = treeview<String> {
            root = TreeItem(controller.model.packageName)

            cellFormat { text = it }

            root.children.add(TreeItem("Enumerations"))
            root.children.add(TreeItem("Unions"))
            root.children.add(TreeItem("Structures"))

            val enumsRoot = root.children.find { it.value == "Enumerations" }

            for (enum:Cenum in controller.enums) {
                println(enum.name)
                enumsRoot?.children?.add(TreeItem(enum.name))
            }

            //populate { parent -> controller.enums.map { it.name }
            //}

            setOnMouseClicked {
                if (it.clickCount == 2) {
                    println("Double click")
                    p.tab("hello")
                }
            }
        }
        button("add node to Enumerations").action {
            //controller.enums.add()
            t.root.children.find { it.value == "Enumerations" }?.children?.add(TreeItem("Hello"))
        }
    }
}

class UserStructuresTreeViewTestApp : App(UserStructuresTreeViewTestView::class)

fun main(args: Array<String>) {
    launch<UserStructuresTreeViewTestApp>(args)
}