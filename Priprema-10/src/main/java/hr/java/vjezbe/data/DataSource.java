package hr.java.vjezbe.data;

import hr.java.vjezbe.entitet.*;
import hr.java.vjezbe.iznimke.DataSourceException;

import java.util.List;

public interface DataSource {
	List<Student> readStudenti() throws DataSourceException;
	void createStudent(Student student) throws DataSourceException;
	List<Student> readStudentiKojiImajuRodjendan() throws DataSourceException;
	List<Student> readStudentiWhere(Student criteria) throws DataSourceException;

	List<Profesor> readProfesori() throws DataSourceException;
	void createProfesor(Profesor profesor) throws DataSourceException;

	List<Predmet> readPredmeti() throws DataSourceException;
	void createPredmet(Predmet predmet) throws DataSourceException;

	List<Ispit> readIspiti() throws DataSourceException;
	void createIspit(Ispit ispit) throws DataSourceException;
	List<Ispit> readIspitiWhere(Ispit criteria) throws DataSourceException;

	List<ObrazovnaUstanova> readObrazovneUstanove() throws DataSourceException;
	void createObrazovnaUstanova(ObrazovnaUstanova ustanova) throws DataSourceException;
}
