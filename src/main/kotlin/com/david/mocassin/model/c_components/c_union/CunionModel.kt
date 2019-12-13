package com.david.mocassin.model.c_components.c_union

import tornadofx.ItemViewModel

class CunionModel: ItemViewModel<Cunion>() {
    val name = bind(Cunion::nameProperty, autocommit = true)
    val attributes = bind(Cunion::attributesProperty, autocommit = true)
}