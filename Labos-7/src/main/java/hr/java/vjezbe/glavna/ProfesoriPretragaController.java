package hr.java.vjezbe.glavna;

import hr.java.vjezbe.data.Datoteke;
import hr.java.vjezbe.entitet.Profesor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class ProfesoriPretragaController {
	private List<Profesor> sviProfesori;

	@FXML
	private TextField sifraUnos;
	@FXML
	private TextField imeUnos;
	@FXML
	private TextField prezimeUnos;
	@FXML
	private TextField titulaUnos;

	@FXML
	private TableView<Profesor> profesorTableView;
	@FXML
	private TableColumn<Profesor, String> sifraColumn;
	@FXML
	private TableColumn<Profesor, String> imeColumn;
	@FXML
	private TableColumn<Profesor, String> prezimeColumn;
	@FXML
	private TableColumn<Profesor, String> titulaColumn;

	public void initialize() {
		sviProfesori = Datoteke.loadProfesori();
		sifraColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSifra()));
		imeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIme()));
		prezimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrezime()));
		titulaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitula()));
		profesorTableView.setItems(FXCollections.observableList(sviProfesori));
	}

	public void pretrazi() {
		var filtrirano = sviProfesori.stream()
			.filter(p -> p.getSifra().contains(sifraUnos.getText()))
			.filter(p -> p.getIme().contains(imeUnos.getText()))
			.filter(p -> p.getPrezime().contains(prezimeUnos.getText()))
			.filter(p -> p.getTitula().contains(titulaUnos.getText())).toList();

		profesorTableView.setItems(FXCollections.observableList(filtrirano));
	}
}
