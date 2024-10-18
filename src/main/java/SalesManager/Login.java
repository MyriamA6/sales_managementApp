package SalesManager;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {

    @FXML
    private Button connexion_button;

    @FXML
    private TextField login_name;

    @FXML
    private PasswordField password;

    @FXML
    public int connectingToUserAccount(Connection co) throws SQLException {
        Statement stmt=co.createStatement();
        ResultSet rs=stmt.executeQuery("select customer_id from customer where login_name="+login_name.getText()+
                " and user_password="+password.getText());

        return 0;
    }


}
