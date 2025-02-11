module org.example.feemaintenanceapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.feemaintenanceapplication to javafx.fxml;
    exports org.example.feemaintenanceapplication;
}