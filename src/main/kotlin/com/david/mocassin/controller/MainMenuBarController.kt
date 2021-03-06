package com.david.mocassin.controller

import com.david.mocassin.model.DataStructure
import com.david.mocassin.model.DataStructureEnum
import com.david.mocassin.model.c_components.c_enum.Cenum
import com.david.mocassin.model.c_components.c_struct.CuserStructure
import com.david.mocassin.model.c_components.c_union.Cunion
import com.david.mocassin.utils.readStringFromBinaryFile
import com.david.mocassin.view.MainView
import com.david.mocassin.view.components.MainMenuBar
import com.david.mocassin.view.components.sidebar_drawers.LeftSideDrawer
import com.david.mocassin.view.components.wizards.generated_structures_wizards.slist_wizard.SlistWizard
import com.david.mocassin.view.components.wizards.user_structures_wizards.enum_wizard.EnumWizard
import com.david.mocassin.view.components.wizards.user_structures_wizards.struct_wizard.StructWizard
import com.david.mocassin.view.components.wizards.user_structures_wizards.union_wizard.UnionWizard
import edu.upm.david.security.gamalcrypt.cryptography.Cryptorithm
import javafx.stage.FileChooser
import tornadofx.*
import java.nio.file.Path
import javax.json.JsonObject


class MainMenuBarController : Controller() {
    private val mainMenuBar: MainMenuBar by inject()
    private val mainView: MainView by inject()
    val projectController: ProjectController by inject()
    val leftSideDrawer: LeftSideDrawer by inject()

    val enumIcon = resources.imageview("/icons/enum32.png")
    val unionIcon = resources.imageview("/icons/union32.png")
    val structIcon = resources.imageview("/icons/struct32.png")

    val slistIcon = resources.imageview("/icons/slist32.png")
    val dlistIcon = resources.imageview("/icons/dlist32.png")
    val btreeIcon = resources.imageview("/icons/btree32.png")
    val treeIcon = resources.imageview("/icons/tree32.png")

    fun enableSlist() {
        mainMenuBar.addSlistItem.isDisable = false
    }

    fun openFromComputer() {
        val ef = arrayOf(FileChooser.ExtensionFilter("Mocassin file (*.moc)", "*.moc"))
        chooseFile(
            mainView.messages["select_file_open"],
            ef,
            FileChooserMode.Single
        ).let { file ->
            if (file.isNotEmpty()) {
                val packageName = file.first().name.split(".").first()
                projectController.name = packageName
                leftSideDrawer.controller.packageName.value = packageName
                mainView.title = mainView.defaultTitle + " [${packageName}]"

                val content = Cryptorithm.decrypt(readStringFromBinaryFile(file.component1()))

                val inputObject: JsonObject = loadJsonObject(content)
                projectController.updateModel(inputObject)
            }
        }
    }

    fun saveProjectLocally() {
        if (projectController.userModel.isEmpty().not()) {
            val directory = chooseDirectory(mainView.messages["select_dir"])
            if (directory != null) {
                projectController.saveToMocFile(directory.toString())
            }
        } else {
            warning(mainView.messages["project_empty_header"], mainView.messages["project_empty_content"])
        }
    }

    fun generateProject() {
        if (!projectController.userDataStructures.isEmpty()) {
            val directory = chooseDirectory(mainView.messages["output_dir"])
            if (directory != null) {
                projectController.generate(directory.toString())
            }
        } else {
            warning(mainView.messages["no_ds_created_header"], mainView.messages["no_ds_created_content"])
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
            // save the enumeration
            projectController.userModel.add(enumWizard.enumModel.item)
            // update tree view of user's structures
            leftSideDrawer.controller.addLastEnumNode(leftSideDrawer.enumRoot)

            information(
                mainView.messages["enum_added"],
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
            // update tree view of user's structures
            leftSideDrawer.controller.addLastUnionNode(leftSideDrawer.unionRoot)

            information(
                mainView.messages["union_added"],
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
            // update tree view of user's structures
            leftSideDrawer.controller.addLastStructNode(leftSideDrawer.structRoot)

            information(
                mainView.messages["struct_added"],
                structWizard.structModel.item.toJSON().toString()
            )

            structWizard.structModel.item =
                CuserStructure("")
            structWizard.structModel.attributes.value.clear()
            structWizard.close()
        }
    }

    fun newSlist() {
        val slistWizard = SlistWizard()
        slistWizard.openModal()
        slistWizard.onComplete {
            projectController.userDataStructures.add(slistWizard.slistModel.item)
            // update tree vies
            leftSideDrawer.controller.addLastSlistNode(leftSideDrawer.slistRoot)
            // disable add slist
            mainMenuBar.addSlistItem.isDisable = true

            information(
                mainView.messages["slist_added"],
                slistWizard.slistModel.item.toJSON().toString()
            )

            slistWizard.slistModel.item = DataStructure(projectController.userModel, DataStructureEnum.SLIST)
            slistWizard.slistModel.userVariables.value.clear()
            slistWizard.close()
        }
    }
}