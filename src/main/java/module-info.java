module org.example.apppooproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    //requires mysql.connector.java;
    requires javafx.graphics;
    requires java.desktop;

    opens org.apppooproject.DataBaseManagers to javafx.fxml;
    exports org.apppooproject.DataBaseManagers;
    exports org.apppooproject.Model;
    opens org.apppooproject.Model to javafx.fxml;
    exports org.apppooproject.Controllers;
    opens org.apppooproject.Controllers to javafx.fxml;
    exports org.apppooproject.App;
    opens org.apppooproject.App to javafx.fxml;
}