package com.david.mocassin.model.c_components.c_enum

import tornadofx.ItemViewModel

/**
 * This class is the model of an attribute. This class is used for validation
 */
class CenumAttributeModel: ItemViewModel<CenumAttribute>() {
    val name = bind(CenumAttribute::nameProperty, autocommit = true)
    val value = bind(CenumAttribute::valueProperty, autocommit = true)
}