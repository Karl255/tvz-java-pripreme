module hr.java.vjezbe.priprema10 {
	requires java.sql;
	requires javafx.controls;
	requires javafx.fxml;
	requires org.slf4j;
	requires com.h2database;

	opens hr.java.vjezbe.data to javafx.fxml;
	opens hr.java.vjezbe.glavna to javafx.fxml;
	opens hr.java.vjezbe.glavna.controllers to javafx.fxml;
	exports hr.java.vjezbe.data;
	exports hr.java.vjezbe.entitet;
	exports hr.java.vjezbe.glavna;
	exports hr.java.vjezbe.glavna.controllers;
	exports hr.java.vjezbe.iznimke;
}
