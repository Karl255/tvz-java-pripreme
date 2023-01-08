package hr.java.vjezbe.sortiranje;

import hr.java.vjezbe.entitet.Student;

import java.util.Comparator;

public class StudentSorter implements Comparator<Student> {
	@Override
	public int compare(Student a, Student b) {
		int comp = a.getPrezime().compareTo(b.getPrezime());
		return comp != 0 ? comp : a.getIme().compareTo(b.getIme());
	}
}
