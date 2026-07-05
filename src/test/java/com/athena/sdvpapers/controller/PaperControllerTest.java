package com.athena.sdvpapers.controller;

// Mockito static helpers
import static org.mockito.Mockito.verify; // assert a method was called on a mock
import static org.mockito.Mockito.when; // stub what a mock returns
import static org.mockito.Mockito.verifyNoMoreInteractions; // assert nothing else was called

import java.util.Arrays; // Arrays.asList(...) to build test lists

import org.junit.jupiter.api.Test; // JUnit 5 test marker
import org.junit.jupiter.api.extension.ExtendWith; // plug Mockito into JUnit 5
import org.mockito.InjectMocks; // build the real controller with mocks injected
import org.mockito.Mock; // create a fake dependency
import org.mockito.junit.jupiter.MockitoExtension; // Mockito + JUnit 5 glue

import com.athena.sdvpapers.Paper;
import com.athena.sdvpapers.repository.PaperRepository;
import com.athena.sdvpapers.view.PaperView;

// Enables @Mock / @InjectMocks processing before each test
@ExtendWith(MockitoExtension.class)
class PaperControllerTest {

	@Mock
	private PaperRepository paperRepository; // fake repo. no real MongoDB

	@Mock
	private PaperView paperView; // fake view no real GUI

	@InjectMocks
	private PaperController paperController; // real controller, gets the two mocks

	@Test
	void testAllPapers() {
		Paper paper = new Paper("1", "IDPS for SDV", 2021);
		// Stub: repo returns a list with our one paper
		when(paperRepository.findAll())
			.thenReturn(Arrays.asList(paper));
		paperController.allPapers();
		// Verify controller passed that list to the view
		verify(paperView).showAllPapers(Arrays.asList(paper));
	}

	@Test
	void testNewPaperWhenPaperDoesNotAlreadyExist() {
		Paper paper = new Paper("1", "IDPS for SDV", 2021);
		// Stub no paper exists with this id
		when(paperRepository.findById("1")).thenReturn(null);
		paperController.newPaper(paper);
		// Verify it saved, then told the view "added"
		verify(paperRepository).save(paper);
		verify(paperView).paperAdded(paper);
	}

	@Test
	void testNewPaperWhenPaperAlreadyExists() {
		Paper paperToAdd = new Paper("1", "IDPS for SDV", 2021);
		Paper existingPaper = new Paper("1", "Existing", 2020);
		// Stub: a paper with this id already exists
		when(paperRepository.findById("1")).thenReturn(existingPaper);
		paperController.newPaper(paperToAdd);
		// Verify it showed the error with the existing paper...
		verify(paperView).showError(
			"Already existing paper with id 1", existingPaper);
		// ...and did nothing else on the repo (no save)
		verifyNoMoreInteractions(paperRepository);
	}

	@Test
	void testDeletePaperWhenPaperExists() {
		Paper paper = new Paper("1", "IDPS for SDV", 2021);
		// Stub: the paper exists
		when(paperRepository.findById("1")).thenReturn(paper);
		paperController.deletePaper(paper);
		// Verify: it deleted, then told the view "removed"
		verify(paperRepository).delete("1");
		verify(paperView).paperRemoved(paper);
	}

	@Test
	void testDeletePaperWhenPaperDoesNotExist() {
		Paper paper = new Paper("1", "IDPS for SDV", 2021);
		// Stub: the paper does not exist
		when(paperRepository.findById("1")).thenReturn(null);
		paperController.deletePaper(paper);
		// Verify: it reported "not found" and touched nothing else on the repo
		verify(paperView).showErrorPaperNotFound(paper);
		verifyNoMoreInteractions(paperRepository);
	}
}