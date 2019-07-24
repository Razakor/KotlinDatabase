package com.vntu.add

import com.vntu.main.QueryResult
import javafx.collections.ObservableList
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class AddVntuDepController {

    lateinit var textField: TextField
    lateinit var closeButton: Button
    private lateinit var resultTable: TableView<ObservableList<String>>

    fun initData(resultTable: TableView<ObservableList<String>>) {
        this.resultTable = resultTable
    }

    @Throws(SQLException::class)
    fun addDepQuery() {
        val result = textField.text
        var query = "INSERT INTO vntu(name)\n" +
                "VALUES ('$result')"
        QueryResult.updateDataBase(query)
        query = "SELECT vntu.name FROM vntu"
        resultTable = QueryResult.getTableResult(resultTable, query)
        textField.clear()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }
}