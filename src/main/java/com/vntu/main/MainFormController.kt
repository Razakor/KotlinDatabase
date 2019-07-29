package com.vntu.main

import com.vntu.distribution.ReferralController
import com.vntu.preparation.StartSController
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.stage.Stage
import java.io.IOException
import java.sql.Date
import java.sql.SQLException
import java.util.Objects
import java.time.format.DateTimeFormatter

class MainFormController {
    lateinit var titleLabel: Label
    lateinit var bachelorLabel: Label
    lateinit var msLabel: Label
    private val window = OpenNewWindow()

    @Throws(SQLException::class)
    fun initialize() {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var query = "SELECT start, end FROM timings"
        val dateList = QueryResult.getListResult(query, false)
        val startDate = Date.valueOf(dateList[0]).toLocalDate().format(formatter).toString()
        val endDate = Date.valueOf(dateList[1]).toLocalDate().format(formatter).toString()
        titleLabel.text = "Робочий триместр проходитимуть студенти 3-го курсу та молодші спеціалісти."
        bachelorLabel.text = "Робота на виробництві в бакалаврів починається: $startDate, закінчується: $endDate."

        query = "SELECT start, end FROM timings_ms"
        val msDateList = QueryResult.getListResult(query, false)
        val msStartDate = Date.valueOf(msDateList[0]).toLocalDate().format(formatter).toString()
        val msEndDate = Date.valueOf(msDateList[1]).toLocalDate().format(formatter).toString()
        msLabel.text = "Робота на виробництві в молодших спеціалістів починається: $msStartDate, закінчується: $msEndDate."
    }

    @Throws(IOException::class)
    fun openStudentState() {
        val fxmlLoader =
            FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/student_state.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Довідник студентів"
        stage.scene = Scene(root)
        stage.isMaximized = true
        stage.show()
    }

    @Throws(IOException::class)
    fun openEnterpriseState() {
        val fxmlLoader =
            FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/enterprise_state.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Довідник підприємств"
        stage.scene = Scene(root)
        stage.isMaximized = true
        stage.show()

    }

    @Throws(IOException::class)
    fun openWorkStudState() {
        window.openWindow("fxml/work_stud_state.fxml", "Довідник робочих спеціальностей")
    }

    @Throws(IOException::class)
    fun openWorkVntu() {
        window.openWindow("fxml/work_vntu.fxml", "Довідник підрозділів ВНТУ")
    }

    @Throws(IOException::class)
    fun openStudDistrib() {
        window.openWindow("fxml/stud_distrib.fxml", "Розподіл студентів по підприємствам")
    }

    @Throws(IOException::class)
    fun openReferral() {
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/referral.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Друк направлень"
        stage.scene = Scene(root)
        val controller = fxmlLoader.getController<ReferralController>()
        controller.initData(stage)
        stage.show()
    }

    @Throws(IOException::class)
    fun openStatistics() {
        window.openWindow("fxml/statistics.fxml", "Загальна довідка про розподіл студентів")
    }

    @Throws(IOException::class)
    fun openStateOfAffairs() {
        window.openWindow("fxml/state_of_affairs.fxml", "Довідка про стан справ")
    }

    @Throws(IOException::class)
    fun openMoreThanTwoStud() {
        window.openWindow("fxml/more_than_two_stud.fxml", "")
    }

    @Throws(IOException::class)
    fun openStartS() {
        val fxmlLoader = FXMLLoader(Objects.requireNonNull(javaClass.classLoader.getResource("fxml/start_s.fxml")))
        val root = fxmlLoader.load<Parent>()
        val stage = Stage()
        stage.title = "Підготовка до літнього триместру"
        stage.scene = Scene(root)
        val controller = fxmlLoader.getController<StartSController>()
        controller.initData(bachelorLabel, msLabel)
        stage.show()
    }

}
