package com.athena.sdvpapers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PaperTest {

	@Test
	public void testPaperHasTitleAndYear() {
		Paper paper = new Paper("1", "Anomaly Detection for IDPS", 2021);
		assertEquals("1", paper.getId());
		assertEquals("Anomaly Detection for IDPS", paper.getTitle());
		assertEquals(2021, paper.getYear());
	}
	@Test
	public void testNewPaperHasNoAuthors() {
		Paper paper = new Paper("1","Anomaly Detection for IDPS", 2021);
		assertTrue(paper.getAuthors().isEmpty());
	}

	@Test
	public void testAddAuthorToPaper() {
		Paper paper = new Paper("1","Anomaly Detection for IDPS", 2021);
		Author author = new Author("1", "Bondavalli");
		paper.addAuthor(author);
		assertTrue(paper.getAuthors().contains(author));
	}
	@Test
	public void testEqualsAndHashCode() {
		Paper paper1 = new Paper("1", "IDPS for SDV", 2021);
		Paper paper2 = new Paper("1", "IDPS for SDV", 2021);
		Paper differentId = new Paper("2", "IDPS for SDV", 2021);
		Paper differentTitle = new Paper("1", "Other", 2021);
		Paper differentYear = new Paper("1", "IDPS for SDV", 2022);

		assertEquals(paper1, paper1);
		assertEquals(paper1, paper2);
		assertEquals(paper1.hashCode(), paper2.hashCode());

		assertNotEquals(paper1, null);
		assertNotEquals(paper1, "some string");

		assertNotEquals(paper1, differentId);
		assertNotEquals(paper1, differentTitle);
		assertNotEquals(paper1, differentYear);
	}
}