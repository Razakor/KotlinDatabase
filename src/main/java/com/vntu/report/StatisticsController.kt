package com.vntu.report

import com.vntu.main.QueryResult
import javafx.collections.ObservableList
import javafx.scene.control.TableView
import java.sql.SQLException

class StatisticsController {
    lateinit var bachelorTable: TableView<ObservableList<String>>
    lateinit var juniorSpecialistTable: TableView<ObservableList<String>>

    @Throws(SQLException::class)
    fun initialize() {

        var query = "SELECT student_b.short_name, student_b.quantity, distribution_b.quantity, distribution_b.quantity * 100 / student_b.quantity, student_b.quantity - distribution_b.quantity\n" +
                    "FROM student_b\n" +
                    "LEFT JOIN distribution_b ON student_b.short_name = distribution_b.short_name"

        QueryResult.getTableResult(bachelorTable, query)

        query = "SELECT student_ms.short_name, student_ms.quantity, distribution_ms.quantity, distribution_ms.quantity * 100 / student_ms.quantity, student_ms.quantity - distribution_ms.quantity\n" +
                    "FROM student_ms\n" +
                    "LEFT JOIN distribution_ms ON student_ms.short_name = distribution_ms.short_name"

        QueryResult.getTableResult(juniorSpecialistTable, query)
    }
}
