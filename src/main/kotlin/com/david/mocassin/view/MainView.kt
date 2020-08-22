package com.david.mocassin.view

import com.david.mocassin.controller.MainViewController
import com.david.mocassin.view.components.sidebar_drawers.LeftSideDrawer
import com.david.mocassin.view.components.MainMenuBar
import com.david.mocassin.view.styles.MainStyle
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.TabPane
import javafx.scene.image.ImageView

import tornadofx.*

class MainView: View("") {
    val controller : MainViewController by inject()
    var centerTabPane: TabPane by singleAssign()
    val defaultTitle = messages["title"]
    var logo: ImageView by singleAssign()

    init {
        title = "$defaultTitle [${controller.projectController.name}]"

        FX.localeProperty().onChange {
            information("${messages["language_change_header"]} ${FX.locale.displayLanguage}", messages["language_change_content"])
        }
    }

    fun updatePackageNameInTitle(name: String) {
        title = title.removeRange(title.indexOf("["), title.indexOf("]") + 1)
        title += " [${name}]"
    }

    override val root = borderpane {
        left<LeftSideDrawer>()
        top<MainMenuBar>()
        right {}
        bottom {
            toolbar {
                button(messages["btb_generate"]) {
                    addClass("btn-primary", "btn-lg")
                }.action {
                    controller.mainMenuBarController.generateProject()
                }
                button(messages["btb_save"]) {
                    addClass("btn-info", "btn-lg")
                }.action {
                    controller.mainMenuBarController.saveProjectLocally()
                }
                button(messages["btb_open"]) {
                    addClass("btn-warning", "btn-lg")
                }.action {
                    controller.mainMenuBarController.openFromComputer()
                }
                style {
                    alignment = Pos.CENTER
                }
            }
        }
        center {

        }
    }

    init {
        with (root) {
            prefWidth = 1000.0
            prefHeight = 800.0

            addClass(MainStyle.mainView)
        }
    }

    override fun onDock() {
        currentWindow?.setOnCloseRequest { event ->
            alert(Alert.AlertType.CONFIRMATION,
                messages["quit_header"],
                messages["quit_content"]
            ).let {
                when {
                    it.result.buttonData.isCancelButton -> event.consume()
                }
            }
        }
    }
}