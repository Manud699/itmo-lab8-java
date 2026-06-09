module client {

    requires javafx.controls;
    requires javafx.fxml;

    opens client to javafx.fxml;
    requires common;
    requires java.desktop;
    exports client;

    opens client.controllers to javafx.fxml;
    exports client.controllers;
    exports client.network;
    opens client.network to javafx.fxml;

}