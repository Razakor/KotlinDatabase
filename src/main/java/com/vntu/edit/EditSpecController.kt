package com.vntu.edit

import com.vntu.main.QueryResult
import javafx.collections.ObservableList
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class EditSpecController {

    lateinit var textField: TextField
    lateinit var closeButton: Button
    private lateinit var resultTable: TableView<ObservableList<String>>
    private lateinit var oldName: String

    fun initData(resultTable: TableView<ObservableList<String>>, data: String) {
        textField.text = data
        oldName = data
        this.resultTable = resultTable
    }

    @Throws(SQLException::class)
    fun editSpecQuery() {
        var query = "UPDATE speciality SET name = '${textField.text}' WHERE name = '$oldName'"
        QueryResult.updateDataBase(query)
        query = "SELECT speciality.name FROM speciality"
        resultTable = QueryResult.getTableResult(resultTable, query)
        val stage = closeButton.scene.window as Stage
        stage.close()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }
}
