package hr.java.vjezbe.entitet;

import java.io.Serializable;

public abstract class Entitet implements Serializable {
	private Long id;

	public Entitet(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Entitet setId(Long id) {
		this.id = id;
		return this;
	}
}
