package com.david.mocassin.view.components

import com.david.mocassin.controller.ProjectController
import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.view.components.fragments.NewProjectModal
import com.david.mocassin.view.components.wizards.EnumWizard
import com.david.mocassin.view.components.wizards.UnionWizard
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import tornadofx.*
//TODO add a controller
class MainMenuBar : View() {
    private val projectController: ProjectController by inject()

    private val enumIcon = resources.imageview("/icons/enum32.png")
    private val unionIcon = resources.imageview("/icons/union32.png")

    override val root = menubar {
        menu("File") {
            item("New project", keyCombination = "Shortcut+N").action {
                find<NewProjectModal>().openModal(stageStyle = StageStyle.UTILITY)
            }
            separator()
            item("Open").action {
                val ef = arrayOf(FileChooser.ExtensionFilter("Mocassin file (*.moc)", "*.moc"))
                val file = chooseFile("Select a .moc file to open", ef, FileChooserMode.Single)
                println(file)
            }
            item("Import from Web application")
            separator()
            item("Save to Web application")
            item("Save project locally")
            separator()
            item("Export")
            item("Generate")
        }
        menu("Add") {
            item("SList (Single-chained Linked List)")
            item("DList (Double-chained Linked List)")
            separator()
            item("BTree (Binary Tree)")
            item("BSTree (Binary Search Tree)")
            item("Tree (Multi-node Tree)")
        }
        menu("New") {
            item("Enum", keyCombination = "Shortcut+E", graphic = enumIcon).action {
                // we need to create a new wizard each time we add a new enumeration
                val enumWizard = EnumWizard()
                // open the wizard as a modal window
                enumWizard.openModal()
                // when the user click on "finish"
                enumWizard.onComplete {
                    println(enumWizard.enumModel.item.toJSON())
                    // save the enumeration
                    projectController.userModel.add(enumWizard.enumModel.item)

                    information("Enumeration successfully added !", enumWizard.enumModel.item.toJSON().toString())

                    // wizard model reset for a next one
                    enumWizard.enumModel.item =
                        Cenum("")
                    enumWizard.enumModel.attributes.value.clear()
                }
            }
            item("Union", keyCombination = "Shortcut+U", graphic = unionIcon).action {
                val unionWizard = UnionWizard()
                unionWizard.openModal()
                unionWizard.onComplete {
                    //println(unionWizard.unionModel.item.toJSON())
                    projectController.userModel.add(unionWizard.unionModel.item)
                    information("Union successfully added !", unionWizard.unionModel.item.toJSON().toString())

                    unionWizard.unionModel.attributes.value.clear()
                }
            }
            item("Struct")
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