package com.vntu.add

import com.vntu.directories.StudentStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage

import java.sql.SQLException

class AddGroupController {
    lateinit var closeButton: Button
    lateinit var nameTextField: TextField
    private lateinit var studentStateController: StudentStateController
    private lateinit var idInstitute: String
    private lateinit var courseName: String

    @Throws(SQLException::class)
    fun initData(studentStateController: StudentStateController, instituteName: String, courseName: String) {
        this.studentStateController = studentStateController
        this.courseName = courseName
        idInstitute = QueryResult.getListResult("SELECT id FROM institute WHERE short_name = '$instituteName'", false)[0].toString()
    }

    @Throws(SQLException::class)
    fun addGroupQuery() {
        val name = Parser.processQuote(nameTextField.text)
        val query = "INSERT INTO `group` (name, id_institute, course) VALUES ('$name', '$idInstitute', '$courseName')"
        QueryResult.updateDataBase(query)
        nameTextField.clear()
        studentStateController.setGroupComboBox()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }
}