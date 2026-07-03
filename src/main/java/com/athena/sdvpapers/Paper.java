package com.athena.sdvpapers;

import java.util.ArrayList;
import java.util.List;

public class Paper {
	private String title;
	private int year;
	private List<Author> authors = new ArrayList<>();

	public Paper(String title, int year) {
		this.title = title;
		this.year = year;
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