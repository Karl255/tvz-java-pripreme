package hr.java.vjezbe.entitet;

import java.io.Serializable;

/**
 * Dvorana u kojoj se može održavati nastava i ispiti.
 * @param zgrada Naziv zgrade u kojoj se prostorija nalazi.
 * @param prostorija Naziv prostorije.
 */
public record Dvorana(String zgrada, String prostorija) implements Serializable {}
