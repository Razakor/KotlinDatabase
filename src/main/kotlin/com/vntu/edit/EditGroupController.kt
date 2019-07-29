package com.vntu.edit

import com.vntu.directories.StudentStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class EditGroupController {
    lateinit var nameTextField: TextField
    lateinit var closeButton: Button
    private lateinit var oldName: String
    private lateinit var studentStateController: StudentStateController

    fun initData(studentStateController: StudentStateController, groupName: String) {
        nameTextField.text = groupName
        oldName = groupName
        this.studentStateController = studentStateController
    }

    @Throws(SQLException::class)
    fun editGroupQuery() {
        val name = Parser.processQuote(nameTextField.text)
        val query = "UPDATE `group` SET name = '$name' WHERE name = '$oldName'"
        QueryResult.updateDataBase(query)
        studentStateController.setGroup()
        val stage = closeButton.scene.window as Stage
        stage.close()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }
}
