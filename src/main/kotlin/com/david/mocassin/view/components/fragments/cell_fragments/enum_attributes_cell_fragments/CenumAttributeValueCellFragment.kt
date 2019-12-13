package com.david.mocassin.view.components.fragments.cell_fragments.enum_attributes_cell_fragments

import com.david.mocassin.model.c_components.c_enum.CenumAttribute
import com.david.mocassin.model.c_components.c_enum.CenumAttributeModel
import tornadofx.*

class CenumAttributeValueCellFragment: TableCellFragment<CenumAttribute, Int>() {
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