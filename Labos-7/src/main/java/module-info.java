module hr.java.vjezbe.labos7 {
	requires javafx.controls;
	requires javafx.fxml;
	requires org.slf4j;


	opens hr.java.vjezbe.glavna to javafx.fxml;
	exports hr.java.vjezbe.glavna;
	exports hr.java.vjezbe.entitet;
	exports hr.java.vjezbe.glavna.controllers;
	opens hr.java.vjezbe.glavna.controllers to javafx.fxml;
	exports hr.java.vjezbe.data;
	opens hr.java.vjezbe.data to javafx.fxml;
}
