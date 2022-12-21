package hr.java.vjezbe.glavna;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class VjezbeApplication extends Application {
	private static Stage appStage;

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
		launch();
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
}
