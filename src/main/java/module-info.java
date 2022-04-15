module com.besturingssystemen2.opdracht2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.xml;

    opens com.besturingssystemen2.opdracht2 to javafx.fxml;
    exports com.besturingssystemen2.opdracht2;
}