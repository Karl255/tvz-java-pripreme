package hr.java.vjezbe.entitet;

import java.io.Serializable;

/**
 * Dvorana u kojoj se može održavati nastava i ispiti.
 * @param naziv Naziv prostorije.
 * @param zgrada Naziv zgrade u kojoj se prostorija nalazi.
 */
public record Dvorana(String naziv, String zgrada) implements Serializable {}
