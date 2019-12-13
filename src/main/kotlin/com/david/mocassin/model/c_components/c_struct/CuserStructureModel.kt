package com.david.mocassin.model.c_components.c_struct

import tornadofx.ItemViewModel

class CuserStructureModel: ItemViewModel<CuserStructure>() {
    val name = bind(CuserStructure::nameProperty, autocommit = true)
    val isDisplayFunctionGenerated = bind(CuserStructure::isDisplayFunctionGeneratedProperty, autocommit = true)
    val attributes = bind(CuserStructure::attributesProperty, autocommit = true)
}