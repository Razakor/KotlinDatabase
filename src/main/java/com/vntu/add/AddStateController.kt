package com.vntu.add

import com.vntu.directories.EnterpriseStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage

import java.sql.SQLException

class AddStateController {
    var closeButton: Button? = null
    var nameTextField: TextField? = null
    private var enterpriseStateController: EnterpriseStateController? = null
    private var idCountry: String? = null

    @Throws(SQLException::class)
    fun initData(enterpriseStateController: EnterpriseStateController, countryName: String) {

        this.enterpriseStateController = enterpriseStateController

        idCountry =
            QueryResult.getListResult("SELECT id FROM country WHERE name = '$countryName'", false).get(0).toString()

    }

    @Throws(SQLException::class)
    fun addStateQuery() {

        val state_name = Parser.processQuote(nameTextField!!.text)

        val query = "INSERT INTO state(name, id_country)\n" +
                "VALUES ('" + state_name + "'," + idCountry + ")"

        QueryResult.updateDataBase(query)
        nameTextField!!.clear()
        enterpriseStateController!!.setStateComboBox()
    }

    fun cancel() {
        val stage = closeButton!!.scene.window as Stage
        stage.close()
    }
}
