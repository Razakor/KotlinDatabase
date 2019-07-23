package com.vntu.edit

import com.vntu.directories.StudentStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.event.ActionEvent
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class EditInstController {

    var closeButton: Button? = null
    var shortNameTextField: TextField? = null
    var nameTextField: TextField? = null
    private var oldShortName: String? = null
    private var studentStateController: StudentStateController? = null

    fun initData(studentStateController: StudentStateController) {
        shortNameTextField!!.text = studentStateController.instituteComboBox!!.getValue()
        oldShortName = shortNameTextField!!.text
        this.studentStateController = studentStateController
        try {
            nameTextField!!.text = QueryResult.getListResult(
                "SELECT name FROM institute WHERE short_name = '" + shortNameTextField!!.text + "'",
                false
            ).get(0).toString()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    @Throws(SQLException::class)
    fun editInstQuery() {
        val name = Parser.processQuote(nameTextField!!.text)
        val query = "UPDATE institute\n" +
                "SET short_name = '" + shortNameTextField!!.text + "', name = '" + name + "'\n" +
                "WHERE short_name = '" + oldShortName + "'"
        QueryResult.updateDataBase(query)
        studentStateController!!.setInstitute()
        val stage = closeButton!!.scene.window as Stage
        stage.close()
    }

    fun cancel() {
        val stage = closeButton!!.scene.window as Stage
        stage.close()
    }
}