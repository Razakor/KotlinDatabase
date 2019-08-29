package com.vntu.main

import com.vntu.database.statement
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import java.sql.*

object QueryResult {
    private const val CHOOSE_ALL = "Всі"

    @Throws(SQLException::class)
    fun updateDataBase(query: String) {
        statement.executeUpdate(query)
        statement.close()
    }

    @Throws(SQLException::class)
    fun getListResult(query: String, withChooseAll: Boolean): ObservableList<String> {
        val strings = FXCollections.observableArrayList<String>()
        val resultSet = statement.executeQuery(query)

        if (withChooseAll) strings.add(CHOOSE_ALL)

        while (resultSet.next()) {
            for (i in 1..resultSet.metaData.columnCount) {
                if (resultSet.getString(i) == null) {
                    strings.add("")
                } else {
                    val s = resultSet.getString(i)
                    strings.add(s)
                }
            }
        }

        resultSet.close()
        statement.close()

        return strings
    }

    @Throws(SQLException::class)
    fun getTableResult(resultTable: TableView<ObservableList<String>>, query: String): TableView<ObservableList<String>> {

        val rows: ObservableList<ObservableList<String>> = FXCollections.observableArrayList()
        val resultSet = statement.executeQuery(query)

        for (i in 0 until resultSet.metaData.columnCount) {
            val col: TableColumn<ObservableList<String>, String> = resultTable.columns[i]
                    as TableColumn<ObservableList<String>, String>
            col.setCellValueFactory { SimpleStringProperty(it.value[i].toString()) }
            resultTable.columns[i] = col
        }

        while (resultSet.next()) {
            val row = FXCollections.observableArrayList<String>()
            for (i in 1..resultSet.metaData.columnCount) {
                if (resultSet.getString(i) == null) {
                    row.add("")
                } else {
                    row.add(resultSet.getString(i))
                }
            }
            rows.add(row)
        }

        resultSet.close()
        statement.close()
        resultTable.items = rows

        return resultTable
    }

    @Throws(SQLException::class)
    fun getTableAsList(query: String): ObservableList<ObservableList<String>> {

        val rows: ObservableList<ObservableList<String>> = FXCollections.observableArrayList()
        val resultSet = statement.executeQuery(query)

        while (resultSet.next()) {
            val row = FXCollections.observableArrayList<String>()
            for (i in 1..resultSet.metaData.columnCount) {
                if (resultSet.getString(i) == null) {
                    row.add("")
                } else {
                    row.add(resultSet.getString(i))
                }
            }
            rows.add(row)
        }

        resultSet.close()
        statement.close()

        return rows
    }

    @Throws(SQLException::class)
    fun getTable(resultTable: TableView<ObservableList<String>>, query: String): TableView<ObservableList<String>> {

        val resultSet = statement.executeQuery(query)

        for (i in 0 until resultSet.metaData.columnCount) {
            val col: TableColumn<ObservableList<String>, String> = resultTable.columns[i]
                    as TableColumn<ObservableList<String>, String>
            col.setCellValueFactory { SimpleStringProperty(it.value[i].toString()) }
            resultTable.columns[i] = col
        }

        resultSet.close()
        statement.close()

        return resultTable
    }
}