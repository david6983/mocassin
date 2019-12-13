package com.david.mocassin.model.user_model

import tornadofx.ItemViewModel

class UserModelViewModel: ItemViewModel<UserModel>() {
    val packageName = bind(UserModel::packageNameProperty)
    val userStructureList = bind(UserModel::userStructureListProperty)
    val userEnumList = bind(UserModel::userEnumListProperty)
    val userUnionList = bind(UserModel::userUnionListProperty)
}