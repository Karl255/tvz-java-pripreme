package hr.java.vjezbe.data;

import hr.java.vjezbe.iznimke.DataSourceException;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementModifier {
	void modify(PreparedStatement s) throws SQLException, DataSourceException;
}
