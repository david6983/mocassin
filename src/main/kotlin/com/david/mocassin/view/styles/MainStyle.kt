package com.david.mocassin.view.styles

import tornadofx.*

class MainStyle : Stylesheet() {
    companion object {
        private val mainFontSize = 14.px
        private val mainFontFamily = "Segoe UI"

        val mainView by cssclass()
    }

    init {
        mainView {
            fontSize = mainFontSize
            fontFamily = mainFontFamily
        }
    }
}