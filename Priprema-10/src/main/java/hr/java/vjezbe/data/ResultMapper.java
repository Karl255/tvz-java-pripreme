package hr.java.vjezbe.data;

import hr.java.vjezbe.iznimke.DataSourceException;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface ResultMapper<T> {
	T map(ResultSet r) throws SQLException, DataSourceException;
}
