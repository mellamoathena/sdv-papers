package com.athena.sdvpapers.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.athena.sdvpapers.Author;
import com.athena.sdvpapers.repository.AuthorRepository;
import com.athena.sdvpapers.view.AuthorView;

@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

	@Mock
	private AuthorRepository authorRepository;

	@Mock
	private AuthorView authorView;

	@InjectMocks
	private AuthorController authorController;

	@Test
	void testAllAuthors() {
		Author author = new Author("1", "Bondavalli");
		when(authorRepository.findAll())
			.thenReturn(Arrays.asList(author));
		authorController.allAuthors();
		verify(authorView).showAllAuthors(Arrays.asList(author));
	}

	@Test
	void testNewAuthorWhenAuthorDoesNotAlreadyExist() {
		Author author = new Author("1", "Bondavalli");
		when(authorRepository.findById("1")).thenReturn(null);
		authorController.newAuthor(author);
		verify(authorRepository).save(author);
		verify(authorView).authorAdded(author);
	}

	@Test
	void testNewAuthorWhenAuthorAlreadyExists() {
		Author authorToAdd = new Author("1", "Bondavalli");
		Author existingAuthor = new Author("1", "Existing");
		when(authorRepository.findById("1")).thenReturn(existingAuthor);
		authorController.newAuthor(authorToAdd);
		verify(authorView).showError(
			"Already existing author with id 1", existingAuthor);
		verifyNoMoreInteractions(authorRepository);
	}

	@Test
	void testDeleteAuthorWhenAuthorExists() {
		Author author = new Author("1", "Bondavalli");
		when(authorRepository.findById("1")).thenReturn(author);
		authorController.deleteAuthor(author);
		verify(authorRepository).delete("1");
		verify(authorView).authorRemoved(author);
	}

	@Test
	void testDeleteAuthorWhenAuthorDoesNotExist() {
		Author author = new Author("1", "Bondavalli");
		when(authorRepository.findById("1")).thenReturn(null);
		authorController.deleteAuthor(author);
		verify(authorView).showErrorAuthorNotFound(author);
		verifyNoMoreInteractions(authorRepository);
	}

	@Test
	void testAddPaperToAuthorWhenAuthorExists() {
		Author author = new Author("1", "Bondavalli");
		when(authorRepository.findById("1")).thenReturn(author);
		authorController.addPaperToAuthor(author, "10");
		assertThat(author.getPaperIds()).containsExactly("10");
		verify(authorRepository).save(author);
		verify(authorView).authorUpdated(author);
	}

	@Test
	void testAddPaperToAuthorWhenAuthorDoesNotExist() {
		Author author = new Author("1", "Bondavalli");
		when(authorRepository.findById("1")).thenReturn(null);
		authorController.addPaperToAuthor(author, "10");
		verify(authorView).showErrorAuthorNotFound(author);
		verifyNoMoreInteractions(authorRepository);
	}
}