package hr.java.vjezbe.entitet;

import java.io.Serializable;

public abstract class Entitet implements Serializable {
	private long id;

	public Entitet(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Entitet setId(long id) {
		this.id = id;
		return this;
	}
}
