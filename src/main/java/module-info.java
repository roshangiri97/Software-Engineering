module com.disasterresponse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires jakarta.mail;
requires javafx.graphics;

    // Open the controller and other packages for reflection
    opens com.disasterresponse to javafx.fxml;
    opens com.disasterresponse.controller to javafx.fxml,javafx.graphics;
    opens com.disasterresponse.model to javafx.fxml;

    exports com.disasterresponse;
    exports com.disasterresponse.controller;
    exports com.disasterresponse.model;
}
