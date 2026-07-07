package com.athena.sdvpapers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	@Test
	public void testEqualsAndHashCode() {
		Author author1 = new Author("1", "Bondavalli");
		Author author2 = new Author("1", "Bondavalli");
		Author differentId = new Author("2", "Bondavalli");
		Author differentName = new Author("1", "Zoppi");

		// reflexive / equal
		assertEquals(author1, author1);
		assertEquals(author1, author2);
		assertEquals(author1.hashCode(), author2.hashCode());

		// not equal to null and to a different type
		assertNotEquals(author1, null);
		assertNotEquals(author1, "some string");

		// differing fields
		assertNotEquals(author1, differentId);
		assertNotEquals(author1, differentName);
	}

}