package com.athena.sdvpapers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Paper {
	private String id;
	private String title;
	private int year;
	private List<Author> authors = new ArrayList<>();

	public Paper(String id,String title, int year) {
		this.id = id;
		this.title = title;
		this.year = year;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, title, year);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paper other = (Paper) obj;
		return Objects.equals(id, other.id) && Objects.equals(title, other.title) && year == other.year;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public int getYear() {
		return year;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void addAuthor(Author author) {
		authors.add(author);
	}
}