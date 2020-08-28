package com.david.mocassin.view.components

import com.david.mocassin.controller.MainMenuBarController
import com.david.mocassin.view.components.fragments.AboutModal
import com.david.mocassin.view.components.fragments.ChangeLanguageModal
import com.david.mocassin.view.components.fragments.ChangeNameModal
import com.david.mocassin.view.components.fragments.NewProjectModal
import javafx.scene.control.MenuItem
import javafx.scene.paint.Color
import javafx.stage.StageStyle
import tornadofx.*
import java.time.LocalDate

class MainMenuBar : View() {
    private val controller: MainMenuBarController by inject()

    var addSlistItem: MenuItem by singleAssign()

    private fun newProject() {
        find<NewProjectModal>().openModal(stageStyle = StageStyle.UTILITY)
    }

    private fun changeName() {
        find<ChangeNameModal>().openModal(stageStyle = StageStyle.UTILITY)
    }

    private fun changeLanguage() {
        find<ChangeLanguageModal>().openModal(stageStyle = StageStyle.UTILITY)
    }

    private fun about() {
        find<AboutModal>().openModal(stageStyle = StageStyle.UTILITY)
    }

    override val root = menubar {
        menu(messages["mb_file"]) {
            item(messages["mb_new_project"], keyCombination = "Shortcut+N") {
                graphic = hbox {
                    rectangle {
                        fill = Color.GREEN
                        width = 8.0
                        height = 32.0
                    }
                    imageview(resources["/icons/file32.png"])
                }
            }.action { newProject() }
            separator()
            item(messages["mb_open_from"], keyCombination = "Shortcut+O") {
                graphic = hbox {
                    rectangle {
                        fill = Color.CORAL
                        width = 8.0
                        height = 32.0
                    }
                    imageview(resources["/icons/open32.png"])
                }
            }.action { controller.openFromComputer() }
            item(messages["mb_save_project"], keyCombination = "Shortcut+S") {
                graphic = hbox {
                    rectangle {
                        fill = Color.YELLOW
                        width = 8.0
                        height = 32.0
                    }
                    imageview(resources["/icons/enum32.png"])
                }
            }.action { controller.saveProjectLocally() }
            separator()
            item(messages["mb_generate_c"], keyCombination = "Shortcut+G") {
                graphic = hbox {
                    rectangle {
                        fill = Color.rgb(57 ,73 ,171)
                        width = 8.0
                        height = 32.0
                    }
                    imageview(resources["/icons/generate32.png"])
                }
            }.action {
                controller.generateProject()
            }
            separator()
            item(messages["mb_save_web"], keyCombination = "Shortcut+W").isDisable = true
            item(messages["mb_import_web"], keyCombination = "Shortcut+I").isDisable = true
            separator()
            item(messages["mb_test"], keyCombination = "Shortcut+X").isDisable = true
        }
        menu(messages["mb_generate"]) {
            item(messages["mb_slist"], keyCombination = "Shortcut+L", graphic = controller.slistIcon){
                addSlistItem = this
            }.action {
                controller.newSlist()
            }
            item(
                messages["mb_dlist"],
                keyCombination = "Shortcut+D",
                graphic = controller.dlistIcon
            ).isDisable = true
            separator()
            item(messages["mb_btree"], keyCombination = "Shortcut+B", graphic = controller.btreeIcon).isDisable = true
            item(messages["mb_bstree"], keyCombination = "Shortcut+Y").isDisable = true
            item(messages["mb_tree"], keyCombination = "Shortcut+T", graphic = controller.treeIcon).isDisable =
                true
            item(messages["mb_quadtree"], keyCombination = "Shortcut+Q").isDisable =
                true
            item(messages["mb_rtree"], keyCombination = "Shortcut+R").isDisable =
                true
            separator()
            item(messages["mb_graph"], keyCombination = "Shortcut+P").isDisable = true
            separator()
            item(messages["mb_hashtable"], keyCombination = "Shortcut+H").isDisable = true
        }
        menu(messages["mb_new"]) {
            item("Enum", keyCombination = "Shortcut+E", graphic = controller.enumIcon).action {
                controller.newEnum()
            }
            item("Union", keyCombination = "Shortcut+U", graphic = controller.unionIcon).action {
                controller.newUnion()
            }
            item("Struct", keyCombination = "Shortcut+R", graphic = controller.structIcon).action {
                controller.newStruct()
            }
        }
        menu(messages["mb_pref"]) {
            item(messages["mb_package_name"]).action { changeName() }
            item(messages["change_language"]).action { changeLanguage() }
        }
        menu(messages["mb_help"]) {
            item(messages["mb_doc"])
            separator()
            item(messages["mb_about"]).action { about() }
        }
    }
}