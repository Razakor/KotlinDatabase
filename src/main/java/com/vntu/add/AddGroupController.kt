package com.vntu.add

import com.vntu.directories.StudentStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage

import java.sql.SQLException

class AddGroupController {

    var closeButton: Button? = null
    var nameTextField: TextField? = null
    private var studentStateController: StudentStateController? = null
    private var idInstitute: String? = null
    private var courseName: String? = null

    fun initData(studentStateController: StudentStateController, instituteName: String, courseName: String) {

        this.studentStateController = studentStateController
        this.courseName = courseName
        try {
            idInstitute =
                QueryResult.getListResult("SELECT id FROM institute WHERE short_name = '$instituteName'", false).get(0)
                    .toString()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    @Throws(SQLException::class)
    fun addGroupQuery() {
        val name = Parser.processQuote(nameTextField!!.text)
        val query = "INSERT INTO `group` (name, id_institute, course) VALUES ('$name', '$idInstitute', '$courseName')"
        QueryResult.updateDataBase(query)
        nameTextField!!.clear()
        studentStateController!!.setGroupComboBox()

    }

    fun cancel() {
        val stage = closeButton!!.scene.window as Stage
        stage.close()
    }


}
