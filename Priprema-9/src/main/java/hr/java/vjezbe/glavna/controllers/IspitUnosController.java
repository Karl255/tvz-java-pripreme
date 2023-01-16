package hr.java.vjezbe.glavna.controllers;

import hr.java.vjezbe.entitet.*;
import hr.java.vjezbe.glavna.VjezbeApplication;
import hr.java.vjezbe.glavna.fxutil.FXUtil;
import hr.java.vjezbe.glavna.fxutil.FXValidate;
import hr.java.vjezbe.glavna.fxutil.LocalDateConverter;
import hr.java.vjezbe.iznimke.DataSourceException;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalTime;
import java.util.ArrayList;

public class IspitUnosController {
	@FXML private ChoiceBox<Predmet> predmetUnos;
	@FXML private ChoiceBox<Student> studentUnos;
	@FXML private TextField ocjenaUnos;
	@FXML private DatePicker datumPisanjaUnos;
	@FXML private TextField vrijemePisanjaUnos;
	
	@FXML
	private void initialize() {
		var data = VjezbeApplication.getDataSource();

		try {
			predmetUnos.setItems(FXCollections.observableList(data.readPredmeti()));
		} catch (DataSourceException e) {
			FXUtil.showDataSourceErrorAlert(e.getMessage());
		}
		predmetUnos.getSelectionModel().selectFirst();
		predmetUnos.getSelectionModel().selectedItemProperty().addListener(this::odabranPredmet);
		
		datumPisanjaUnos.setConverter(new LocalDateConverter(VjezbeApplication.DATE_FORMAT_SHORT));
	}
	
	private void odabranPredmet(Observable observable, Predmet previousPredmet, Predmet predmet) {
		studentUnos.setItems(FXCollections.observableList(new ArrayList<>(predmet.getStudenti())));
		studentUnos.getSelectionModel().selectFirst();
	}
	
	@FXML
	private void unesi() {
		ArrayList<String> messages = new ArrayList<>();
		
		if (predmetUnos.getSelectionModel().getSelectedItem() == null) {
			messages.add("Nije odabran predmet!");
		}
		
		if (studentUnos.getSelectionModel().getSelectedItem() == null) {
			messages.add("Nije odabran student!");
		}

		if (!FXValidate.isValidInt(ocjenaUnos.getText(), o -> o >= 1 && o <= 5)) {
			messages.add("Nije unesena ispravna ocjena!");
		}
		
		if (datumPisanjaUnos.getValue() == null) {
			messages.add("Nije unesen ispravan datum!");
		}
		
		if (!FXValidate.isValidTime(vrijemePisanjaUnos.getText(), VjezbeApplication.TIME_FORMAT_SHORT)) {
			messages.add("Nije uneseno ispravno vrijeme!");
		}

		if (messages.size() == 0) {
			try {
				VjezbeApplication.getDataSource().createIspit(new Ispit(
					-1,
					predmetUnos.getValue(),
					studentUnos.getValue(),
					Ocjena.parseOcjena(ocjenaUnos.getText()),
					datumPisanjaUnos.getValue().atTime(LocalTime.parse(vrijemePisanjaUnos.getText(), VjezbeApplication.TIME_FORMAT_SHORT))
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
