module bEIGHTManager {
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.controls;
	requires zip4j;
	requires org.json;
	requires org.apache.commons.io;
	
	opens application to javafx.graphics, javafx.fxml;
}
