package com.vntu.preparation

import com.vntu.main.QueryResult
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import java.sql.Date
import java.sql.SQLException

class StartSController {
    lateinit var startDatePicker: DatePicker
    lateinit var endDatePicker: DatePicker
    private lateinit var startDate: Date
    private lateinit var endDate: Date
    private lateinit var label: Label

    @Throws(SQLException::class)
    fun initialize() {
        val query = "SELECT start, end FROM timings"
        val dateList = QueryResult.getListResult(query, false)
        startDate = Date.valueOf(dateList[0].toString())
        endDate = Date.valueOf(dateList[1].toString())
        startDatePicker.value = startDate.toLocalDate()
        endDatePicker.value = endDate.toLocalDate()
    }

    fun initData(label: Label) {
        this.label = label
    }

    @Throws(SQLException::class)
    fun save() {
        startDate = Date.valueOf(startDatePicker.value)
        endDate = Date.valueOf(endDatePicker.value)
        val query = "UPDATE timings\n" +
                "SET start = '$startDate', end = '$endDate'\n" +
                "WHERE id = '1'"
        QueryResult.updateDataBase(query)
        label.text = "Робота на виробництві починається: $startDate, закінчується: $endDate."
    }

    fun deleteAllStudents() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Видалення студентів"
        alert.headerText = "Видалити всіх студентів?"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            QueryResult.updateDataBase("DELETE FROM student")
            QueryResult.updateDataBase("DELETE FROM distribution")
            QueryResult.updateDataBase("DELETE FROM `group`")
        }
    }

    fun nextCourse() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.title = "Переведення студентів"
        alert.headerText = "Перевести всіх студентів на наступний курс"
        val result = alert.showAndWait()
        if (result.get() == ButtonType.OK) {
            val query = "UPDATE student\n" +
                    "SET id_course = id_course + 1"
            QueryResult.updateDataBase(query)
        }
    }
}
