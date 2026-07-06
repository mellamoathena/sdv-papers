package com.athena.sdvpapers.view;

import java.util.List;
import com.athena.sdvpapers.Author;

public interface AuthorView {

	void showAllAuthors(List<Author> authors);

	void authorAdded(Author author);

	void authorRemoved(Author author);

	void showError(String message, Author author);

	void showErrorAuthorNotFound(Author author);
	
	void authorUpdated(Author author);
}