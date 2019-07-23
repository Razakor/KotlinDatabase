package com.vntu.main

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.control.ComboBox
import javafx.scene.control.Tooltip
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

class ComboBoxAutoComplete (private val cmb: ComboBox<String>) {
    private var filter = ""
    private val originalItems: ObservableList<String> = FXCollections.observableArrayList(cmb.items)

    init {
        cmb.tooltip = Tooltip()
        cmb.onKeyPressed = EventHandler<KeyEvent> { this.handleOnKeyPressed(it) }
        cmb.onHidden = EventHandler<Event> { this.handleOnHiding() }
    }

    private fun handleOnKeyPressed(e: KeyEvent) {
        var filteredList = FXCollections.observableArrayList<String>()
        val code = e.code

        when {
            code.isLetterKey -> {
                filter += e.text
            }
            code == KeyCode.BACK_SPACE && filter.isNotEmpty() -> {
                filter = filter.substring(0, filter.length - 1)
                cmb.items.setAll(originalItems)
            }
            code == KeyCode.ESCAPE -> {
                filter = ""
            }
            filter.isEmpty() -> {
                filteredList = originalItems
                cmb.tooltip.hide()
            }
            else -> {
                val items = cmb.items.stream()
                val txtUsr = filter.toLowerCase()
                items.filter { el -> el.toString().toLowerCase().contains(txtUsr) }
                    .forEach { filteredList.add(it) }
                cmb.tooltip.text = txtUsr
                val stage = cmb.scene.window
                val posX = stage.x + cmb.boundsInParent.minX
                val posY = stage.y + cmb.boundsInParent.minY
                cmb.tooltip.show(stage, posX, posY)
                cmb.show()
            }
        }
        cmb.items.setAll(filteredList)
    }

    private fun handleOnHiding() {
        filter = ""
        cmb.tooltip.hide()
        val s = cmb.selectionModel.selectedItem
        cmb.items.setAll(originalItems)
        cmb.selectionModel.select(s)
    }
}
