package com.vntu.add

import com.vntu.directories.StudentStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class AddInstController {
    lateinit var closeButton: Button
    lateinit var nameTextField: TextField
    lateinit var shortNameTextField: TextField
    private lateinit var studentStateController: StudentStateController

    fun initData(studentStateController: StudentStateController) {
        this.studentStateController = studentStateController
    }

    @Throws(SQLException::class)
    fun addInstQuery() {
        val shortName = shortNameTextField.text
        val name = Parser.processQuote(nameTextField.text)
        val query = "INSERT INTO institute(name, short_name)\n" +
                "VALUES ('$name', '$shortName')"
        QueryResult.updateDataBase(query)
        shortNameTextField.clear()
        nameTextField.clear()
        studentStateController.setInstituteComboBox()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }
}