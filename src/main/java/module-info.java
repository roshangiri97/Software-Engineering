// File: src/main/java/module-info.java

module com.disasterresponse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.persistence;
    requires java.sql;
    requires mysql.connector.java;
    

    opens com.disasterresponse to javafx.fxml;
    opens com.disasterresponse.controller to javafx.fxml;
    opens com.disasterresponse.model to javafx.fxml;
    
    exports com.disasterresponse;
    exports com.disasterresponse.controller;
    exports com.disasterresponse.model;
    
    
}