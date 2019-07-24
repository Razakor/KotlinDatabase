package com.vntu.add

import com.vntu.directories.EnterpriseStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException

class AddRegionController {
    lateinit var closeButton: Button
    lateinit var nameTextField: TextField
    private lateinit var enterpriseStateController: EnterpriseStateController
    private lateinit var idState: String

    @Throws(SQLException::class)
    fun initData(enterpriseStateController: EnterpriseStateController, stateName: String) {
        this.enterpriseStateController = enterpriseStateController
        idState = QueryResult.getListResult("SELECT id FROM state WHERE name = '$stateName'", false)[0].toString()
    }

    @Throws(SQLException::class)
    fun addRegionQuery() {
        val name = Parser.processQuote(nameTextField.text)
        val query = "INSERT INTO region(name, id_state)\n" +
                "VALUES ('$name', '$idState')"
        QueryResult.updateDataBase(query)
        nameTextField.clear()
        enterpriseStateController.setRegionComboBox()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }
}
