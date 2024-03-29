package com.vntu.edit

import com.vntu.directories.EnterpriseStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class EditCityController {
    lateinit var nameTextField: TextField
    lateinit var closeButton: Button
    lateinit var indexTextField: TextField
    private lateinit var oldName: String
    private lateinit var enterpriseStateController: EnterpriseStateController

    @Throws(SQLException::class)
    fun initData(enterpriseStateController: EnterpriseStateController, name: String) {
        nameTextField.text = name
        oldName = Parser.processQuote(name)
        this.enterpriseStateController = enterpriseStateController
        indexTextField.text = Parser.processBrackets(QueryResult.getListResult("SELECT `index` FROM city WHERE name = '$oldName'", false).toString())
    }

    @Throws(SQLException::class)
    fun editCityQuery() {
        val name = Parser.processQuote(nameTextField.text)
        val index = indexTextField.text
        val query = "UPDATE city\n" +
                "SET name = '$name', `index` = '$index'\n" +
                "WHERE name = '$oldName'"
        QueryResult.updateDataBase(query)
        enterpriseStateController.setCityComboBox()
        val stage = closeButton.scene.window as Stage
        stage.close()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }
}