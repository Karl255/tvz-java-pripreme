package hr.java.vjezbe.glavna.controllers;

import hr.java.vjezbe.glavna.VjezbeApplication;
import javafx.fxml.FXML;

public class IzbornikController {
	@FXML
	public void otvoriPretraguProfesora() {
		VjezbeApplication.showWindow("views/profesori-pretraga-view.fxml");
	}
	
	@FXML
	public void otvoriPretraguStudenata() {
		VjezbeApplication.showWindow("views/studenti-pretraga-view.fxml");
	}
	
	@FXML
	public void otvoriPretraguPredmeta() {
		VjezbeApplication.showWindow("views/predmeti-pretraga-view.fxml");
	}
	
	@FXML
	public void otvoriPretraguIspita() {
		VjezbeApplication.showWindow("views/ispiti-pretraga-view.fxml");
	}
}
