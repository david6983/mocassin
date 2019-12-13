package com.david.mocassin.controller

import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.model.c_components.c_struct.CuserStructure
import com.david.mocassin.model.c_components.c_union.Cunion
import com.david.mocassin.view.components.fragments.NewProjectModal
import com.david.mocassin.view.components.wizards.user_structures_wizards.enum_wizard.EnumWizard
import com.david.mocassin.view.components.wizards.user_structures_wizards.struct_wizard.StructWizard
import com.david.mocassin.view.components.wizards.user_structures_wizards.union_wizard.UnionWizard
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import tornadofx.*

class MainMenuBarController : Controller() {
    val projectController: ProjectController by inject()

    val enumIcon = resources.imageview("/icons/enum32.png")
    val unionIcon = resources.imageview("/icons/union32.png")
    val structIcon = resources.imageview("/icons/struct32.png")

    val slistIcon = resources.imageview("/icons/slist32.png")
    val dlistIcon = resources.imageview("/icons/dlist32.png")
    val btreeIcon = resources.imageview("/icons/btree32.png")
    val treeIcon = resources.imageview("/icons/tree32.png")

    fun newProject() {
        find<NewProjectModal>().openModal(stageStyle = StageStyle.UTILITY)
    }

    fun openFromComputer() {
        val ef = arrayOf(FileChooser.ExtensionFilter("Mocassin file (*.moc)", "*.moc"))
        val file = chooseFile(
            "Select a .moc file to open",
            ef,
            FileChooserMode.Single
        )
        println(file)
    }

    fun saveProjectLocally() {
        if (projectController.userModel.isEmpty().not()) {
            val directory = chooseDirectory("Select a directory to save project")
            projectController.saveMocFiles(directory.toString())
        }
    }

    fun newEnum() {
        // we need to create a new wizard each time we add a new enumeration
        val enumWizard =
            EnumWizard()
        // open the wizard as a modal window
        enumWizard.openModal()
        // when the user click on "finish"
        enumWizard.onComplete {
            println(enumWizard.enumModel.item.toJSON())
            // save the enumeration
            projectController.userModel.add(enumWizard.enumModel.item)

            information(
                "Enumeration successfully added !",
                enumWizard.enumModel.item.toJSON().toString()
            )

            // wizard model reset for a next one
            enumWizard.enumModel.item = Cenum("")
            enumWizard.enumModel.attributes.value.clear()
        }
    }

    fun newUnion() {
        val unionWizard =
            UnionWizard()
        unionWizard.openModal()
        unionWizard.onComplete {
            projectController.userModel.add(unionWizard.unionModel.item)

            information(
                "Union successfully added !",
                unionWizard.unionModel.item.toJSON().toString()
            )

            unionWizard.unionModel.item = Cunion("")
            unionWizard.unionModel.attributes.value.clear()
            unionWizard.close()
        }
    }

    fun newStruct() {
        val structWizard =
            StructWizard()
        structWizard.openModal()
        structWizard.onComplete {
            projectController.userModel.add(structWizard.structModel.item)

            information(
                "Struct successfully added !",
                structWizard.structModel.item.toJSON().toString()
            )

            structWizard.structModel.item =
                CuserStructure("")
            structWizard.structModel.attributes.value.clear()
            structWizard.close()
        }
    }
}