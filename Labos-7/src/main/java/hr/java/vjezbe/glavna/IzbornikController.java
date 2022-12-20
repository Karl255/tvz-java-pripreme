package hr.java.vjezbe.glavna;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;

public class IzbornikController {
	@FXML
	public void otvoriPretraguProfesora() {
		VjezbeApplication.showWindow(FXUtil.class.getResource("profesori-pretraga-view.fxml"));
	}
}
