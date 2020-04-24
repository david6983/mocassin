package com.david.mocassin.model

import tornadofx.ItemViewModel

class DataStructureModel: ItemViewModel<DataStructure>() {
    val userVariables = bind(DataStructure::userVariablesProperty, autocommit = true)
    val userModel = bind(DataStructure::userModelProperty, autocommit = true)
}