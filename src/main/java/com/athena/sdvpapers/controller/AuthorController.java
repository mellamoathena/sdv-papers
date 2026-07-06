package com.athena.sdvpapers.controller;

import com.athena.sdvpapers.Author;
import com.athena.sdvpapers.repository.AuthorRepository;
import com.athena.sdvpapers.view.AuthorView;

public class AuthorController {

	private AuthorView authorView;
	private AuthorRepository authorRepository;

	public AuthorController(AuthorView authorView, AuthorRepository authorRepository) {
		this.authorView = authorView;
		this.authorRepository = authorRepository;
	}

	public void allAuthors() {
		authorView.showAllAuthors(authorRepository.findAll());
	}

	public void newAuthor(Author author) {
		Author existing = authorRepository.findById(author.getId());
		if (existing != null) {
			authorView.showError(
				"Already existing author with id " + author.getId(),
				existing);
			return;
		}
		authorRepository.save(author);
		authorView.authorAdded(author);
	}

	public void deleteAuthor(Author author) {
		if (authorRepository.findById(author.getId()) == null) {
			authorView.showErrorAuthorNotFound(author);
			return;
		}
		authorRepository.delete(author.getId());
		authorView.authorRemoved(author);
	}
	public void addPaperToAuthor(Author author, String paperId) {
		Author existing = authorRepository.findById(author.getId());
		if (existing == null) {
			authorView.showErrorAuthorNotFound(author);
			return;
		}
		existing.addPaperId(paperId);
		authorRepository.save(existing);
		authorView.authorUpdated(existing);
	}
}