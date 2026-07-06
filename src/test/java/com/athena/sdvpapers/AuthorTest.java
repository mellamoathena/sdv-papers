package com.athena.sdvpapers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class AuthorTest {

	@Test
	public void testAuthorHasIdAndName() {
		Author author = new Author("1", "Bondavalli");
		assertEquals("1", author.getId());
		assertEquals("Bondavalli", author.getName());
	}
	
	@Test
	public void testNewAuthorHasNoPapers() {
		Author author = new Author("1", "Bondavalli");
		assertTrue(author.getPapers().isEmpty());
	}
	@Test
	public void testAddPaperToAuthor() {
		Author author = new Author("1", "Bondavalli");
		Paper paper = new Paper("1","Anomaly Detection for IDPS", 2021);
		author.addPaper(paper);
		assertTrue(author.getPapers().contains(paper));
	}
	@Test
	public void testNewAuthorHasNoPaperIds() {
		Author author = new Author("1", "Bondavalli");
		assertTrue(author.getPaperIds().isEmpty());
	}

	@Test
	public void testAddPaperId() {
		Author author = new Author("1", "Bondavalli");
		author.addPaperId("10");
		assertTrue(author.getPaperIds().contains("10"));
	}

}