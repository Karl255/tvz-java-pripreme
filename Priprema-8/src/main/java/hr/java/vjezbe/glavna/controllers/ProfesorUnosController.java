package hr.java.vjezbe.glavna.controllers;

import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.glavna.VjezbeApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class ProfesorUnosController {
	@FXML private TextField sifraUnos;
	@FXML private TextField imeUnos;
	@FXML private TextField prezimeUnos;
	@FXML private TextField titulaUnos;
	
	@FXML
	private void unesi() {
		ArrayList<String> messages = new ArrayList<>();
		
		if (sifraUnos.getText().isBlank()) {
			messages.add("Polje šifra je prazno!");
		}
		
		if (imeUnos.getText().isBlank()) {
			messages.add("Polje ime je prazno!");
		}
		
		if (prezimeUnos.getText().isBlank()) {
			messages.add("Polje prezime je prazno!");
		}
		
		if (titulaUnos.getText().isBlank()) {
			messages.add("Polje titula je prazno!");
		}
		
		if (messages.size() == 0) {
			VjezbeApplication.getDataSource().createProfesor(new Profesor(
				-1, 
				sifraUnos.getText(),
				imeUnos.getText(),
				prezimeUnos.getText(),
				titulaUnos.getText()
			));
		} else {
			String m = String.join("\n", messages);
			
			var alert = new Alert(Alert.AlertType.ERROR, m);
			alert.setTitle("Greška");
			alert.show();
		}
	}
}
