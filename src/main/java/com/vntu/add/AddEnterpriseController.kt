package com.vntu.add

import com.vntu.directories.EnterpriseStateController
import com.vntu.main.Parser
import com.vntu.main.QueryResult
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Stage

import java.sql.SQLException

class AddEnterpriseController {


    private var enterpriseStateController: EnterpriseStateController? = null

    var websiteTextField: TextField? = null
    var telephoneTextField: TextField? = null
    var nameTextField: TextField? = null
    var indexTextField: TextField? = null
    var adressTextField: TextField? = null
    var faxTextField: TextField? = null
    var emailTextField: TextField? = null
    var closeButton: Button? = null
    private var idCity: String? = null


    @Throws(SQLException::class)
    fun initData(enterpriseStateController: EnterpriseStateController, city: String) {

        this.enterpriseStateController = enterpriseStateController
        this.idCity = QueryResult.getListResult("SELECT id FROM city WHERE name = '$city'", false).get(0).toString()
    }


    @Throws(SQLException::class)
    fun addEnterpriseQuery() {
        val name = Parser.processQuote(nameTextField!!.text)
        val index = indexTextField!!.text
        val adress = Parser.processQuote(adressTextField!!.text)
        val telephone = telephoneTextField!!.text
        val fax = faxTextField!!.text
        val email = emailTextField!!.text
        val website = websiteTextField!!.text


        val query = "INSERT INTO enterprise (name, id_city, `index`, adress, telephone, fax, email, website) \n" +
                "VALUES ('" + name + "', '" + idCity + "', '" + index + "', '" + adress + "', '" + telephone + "', '" + fax + "', '" + email + "', '" + website + "')"
        QueryResult.updateDataBase(query)
        clear()
        enterpriseStateController!!.setCity()

    }

    fun cancel() {
        val stage = closeButton!!.scene.window as Stage
        stage.close()
    }

    private fun clear() {

        nameTextField!!.clear()
        indexTextField!!.clear()
        adressTextField!!.clear()
        telephoneTextField!!.clear()
        faxTextField!!.clear()
        emailTextField!!.clear()
        websiteTextField!!.clear()

    }


}
