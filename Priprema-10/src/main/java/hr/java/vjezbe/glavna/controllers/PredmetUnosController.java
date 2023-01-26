package hr.java.vjezbe.glavna.controllers;

import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.entitet.Profesor;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.glavna.VjezbeApplication;
import hr.java.vjezbe.glavna.fxutil.FXUtil;
import hr.java.vjezbe.glavna.fxutil.FXValidate;
import hr.java.vjezbe.iznimke.DataSourceException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.HashSet;

public class PredmetUnosController {
	@FXML private TextField sifraUnos;
	@FXML private TextField nazivUnos;
	@FXML private TextField brojEctsBodovaUnos;
	@FXML private ChoiceBox<Profesor> nositeljUnos;
	@FXML private ListView<Student> studentiUnos;
	
	@FXML
	private void initialize() {
		var data = VjezbeApplication.getDataSource();

		try {
			nositeljUnos.setItems(FXCollections.observableList(data.readProfesori()));
		} catch (DataSourceException e) {
			FXUtil.showDataSourceErrorAlert(e.getMessage());
		}
		nositeljUnos.getSelectionModel().selectFirst();

		try {
			studentiUnos.setItems(FXCollections.observableList(data.readStudenti()));
		} catch (DataSourceException e) {
			FXUtil.showDataSourceErrorAlert(e.getMessage());
		}
		studentiUnos.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	@FXML
	private void unesi() {
		ArrayList<String> messages = new ArrayList<>();

		if (sifraUnos.getText().isBlank()) {
			messages.add("Polje šifra je prazno!");
		}

		if (nazivUnos.getText().isBlank()) {
			messages.add("Polje naziv je prazno!");
		}

		if (brojEctsBodovaUnos.getText().isBlank()) {
			messages.add("Polje ECTS bodovi je prazno!");
		}

		if (!FXValidate.isValidInt(brojEctsBodovaUnos.getText())) {
			messages.add("Polje ECTS bodovi ne sadrži broj!");
		}
		
		if (nositeljUnos.getSelectionModel().getSelectedItem() == null) {
			messages.add("Nije odbran nositelj!");
		}
		
		if (studentiUnos.getSelectionModel().getSelectedItems().size() == 0) {
			messages.add("Morate odabrati barem jednog studenta!");
		}

		if (messages.size() == 0) {
			try {
				VjezbeApplication.getDataSource().createPredmet(new Predmet(
					-1L,
					sifraUnos.getText(),
					nazivUnos.getText(),
					Integer.parseInt(brojEctsBodovaUnos.getText()),
					nositeljUnos.getValue(),
					new HashSet<>(studentiUnos.getSelectionModel().getSelectedItems())
				));
			} catch (DataSourceException e) {
				FXUtil.showDataSourceErrorAlert(e.getMessage());
			}

		} else {
			String m = String.join("\n", messages);

			var alert = new Alert(Alert.AlertType.ERROR, m);
			alert.setTitle("Greška");
			alert.show();
		}
	}
}
