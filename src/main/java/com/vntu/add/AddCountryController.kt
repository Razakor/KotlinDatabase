package com.vntu.add

import com.vntu.directories.EnterpriseStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class AddCountryController {

    var closeButton: Button? = null
    var nameTextField: TextField? = null
    private var enterpriseStateController: EnterpriseStateController? = null

    fun initData(enterpriseStateController: EnterpriseStateController) {
        this.enterpriseStateController = enterpriseStateController
    }

    @Throws(SQLException::class)
    fun addCountryQuery() {
        val name = Parser.processQuote(nameTextField!!.text)
        val query = "INSERT INTO country(name)\n" +
                "VALUES ('" + name + "')"
        QueryResult.updateDataBase(query)
        nameTextField!!.clear()
        enterpriseStateController!!.setCountryComboBox()
    }

    fun cancel() {
        val stage = closeButton!!.scene.window as Stage
        stage.close()
    }
}
