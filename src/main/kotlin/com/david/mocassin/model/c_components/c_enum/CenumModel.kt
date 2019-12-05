package com.david.mocassin.model.c_components.c_enum

import tornadofx.ItemViewModel

/**
 * Item view model of Cenum with properties bindings
 *
 * Better integration with TableView and TreeView
 */
class CenumModel: ItemViewModel<Cenum>() {
    val name = bind(Cenum::nameProperty, autocommit = true)
    val attributes = bind(Cenum::attributesProperty, autocommit = true)
}