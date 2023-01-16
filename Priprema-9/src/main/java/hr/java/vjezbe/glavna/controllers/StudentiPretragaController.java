package hr.java.vjezbe.glavna.controllers;

import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.glavna.VjezbeApplication;
import hr.java.vjezbe.glavna.fxutil.FXUtil;
import hr.java.vjezbe.glavna.fxutil.LocalDateConverter;
import hr.java.vjezbe.iznimke.DataSourceException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class StudentiPretragaController {
	@FXML
	private TextField jmbagUnos;
	@FXML
	private TextField imeUnos;
	@FXML
	private TextField prezimeUnos;
	@FXML
	private DatePicker datumRodjenjaUnos;

	@FXML
	private TableView<Student> studentTableView;
	@FXML
	private TableColumn<Student, String> jmbagColumn;
	@FXML
	private TableColumn<Student, String> imeColumn;
	@FXML
	private TableColumn<Student, String> prezimeColumn;
	@FXML
	private TableColumn<Student, String> datumRodjenjaColumn;

	private List<Student> sviStudenti;

	public void initialize() {
		datumRodjenjaUnos.setConverter(new LocalDateConverter(VjezbeApplication.DATE_FORMAT_SHORT));

		jmbagColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJmbag()));
		imeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIme()));
		prezimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrezime()));
		datumRodjenjaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDatumRodjenja().format(VjezbeApplication.DATE_FORMAT_FULL)));

		try {
			sviStudenti = VjezbeApplication.getDataSource().readStudenti();
		} catch (DataSourceException e) {
			FXUtil.showDataSourceErrorAlert(e.getMessage());
		}
		studentTableView.setItems(FXCollections.observableList(sviStudenti));
	}

	public void pretrazi() {
		var filtrirano = sviStudenti.stream()
			.filter(s -> s.getJmbag().contains(jmbagUnos.getText()))
			.filter(s -> s.getIme().contains(imeUnos.getText()))
			.filter(s -> s.getPrezime().contains(prezimeUnos.getText()))
			.filter(s -> datumRodjenjaUnos.getValue() == null || s.getDatumRodjenja().equals(datumRodjenjaUnos.getValue()))
			.toList();

		studentTableView.setItems(FXCollections.observableList(filtrirano));
	}
}
