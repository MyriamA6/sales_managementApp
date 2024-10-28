package org.apppooproject.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.awt.*;

public class SignUpController {

    @FXML
    private TextField address;

    @FXML
    private TextField email;

    @FXML
    private TextField first_name;

    @FXML
    private TextField last_name;

    @FXML
    private TextField password;

    @FXML
    private TextField user_id;

    @FXML
    private Button sign_up_button;

    //verifier si le client n'est pas déjà dans la base, si ces coordonnées n'y sont pas déjà, mail/identifiant....
    @FXML
    void createANewCustomer(ActionEvent event) {



    }

}
