package com.athena.sdvpapers.controller;

// Mockito static helpers: verify (check a call happened), when (stub a return), verifyNoMoreInteractions (no other calls)
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Arrays; // builds a quick list for test data

import org.junit.jupiter.api.Test; // JUnit 5 test method
import org.junit.jupiter.api.extension.ExtendWith; //  plug Mockito into JUnit 5
import org.mockito.InjectMocks; // creates the real object under test, injecting the mocks
import org.mockito.Mock; // creates a fake dependency
import org.mockito.junit.jupiter.MockitoExtension; // the Mockito+JUnit5 integration

import com.athena.sdvpapers.Author;
import com.athena.sdvpapers.repository.AuthorRepository;
import com.athena.sdvpapers.view.AuthorView;

// Turns on Mockito so @Mock / @InjectMocks get processed before each test
@ExtendWith(MockitoExtension.class)
class AuthorControllerTest {

	@Mock
	private AuthorRepository authorRepository; // no real MongoDB

	@Mock
	private AuthorView authorView; // no real GUI

	@InjectMocks
	private AuthorController authorController; // real controller, gets the two mocks above

	@Test
	void testAllAuthors() {
		Author author = new Author("1", "Bondavalli");
		// Stub: when the controller asks the repo for all return our one author
		when(authorRepository.findAll())
			.thenReturn(Arrays.asList(author));
		authorController.allAuthors();
		// Verify: controller passed that list to the view
		verify(authorView).showAllAuthors(Arrays.asList(author));
	}

	@Test
	void testNewAuthorWhenAuthorDoesNotAlreadyExist() {
		Author author = new Author("1", "Bondavalli");
		// Stub no author exists with this id
		when(authorRepository.findById("1")).thenReturn(null);
		authorController.newAuthor(author);
		// Verify: it saved, then told the view "added"
		verify(authorRepository).save(author);
		verify(authorView).authorAdded(author);
	}

	@Test
	void testNewAuthorWhenAuthorAlreadyExists() {
		Author authorToAdd = new Author("1", "Bondavalli");
		Author existingAuthor = new Author("1", "Existing");
		// Stub: an author with this id already exists
		when(authorRepository.findById("1")).thenReturn(existingAuthor);
		authorController.newAuthor(authorToAdd);
		// Verify: it showed the error with the existing author
		verify(authorView).showError(
			"Already existing author with id 1", existingAuthor);
		// Verify: it did NOT save or do anything else on the repo
		verifyNoMoreInteractions(authorRepository);
	}

	@Test
	void testDeleteAuthorWhenAuthorExists() {
		Author author = new Author("1", "Bondavalli");
		// Stub: the author exists
		when(authorRepository.findById("1")).thenReturn(author);
		authorController.deleteAuthor(author);
		// Verify it deleted, then told the view "removed"
		verify(authorRepository).delete("1");
		verify(authorView).authorRemoved(author);
	}

	@Test
	void testDeleteAuthorWhenAuthorDoesNotExist() {
		Author author = new Author("1", "Bondavalli");
		// Stub: the author does not exist
		when(authorRepository.findById("1")).thenReturn(null);
		authorController.deleteAuthor(author);
		// Verify it reported "not found" and touched nothing else on the repo
		verify(authorView).showErrorAuthorNotFound(author);
		verifyNoMoreInteractions(authorRepository);
	}
	@Test
	void testAddPaperToAuthorWhenAuthorExists() {
		Author author = new Author("1", "Bondavalli");
		when(authorRepository.findById("1")).thenReturn(author);
		authorController.addPaperToAuthor(author, "10");
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