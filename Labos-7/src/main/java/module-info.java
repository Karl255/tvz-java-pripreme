module hr.java.vjezbe.labos7 {
	requires javafx.controls;
	requires javafx.fxml;
	requires org.slf4j;


	opens hr.java.vjezbe.glavna to javafx.fxml;
	exports hr.java.vjezbe.glavna;
}
