package com.athena.sdvpapers;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PaperTest {

	@Test
	public void testPaperHasTitleAndYear() {
		Paper paper = new Paper("Anomaly Detection for IDPS", 2021);
		assertEquals("Anomaly Detection for IDPS", paper.getTitle());
		assertEquals(2021, paper.getYear());
	}
	@Test
	public void testNewPaperHasNoAuthors() {
		Paper paper = new Paper("Anomaly Detection for IDPS", 2021);
		assertTrue(paper.getAuthors().isEmpty());
	}

	@Test
	public void testAddAuthorToPaper() {
		Paper paper = new Paper("Anomaly Detection for IDPS", 2021);
		Author author = new Author("Bondavalli");
		paper.addAuthor(author);
		assertTrue(paper.getAuthors().contains(author));
	}
}