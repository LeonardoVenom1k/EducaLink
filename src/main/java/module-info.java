module br.com.javafx.educalink {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens br.com.javafx.educalink to javafx.fxml, com.fasterxml.jackson.databind;
    opens br.com.javafx.educalink.login to javafx.fxml, com.fasterxml.jackson.databind, com.google.gson;
    opens br.com.javafx.educalink.areaalu to javafx.fxml, com.fasterxml.jackson.databind, com.google.gson;
    opens br.com.javafx.educalink.areaprof to javafx.fxml, com.fasterxml.jackson.databind, com.google.gson;

    exports br.com.javafx.educalink;
    exports br.com.javafx.educalink.login;
    exports br.com.javafx.educalink.areaalu;
    exports br.com.javafx.educalink.areaprof;
}
