package com.vntu.edit

import com.vntu.directories.EnterpriseStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage

import java.sql.SQLException

class EditStateController {
    var nameTextField: TextField? = null
    var closeButton: Button? = null
    private var oldName: String? = null
    private var enterpriseStateController: EnterpriseStateController? = null


    fun initData(enterpriseStateController: EnterpriseStateController, name: String) {
        nameTextField!!.text = name
        oldName = Parser.processQuote(name)
        this.enterpriseStateController = enterpriseStateController

    }

    @Throws(SQLException::class)
    fun editStateQuery() {
        val name = Parser.processQuote(nameTextField!!.text)
        val query = "UPDATE state SET name = '$name' WHERE name = '$oldName'"
        QueryResult.updateDataBase(query)
        enterpriseStateController!!.setStateComboBox()
        val stage = closeButton!!.scene.window as Stage
        stage.close()
    }

    fun cancel() {
        val stage = closeButton!!.scene.window as Stage
        stage.close()
    }
}
