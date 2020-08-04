package com.david.mocassin.controller

import tornadofx.Controller

class MainViewController : Controller() {
    val projectController: ProjectController by inject()
    val mainMenuBarController: MainMenuBarController by inject()
}