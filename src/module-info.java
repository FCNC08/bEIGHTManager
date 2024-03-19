module bEIGHTManager {
	requires javafx.graphics;
	requires java.desktop;
	requires javafx.controls;
	requires zip4j;
	
	opens application to javafx.graphics, javafx.fxml;
}
