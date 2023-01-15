package hr.java.vjezbe.glavna.controllers;

import hr.java.vjezbe.data.Data;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.glavna.VjezbeApplication;
import hr.java.vjezbe.glavna.fxutil.LocalDateConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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

	public void initialize() {
		datumRodjenjaUnos.setConverter(new LocalDateConverter(VjezbeApplication.DATE_FORMAT_SHORT));
		
		jmbagColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJmbag()));
		imeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIme()));
		prezimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrezime()));
		datumRodjenjaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDatumRodjenja().format(VjezbeApplication.DATE_FORMAT_FULL)));
		
		studentTableView.setItems(FXCollections.observableList(Data.getStudenti()));
	}

	public void pretrazi() {
		var studenti = Data.getStudenti().stream()
			.filter(s -> s.getJmbag().contains(jmbagUnos.getText()))
			.filter(s -> s.getIme().contains(imeUnos.getText()))
			.filter(s -> s.getPrezime().contains(prezimeUnos.getText()))
			.filter(s -> datumRodjenjaUnos.getValue() == null || s.getDatumRodjenja().equals(datumRodjenjaUnos.getValue()))
			.toList();

		studentTableView.setItems(FXCollections.observableList(studenti));
	}
}
