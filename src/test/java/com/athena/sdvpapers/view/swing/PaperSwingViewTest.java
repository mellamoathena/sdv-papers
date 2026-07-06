package com.athena.sdvpapers.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.athena.sdvpapers.Paper;
import com.athena.sdvpapers.controller.PaperController;

@RunWith(GUITestRunner.class)
public class PaperSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private PaperSwingView paperSwingView;

	@Mock
	private PaperController paperController;

	private AutoCloseable closeable;

	@Override
	protected void onSetUp() {
		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			paperSwingView = new PaperSwingView();
			paperSwingView.setPaperController(paperController);
			return paperSwingView;
		});
		window = new FrameFixture(robot(), paperSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test @GUITest
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("id"));
		window.textBox("idTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("title"));
		window.textBox("titleTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("year"));
		window.textBox("yearTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.list("paperList");
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testWhenIdTitleAndYearAreNonEmptyThenAddButtonShouldBeEnabled() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("titleTextBox").enterText("test");
		window.textBox("yearTextBox").enterText("2021");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
	}

	@Test
	public void testWhenAnyFieldIsBlankThenAddButtonShouldBeDisabled() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("titleTextBox").enterText("test");
		window.textBox("yearTextBox").enterText(" ");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
	}

	// New test for our isValidYear hardening: non-numeric year keeps Add disabled
	@Test
	public void testWhenYearIsNotANumberThenAddButtonShouldBeDisabled() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("titleTextBox").enterText("test");
		window.textBox("yearTextBox").enterText("abc");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAPaperIsSelected() {
		GuiActionRunner.execute(
			() -> paperSwingView.getListPapersModel().addElement(new Paper("1", "test", 2021)));
		window.list("paperList").selectItem(0);
		JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Selected"));
		deleteButton.requireEnabled();
		window.list("paperList").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	public void testShowAllPapersShouldAddPaperDescriptionsToTheList() {
		Paper paper1 = new Paper("1", "test1", 2021);
		Paper paper2 = new Paper("2", "test2", 2022);
		GuiActionRunner.execute(
			() -> paperSwingView.showAllPapers(Arrays.asList(paper1, paper2)));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(paper1.toString(), paper2.toString());
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		Paper paper = new Paper("1", "test1", 2021);
		GuiActionRunner.execute(
			() -> paperSwingView.showError("error message", paper));
		window.label("errorMessageLabel").requireText("error message: " + paper);
	}

	@Test
	public void testPaperAddedShouldAddThePaperToTheListAndResetTheErrorLabel() {
		Paper paper = new Paper("1", "test1", 2021);
		GuiActionRunner.execute(
			() -> paperSwingView.paperAdded(new Paper("1", "test1", 2021)));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(paper.toString());
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testPaperRemovedShouldRemoveThePaperFromListAndResetErrorLabel() {
		Paper paper1 = new Paper("1", "test1", 2021);
		Paper paper2 = new Paper("2", "test2", 2022);
		GuiActionRunner.execute(() -> {
			DefaultListModel<Paper> model = paperSwingView.getListPapersModel();
			model.addElement(paper1);
			model.addElement(paper2);
		});
		GuiActionRunner.execute(
			() -> paperSwingView.paperRemoved(new Paper("1", "test1", 2021)));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(paper2.toString());
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testAddButtonShouldDelegateToPaperControllerNewPaper() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("titleTextBox").enterText("test");
		window.textBox("yearTextBox").enterText("2021");
		window.button(JButtonMatcher.withText("Add")).click();
		verify(paperController).newPaper(new Paper("1", "test", 2021));
	}

	@Test
	public void testDeleteButtonShouldDelegateToPaperControllerDeletePaper() {
		Paper paper1 = new Paper("1", "test1", 2021);
		Paper paper2 = new Paper("2", "test2", 2022);
		GuiActionRunner.execute(() -> {
			DefaultListModel<Paper> model = paperSwingView.getListPapersModel();
			model.addElement(paper1);
			model.addElement(paper2);
		});
		window.list("paperList").selectItem(1);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		verify(paperController).deletePaper(paper2);
	}
}