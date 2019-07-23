package com.vntu.main

import com.vntu.distribution.ReferralController
import com.vntu.preparation.StartSController
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.stage.Stage
import java.io.IOException
import java.sql.SQLException
import java.util.Objects

class MainFormController {
    lateinit var firstLabel: Label
    lateinit var secondLabel: Label
    private val window = OpenNewWindow()

    @Throws(SQLException::class)
    fun initialize() {
        val query = "SELECT start, end FROM timings"
        val dateList = QueryResult.getListResult(query, false)
        val startDate = dateList[0].toString()
        val endDate = dateList[1].toString()
        firstLabel.text = "Робочий триместр проходитимуть студенти 3-го курсу"
        secondLabel.text = "Робота на виробництві починається: $startDate, закінчується: $endDate."
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
        controller.initData(secondLabel)
        stage.show()
    }

}
