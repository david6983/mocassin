package com.david.mocassin.view

import javafx.scene.image.ImageView
import tornadofx.*

class ImageResizableTestView: View("ImageResizable test") {
    var img: ImageView by singleAssign()

    override val root = vbox {
        button("width").action {
            println(widthProperty().value)
        }
        imageview("icons/mocassin.png") {
            img = this
            scaleX = .50
            scaleY = .50
            //isPreserveRatio = true
        }
        widthProperty().onChange {
            img.fitWidth = it
        }

        heightProperty().onChange {
            img.fitHeight = it
        }
    }
}

class ImageResizableTestApp : App(ImageResizableTestView::class)

fun main(args: Array<String>) {
    launch<ImageResizableTestApp>(args)
}