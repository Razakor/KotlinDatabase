package com.vntu.add

import com.vntu.main.QueryResult
import javafx.collections.ObservableList
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class AddSpecController {

    lateinit var textField: TextField
    lateinit var closeButton: Button
    private lateinit var resultTable: TableView<ObservableList<String>>

    fun initData(resultTable: TableView<ObservableList<String>>) {
        this.resultTable = resultTable
    }

    @Throws(SQLException::class)
    fun addSpecQuery() {
        val result = textField.text
        var query = "INSERT INTO speciality(name) \n" +
                "VALUES ('$result')"
        QueryResult.updateDataBase(query)
        query = "SELECT speciality.name FROM speciality"
        resultTable = QueryResult.getTableResult(resultTable, query)
        textField.clear()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }
}
