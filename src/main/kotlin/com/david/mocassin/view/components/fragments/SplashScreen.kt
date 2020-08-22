package com.david.mocassin.view.components.fragments

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import tornadofx.*
import kotlin.concurrent.thread

class SplashScreen : View() {
    private val titleFont = loadFont("/fonts/GloriaHallelujah-Regular.ttf", 32)
    private val subtitleFont = loadFont("/fonts/GloriaHallelujah-Regular.ttf", 22)

    override val root = stackpane {
        setPrefSize(556.0, 250.0)
        imageview("icons/mocassin.png") {
            scaleX = .25
            scaleY = .25
        }

        label(messages["title_1"]) {
            font = titleFont
            textFill = Color.WHITE
            paddingTop = 165.0
        }

        label(messages["title_2"]) {
            font = subtitleFont
            textFill = Color.WHITE
            paddingTop = 225.0
        }

        style {
            backgroundColor += Color.TRANSPARENT
            fontWeight = FontWeight.BOLD
        }
    }

    override fun onDock() {
        thread {
            currentStage?.scene?.fill = null
            println(titleFont)
            Thread.sleep(2000)
            runLater {
                close()
                primaryStage.show()
            }
        }
    }
}