package com.david.mocassin.view.components.wizards.editors

import com.david.mocassin.model.c_components.CenumAttribute
import com.david.mocassin.model.c_components.CenumAttributeModel
import tornadofx.*

class CenumAttributeValueEditor: TableCellFragment<CenumAttribute, Int>() {
    // Bind our ItemModel to the rowItemProperty, which points to the current Item
    val model = CenumAttributeModel().bindToRowItem(this)

    override val root = hbox(spacing = 10) {
        button("-"){
            removeWhen(!editingProperty)
        }.action {
            model.value.value = model.value.value.toInt() - 1
        }
        label(model.value)
        button("+"){
            removeWhen(!editingProperty)
        }.action {
            model.value.value = model.value.value.toInt() + 1
        }
    }

    // Make sure we rollback our model to avoid showing the last failed edit
    override fun startEdit() {
        model.rollback()
    }


}