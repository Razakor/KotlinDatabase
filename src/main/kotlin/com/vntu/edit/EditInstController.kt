package com.vntu.edit

import com.vntu.directories.StudentStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class EditInstController {
    lateinit var closeButton: Button
    lateinit var shortNameTextField: TextField
    lateinit var nameTextField: TextField
    private lateinit var oldShortName: String
    private lateinit var studentStateController: StudentStateController

    @Throws(SQLException::class)
    fun initData(studentStateController: StudentStateController) {
        shortNameTextField.text = studentStateController.instituteComboBox.value
        oldShortName = shortNameTextField.text
        this.studentStateController = studentStateController
        nameTextField.text = QueryResult.getListResult("SELECT name FROM institute WHERE short_name = '" + shortNameTextField.text + "'", false)[0].toString()
    }

    @Throws(SQLException::class)
    fun editInstQuery() {
        val name = Parser.processQuote(nameTextField.text)
        val query = "UPDATE institute\n" +
                    "SET short_name = '${shortNameTextField.text}', name = '$name'\n" +
                    "WHERE short_name = '$oldShortName'"
        QueryResult.updateDataBase(query)
        studentStateController.setInstitute()
        val stage = closeButton.scene.window as Stage
        stage.close()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }
}