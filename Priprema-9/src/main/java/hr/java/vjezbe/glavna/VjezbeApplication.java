package hr.java.vjezbe.glavna;

import hr.java.vjezbe.data.BazaPodatakaDataSource;
import hr.java.vjezbe.data.DataSource;
import hr.java.vjezbe.iznimke.DataSourceException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Properties;

@SuppressWarnings("FieldMayBeFinal")
public class VjezbeApplication extends Application {
	private static final Logger logger = LoggerFactory.getLogger(VjezbeApplication.class);
	
	private static Stage appStage;
	private static DataSource dataSource;

	public static final DateTimeFormatter DATE_FORMAT_SHORT = DateTimeFormatter.ofPattern("d.M.yyyy.");
	public static final DateTimeFormatter DATE_TIME_FORMAT_SHORT = DateTimeFormatter.ofPattern("d.M.yyyy. H:mm");
	public static final DateTimeFormatter TIME_FORMAT_SHORT = DateTimeFormatter.ofPattern("H:mm");
	public static final DateTimeFormatter DATE_FORMAT_FULL = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
	public static final DateTimeFormatter DATE_TIME_FORMAT_FULL = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
	public static final DateTimeFormatter TIME_FORMAT_FULL = DateTimeFormatter.ofPattern("HH:mm");
	
	private static final String DB_PROPERTIES_PATH = "/db.properties";
	
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
		try (var propertiesStream = VjezbeApplication.class.getResourceAsStream(DB_PROPERTIES_PATH)) {
			var properties = new Properties();
			properties.load(propertiesStream);
		
			try (var ds = new BazaPodatakaDataSource(properties)) {
				dataSource = ds;
				launch();
			} catch (DataSourceException e) {
				logger.error("Couldn't connect to database", e);
			} catch (IOException e) {
				logger.error("Couldn't find database properties file: " + DB_PROPERTIES_PATH, e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
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
