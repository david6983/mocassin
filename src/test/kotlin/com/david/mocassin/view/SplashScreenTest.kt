package com.david.mocassin.view

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*
import kotlin.concurrent.thread

class SplashScreenTest : View() {
    private val titleFont = loadFont("/fonts/GloriaHallelujah-Regular.ttf", 24)!!

    override val root = stackpane {
        setPrefSize(556.0, 250.0)
        imageview("icons/mocassin.png") {
            scaleX = .25
            scaleY = .25
        }

        label("Mocassin generate data structures in C") {
            font = titleFont
            textFill = Color.WHITE
            paddingTop = 160.0
        }

        style {
            backgroundColor += Color.TRANSPARENT
            fontWeight = FontWeight.BOLD
        }
    }

    override fun onDock() {
        thread {
            currentStage?.scene?.fill = null
            Thread.sleep(2000)
            runLater {
                close()
                primaryStage.show()
            }
        }
    }
}

class MainView : View("Main View") {
    override val root = stackpane {
        setPrefSize(800.0, 600.0)
        label("Welcome")
    }
}

class SplashApp : App(MainView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        find<SplashScreenTest>().openModal(stageStyle = StageStyle.TRANSPARENT)
    }

    override fun shouldShowPrimaryStage() = false
}

fun main(args: Array<String>) {
    launch<SplashApp>(args)
}