package hr.java.vjezbe.glavna.controllers;

import hr.java.vjezbe.entitet.Predmet;
import hr.java.vjezbe.glavna.VjezbeApplication;
import hr.java.vjezbe.glavna.fxutil.FXUtil;
import hr.java.vjezbe.iznimke.DataSourceException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class PredmetiPretragaController {
	@FXML
	private TextField sifraUnos;
	@FXML
	private TextField nazivUnos;
	@FXML
	private TextField brojEctsBodovaUnos;
	@FXML
	private TextField nositeljUnos;
	@FXML
	private TextField brojStudenataUnos;

	@FXML
	private TableView<Predmet> predmetTableView;
	@FXML
	private TableColumn<Predmet, String> sifraColumn;
	@FXML
	private TableColumn<Predmet, String> nazivColumn;
	@FXML
	private TableColumn<Predmet, Integer> brojEctsBodovaColumn;
	@FXML
	private TableColumn<Predmet, String> nositeljColumn;
	@FXML
	private TableColumn<Predmet, Integer> brojStudenataColumn;

	private List<Predmet> sviPredmeti;

	public void initialize() {
		sifraColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSifra()));
		nazivColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNaziv()));
		brojEctsBodovaColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getBrojEctsBodova()).asObject());
		nositeljColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNositelj().toString()));
		brojStudenataColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getStudenti().size()).asObject());

		try {
			sviPredmeti = VjezbeApplication.getDataSource().readPredmeti();
		} catch (DataSourceException e) {
			FXUtil.showDataSourceErrorAlert(e.getMessage());
		}
		predmetTableView.setItems(FXCollections.observableList(sviPredmeti));
	}

	public void pretrazi() {
		var filtrirano = sviPredmeti.stream()
			.filter(p -> p.getSifra().contains(sifraUnos.getText()))
			.filter(p -> p.getNaziv().contains(nazivUnos.getText()))
			.filter(p -> FXUtil.tryFilterByIntegerInput(p.getBrojEctsBodova(), brojEctsBodovaUnos.getText()))
			.filter(predmet -> predmet.getNositelj().toString().contains(nositeljUnos.getText()))
			.filter(p -> FXUtil.tryFilterByIntegerInput(p.getStudenti().size(), brojStudenataUnos.getText()))
			.toList();

		predmetTableView.setItems(FXCollections.observableList(filtrirano));
	}
}
