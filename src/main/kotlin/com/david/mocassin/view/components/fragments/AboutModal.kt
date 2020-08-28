package com.david.mocassin.view.components.fragments

import javafx.scene.image.ImageView
import tornadofx.*
import java.time.LocalDate

class AboutModal: Fragment("") {
    var img: ImageView by singleAssign()

    init {
        title = messages["mb_about"]
    }

    override val root = vbox {
        label(messages["created_by"])

        label("Copyright ©${LocalDate.now().year}")

        label("logo designed by @saiko2b")

        hbox {
            button("Github").action {
                hostServices.showDocument("https://github.com/david6983/mocassin")
            }

            button("Logo designer").action {
                hostServices.showDocument("https://www.instagram.com/saiko2b/")
            }
        }

        prefWidth = 250.0
        prefHeight = 140.0
    }
}