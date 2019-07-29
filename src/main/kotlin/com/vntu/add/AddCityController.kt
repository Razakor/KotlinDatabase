package com.vntu.add

import com.vntu.directories.EnterpriseStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class AddCityController {
    lateinit var closeButton: Button
    lateinit var nameTextField: TextField
    lateinit var indexTextField: TextField
    private lateinit var enterpriseStateController: EnterpriseStateController
    private lateinit var idRegion: String

    @Throws(SQLException::class)
    fun initData(enterpriseStateController: EnterpriseStateController, regionName: String) {
        this.enterpriseStateController = enterpriseStateController
        idRegion = QueryResult.getListResult("SELECT id FROM region WHERE name = '$regionName'", false)[0].toString()
    }

    @Throws(SQLException::class)
    fun addCityQuery() {
        val name = Parser.processQuote(nameTextField.text)
        val index = Parser.processQuote(indexTextField.text)
        val query = "INSERT INTO city(name, id_region, `index`)\n" +
                "VALUES ('$name', '$idRegion', '$index')"
        QueryResult.updateDataBase(query)
        nameTextField.clear()
        indexTextField.clear()
        enterpriseStateController.setCityComboBox()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }
}