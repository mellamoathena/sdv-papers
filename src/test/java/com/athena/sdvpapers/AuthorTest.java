package com.athena.sdvpapers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AuthorTest {

	@Test
	public void testAuthorHasName() {
		Author author = new Author("Bondavalli");
		assertEquals("Bondavalli", author.getName());
	}
	@Test
	public void testNewAuthorHasNoPapers() {
		Author author = new Author("Bondavalli");
		assertTrue(author.getPapers().isEmpty());
	}
	@Test
	public void testAddPaperToAuthor() {
		Author author = new Author("Bondavalli");
		Paper paper = new Paper("Anomaly Detection for IDPS", 2021);
		author.addPaper(paper);
		assertTrue(author.getPapers().contains(paper));
	}

}