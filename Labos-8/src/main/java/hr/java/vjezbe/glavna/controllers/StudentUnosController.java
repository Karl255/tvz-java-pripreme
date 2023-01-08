package hr.java.vjezbe.glavna.controllers;

import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.glavna.VjezbeApplication;
import hr.java.vjezbe.glavna.fxutil.LocalDateConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.ArrayList;

public class StudentUnosController {
	@FXML private TextField jmbagUnos;
	@FXML private TextField imeUnos;
	@FXML private TextField prezimeUnos;
	@FXML private DatePicker datumRodjenjaUnos;

	@FXML
	private void initialize() {
		datumRodjenjaUnos.setConverter(new LocalDateConverter(VjezbeApplication.DATE_FORMAT_SHORT));
		datumRodjenjaUnos.setValue(LocalDate.of(2002, 1, 1));
	}

	@FXML
	private void unesi() {
		ArrayList<String> messages = new ArrayList<>();

		if (jmbagUnos.getText().isBlank()) {
			messages.add("Polje JMBAG je prazno!");
		}

		if (imeUnos.getText().isBlank()) {
			messages.add("Polje ime je prazno!");
		}

		if (prezimeUnos.getText().isBlank()) {
			messages.add("Polje prezime je prazno!");
		}

		if (datumRodjenjaUnos.getValue() == null) {
			messages.add("Nije unesen ispravan datum rođenja!");
		}

		if (messages.size() == 0) {
			VjezbeApplication.getDataSource().createStudent(new Student(
				-1,
				imeUnos.getText(),
				prezimeUnos.getText(),
				jmbagUnos.getText(),
				datumRodjenjaUnos.getValue()
			));
		} else {
			String m = String.join("\n", messages);

			var alert = new Alert(Alert.AlertType.ERROR, m);
			alert.setTitle("Greška");
			alert.show();
		}
	}
}
