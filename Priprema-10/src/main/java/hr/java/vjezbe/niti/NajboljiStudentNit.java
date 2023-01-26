package hr.java.vjezbe.niti;

import hr.java.vjezbe.entitet.Ispit;
import hr.java.vjezbe.entitet.Student;
import hr.java.vjezbe.glavna.VjezbeApplication;
import hr.java.vjezbe.iznimke.DataSourceException;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class NajboljiStudentNit implements Runnable {
	private final Stage stage;

	public NajboljiStudentNit(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void run() {
		var dataSource = VjezbeApplication.getDataSource();
		List<Student> studenti;
		
		try {
			studenti = dataSource.readStudenti();
		} catch (DataSourceException e) {
			e.printStackTrace();
			return;
		}
		
		Optional<Student> najbolji = Optional.empty();
		double najboljiProsjek = 0;
		
		for (var student : studenti) {
			List<Ispit> ispiti;

			try {
				ispiti = dataSource.readIspitiWhere(new Ispit(null, null, student, null, null));
			} catch (DataSourceException e) {
				e.printStackTrace();
				return;
			}
			
			var prosjek = ispiti.stream()
				.mapToDouble(i -> i.getOcjena().getNumerickaVrijednost())
				.average()
				.orElse(0);
			
			if (najbolji.isEmpty() || prosjek > najboljiProsjek) {
				najbolji = Optional.of(student);
				najboljiProsjek = prosjek;
			}
		}
		
		var title = najbolji.isPresent()
			? String.format("Najbolji student: %s %s (%.2f)", najbolji.get().getIme(), najbolji.get().getPrezime(), najboljiProsjek)
			: "Najbolji student još nije određen!";
		
		stage.setTitle(title);
	}
}
