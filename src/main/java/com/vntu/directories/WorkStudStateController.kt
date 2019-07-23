package com.vntu.directories

import com.vntu.add.AddSpecController
import com.vntu.edit.EditSpecController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.collections.ObservableList
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TableView
import javafx.stage.Stage
import java.io.IOException
import java.sql.SQLException
import java.util.*

class WorkStudStateController {

    lateinit var resultTable: TableView<ObservableList<String>>
    private lateinit var name: String

    @Throws(SQLException::class)
    fun initialize() {
        val query = "SELECT speciality.name FROM speciality"
        resultTable = QueryResult.getTableResult(resultTable, query)
        resultTable.selectionModel.selectedItemProperty().addListener { _, _, newSelection ->
            if (newSelection != null) {
                name = resultTable.selectionModel.selectedItem.toString()
            }
        }
    }

    @Throws(IOException::class)
    fun editSpec() {

        name = Parser.processBrackets(name)

        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/edit_spec.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Зміна даних про спеціальності"
        stage.scene = Scene(root)
        val controller = fxmlLoader.getController<EditSpecController>()
        controller.initData(resultTable, name)
        stage.show()
    }

    @Throws(IOException::class)
    fun addSpec() {
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/add_spec.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Додання нових спеціальностей"
        stage.scene = Scene(root)
        val controller = fxmlLoader.getController<AddSpecController>()
        controller.initData(resultTable)
        stage.show()
    }

    @Throws(SQLException::class)
    fun deleteSpec() {
        name = name.substring(1, name.length - 1)
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Видалення запису"
        alert.headerText = "Видалити \"$name\"?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            var query = "DELETE FROM speciality WHERE speciality.name = '$name'"
            QueryResult.updateDataBase(query)
            query = "SELECT speciality.name FROM speciality"
            resultTable = QueryResult.getTableResult(resultTable, query)
        }
    }
}
