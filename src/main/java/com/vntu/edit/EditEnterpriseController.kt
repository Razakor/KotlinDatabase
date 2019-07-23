package com.vntu.edit

import com.vntu.directories.EnterpriseStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.SQLException
import java.util.ArrayList
import java.util.Arrays

class EditEnterpriseController {

    lateinit var nameTextField: TextField
    lateinit var indexTextField: TextField
    lateinit var adressTextField: TextField
    lateinit var telephoneTextField: TextField
    lateinit var faxTextField: TextField
    lateinit var emailTextField: TextField
    lateinit var closeButton: Button
    lateinit var websiteTextField: TextField
    private var enterpriseStateController: EnterpriseStateController? = null
    private var enterpriseInfo: ArrayList<String>? = null

    @Throws(SQLException::class)
    fun initData(enterpriseStateController: EnterpriseStateController, selectedRow: String) {
        this.enterpriseStateController = enterpriseStateController
        enterpriseInfo =
            ArrayList(listOf(*selectedRow.split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))


        while (enterpriseInfo!!.size > 11) {
            enterpriseInfo!!.add(1, enterpriseInfo!![1] + ", " + enterpriseInfo!![2])
            enterpriseInfo!!.removeAt(2)
            enterpriseInfo!!.removeAt(2)
        }


        nameTextField.text = enterpriseInfo!![2]
        indexTextField.text = enterpriseInfo!![0]
        adressTextField.text = enterpriseInfo!![1]
        telephoneTextField.text = enterpriseInfo!![3]
        faxTextField.text = enterpriseInfo!![4]
        emailTextField.text = enterpriseInfo!![5]
        websiteTextField.text = enterpriseInfo!![6]
    }

    @Throws(SQLException::class)
    fun editEnterprise() {
        val name = Parser.processQuote(nameTextField.text)
        val index = indexTextField.text
        val adress = Parser.processQuote(adressTextField.text)
        val telephone = telephoneTextField.text
        val fax = faxTextField.text
        val email = emailTextField.text
        val website = websiteTextField.text

        val query =
            "UPDATE enterprise SET name = '" + name + "', `index` = '" + index + "', adress = '" + adress + "', telephone = '" + telephone + "', fax = '" + fax + "', email = '" + email + "', website = '" + website + "' WHERE name = '" + enterpriseInfo!![2] + "' AND adress = '" + enterpriseInfo!![1] + "'"
        QueryResult.updateDataBase(query)
        enterpriseStateController!!.setCity()
        val stage = closeButton.scene.window as Stage
        stage.close()
    }

    fun cancel() {
        val stage = closeButton.scene.window as Stage
        stage.close()
    }


}
