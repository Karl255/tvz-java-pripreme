package hr.java.vjezbe.glavna;

import hr.java.vjezbe.data.BazaPodatakaDataSource;
import hr.java.vjezbe.data.DataSource;
import hr.java.vjezbe.data.DatotekeDataSource;
import hr.java.vjezbe.iznimke.DataSourceException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@SuppressWarnings("FieldMayBeFinal")
public class VjezbeApplication extends Application {
	private static Stage appStage;
	private static DataSource dataSource;

	public static final DateTimeFormatter DATE_FORMAT_SHORT = DateTimeFormatter.ofPattern("d.M.yyyy.");
	public static final DateTimeFormatter DATE_TIME_FORMAT_SHORT = DateTimeFormatter.ofPattern("d.M.yyyy. H:mm");
	public static final DateTimeFormatter TIME_FORMAT_SHORT = DateTimeFormatter.ofPattern("H:mm");
	public static final DateTimeFormatter DATE_FORMAT_FULL = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
	public static final DateTimeFormatter DATE_TIME_FORMAT_FULL = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
	public static final DateTimeFormatter TIME_FORMAT_FULL = DateTimeFormatter.ofPattern("HH:mm");
	
	@Override
	public void start(Stage stage) throws IOException {
		appStage = stage;
		
		FXMLLoader fxmlLoader = new FXMLLoader(VjezbeApplication.class.getResource("views/pocetna-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("Vježba");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		try (var ds = new BazaPodatakaDataSource()) {
			dataSource = ds;
			launch();
		} catch (DataSourceException e) {
			var alert = new Alert(Alert.AlertType.ERROR, "Unable to connect to database", ButtonType.OK);
			alert.setTitle("Error");
			alert.showAndWait();
		}
	}
	
	public static void showWindow(String resourcePath) {
		try {
			var window = (Parent) FXMLLoader.load(Objects.requireNonNull(VjezbeApplication.class.getResource(resourcePath)));
			appStage.setScene(new Scene(window));
			appStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static DataSource getDataSource() {
		return dataSource;
	}
}
