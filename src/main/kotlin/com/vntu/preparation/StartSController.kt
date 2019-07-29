package com.vntu.preparation

import com.vntu.main.QueryResult
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.DatePicker
import javafx.scene.control.Label
import java.sql.Date
import java.sql.SQLException
import java.time.format.DateTimeFormatter

class StartSController {
    lateinit var startDatePicker: DatePicker
    lateinit var endDatePicker: DatePicker
    lateinit var msStartDatePicker: DatePicker
    lateinit var msEndDatePicker: DatePicker
    private lateinit var startDate: Date
    private lateinit var endDate: Date
    private lateinit var msStartDate: Date
    private lateinit var msEndDate: Date
    private lateinit var bachelorLabel: Label
    private lateinit var msLabel: Label

    @Throws(SQLException::class)
    fun initialize() {
        var query = "SELECT start, end FROM timings"
        val dateList = QueryResult.getListResult(query, false)
        startDate = Date.valueOf(dateList[0].toString())
        endDate = Date.valueOf(dateList[1].toString())
        startDatePicker.value = startDate.toLocalDate()
        endDatePicker.value = endDate.toLocalDate()

        query = "SELECT start, end FROM timings_ms"
        val msDateList = QueryResult.getListResult(query, false)
        msStartDate = Date.valueOf(msDateList[0].toString())
        msEndDate = Date.valueOf(msDateList[1].toString())
        msStartDatePicker.value = msStartDate.toLocalDate()
        msEndDatePicker.value = msEndDate.toLocalDate()
    }

    fun initData(bachelorLabel: Label, msLabel: Label) {
        this.bachelorLabel = bachelorLabel
        this.msLabel = msLabel
    }

    @Throws(SQLException::class)
    fun save() {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        startDate = Date.valueOf(startDatePicker.value)
        endDate = Date.valueOf(endDatePicker.value)
        var query = "UPDATE timings\n" +
                "SET start = '$startDate', end = '$endDate'\n" +
                "WHERE id = '1'"
        QueryResult.updateDataBase(query)
        val startDateString = startDate.toLocalDate().format(formatter).toString()
        val endDateString =  endDate.toLocalDate().format(formatter).toString()
        bachelorLabel.text = "Робота на виробництві в бакалаврів починається: $startDateString, закінчується: $endDateString."

        msStartDate = Date.valueOf(msStartDatePicker.value)
        msEndDate = Date.valueOf(msEndDatePicker.value)
        query = "UPDATE timings_ms\n" +
                "SET start = '$msStartDate', end = '$msEndDate'\n" +
                "WHERE id = '1'"
        QueryResult.updateDataBase(query)
        val startMsDateString = msStartDate.toLocalDate().format(formatter).toString()
        val endMsDateString =  msEndDate.toLocalDate().format(formatter).toString()
        msLabel.text = "Робота на виробництві в молодших спеціалістів починається: $startMsDateString, закінчується: $endMsDateString."
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
