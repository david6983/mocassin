package com.david.mocassin.view

import tornadofx.*

class TranslationTestView: View("translation test") {

    override val root = vbox {
        text("hello")
        text("world")
    }
}

class TranslationTestApp : App(TranslationTestView::class)

fun main(args: Array<String>) {
    launch<TranslationTestApp>(args)
}