package com.athena.sdvpapers.view.swing;

import static org.assertj.core.api.Assertions.assertThat; // fluent assertions
import static org.mockito.Mockito.verify; // verify controller calls

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher; // find buttons by text
import org.assertj.swing.core.matcher.JLabelMatcher; // find labels by text
import org.assertj.swing.edt.GuiActionRunner; // run code safely on the EDT
import org.assertj.swing.fixture.FrameFixture; // wraps the window for interaction
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner; // JUnit 4 runner for GUI tests
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase; // base class
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.athena.sdvpapers.Author;
import com.athena.sdvpapers.controller.AuthorController;

@RunWith(GUITestRunner.class)
public class AuthorSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private AuthorSwingView authorSwingView;

	@Mock
	private AuthorController authorController; // fake controller

	private AutoCloseable closeable;

	@Override
	protected void onSetUp() {
		closeable = MockitoAnnotations.openMocks(this);
		// Build the view on the EDT, inject the mock controller
		GuiActionRunner.execute(() -> {
			authorSwingView = new AuthorSwingView();
			authorSwingView.setAuthorController(authorController);
			return authorSwingView;
		});
		window = new FrameFixture(robot(), authorSwingView);
		window.show(); // display the frame for testing
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test @GUITest
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("id"));
		window.textBox("idTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("name"));
		window.textBox("nameTextBox").requireEnabled();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.list("authorList");
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testWhenIdAndNameAreNonEmptyThenAddButtonShouldBeEnabled() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
	}

	@Test
	public void testWhenEitherIdOrNameAreBlankThenAddButtonShouldBeDisabled() {
		JTextComponentFixture idTextBox = window.textBox("idTextBox");
		JTextComponentFixture nameTextBox = window.textBox("nameTextBox");

		idTextBox.enterText("1");
		nameTextBox.enterText(" ");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();

		idTextBox.setText("");
		nameTextBox.setText("");

		idTextBox.enterText(" ");
		nameTextBox.enterText("test");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAnAuthorIsSelected() {
		GuiActionRunner.execute(
			() -> authorSwingView.getListAuthorsModel().addElement(new Author("1", "test")));
		window.list("authorList").selectItem(0);
		JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Selected"));
		deleteButton.requireEnabled();
		window.list("authorList").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	public void testShowAllAuthorsShouldAddAuthorDescriptionsToTheList() {
		Author author1 = new Author("1", "test1");
		Author author2 = new Author("2", "test2");
		GuiActionRunner.execute(
			() -> authorSwingView.showAllAuthors(Arrays.asList(author1, author2)));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(author1.toString(), author2.toString());
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		Author author = new Author("1", "test1");
		GuiActionRunner.execute(
			() -> authorSwingView.showError("error message", author));
		window.label("errorMessageLabel").requireText("error message: " + author);
	}

	@Test
	public void testAuthorAddedShouldAddTheAuthorToTheListAndResetTheErrorLabel() {
		Author author = new Author("1", "test1");
		GuiActionRunner.execute(
			() -> authorSwingView.authorAdded(new Author("1", "test1")));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(author.toString());
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testAuthorRemovedShouldRemoveTheAuthorFromListAndResetErrorLabel() {
		Author author1 = new Author("1", "test1");
		Author author2 = new Author("2", "test2");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Author> model = authorSwingView.getListAuthorsModel();
			model.addElement(author1);
			model.addElement(author2);
		});
		GuiActionRunner.execute(
			() -> authorSwingView.authorRemoved(new Author("1", "test1")));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(author2.toString());
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testAddButtonShouldDelegateToAuthorControllerNewAuthor() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test");
		window.button(JButtonMatcher.withText("Add")).click();
		verify(authorController).newAuthor(new Author("1", "test"));
	}

	@Test
	public void testDeleteButtonShouldDelegateToAuthorControllerDeleteAuthor() {
		Author author1 = new Author("1", "test1");
		Author author2 = new Author("2", "test2");
		GuiActionRunner.execute(() -> {
			DefaultListModel<Author> model = authorSwingView.getListAuthorsModel();
			model.addElement(author1);
			model.addElement(author2);
		});
		window.list("authorList").selectItem(1);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		verify(authorController).deleteAuthor(author2);
	}
	
	@Test
	public void testAuthorUpdatedShouldReplaceTheAuthorInTheListAndResetErrorLabel() {
		Author author = new Author("1", "test1");
		GuiActionRunner.execute(() -> authorSwingView.getListAuthorsModel().addElement(author));
		Author updated = new Author("1", "test1");
		updated.addPaperId("10");
		GuiActionRunner.execute(() -> authorSwingView.authorUpdated(updated));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(updated.toString());
		window.label("errorMessageLabel").requireText(" ");
	}
	@Test
	public void testAddPaperToAuthorButtonEnabledOnlyWhenAuthorSelectedAndPaperIdNonEmpty() {
		Author author = new Author("1", "test1");
		GuiActionRunner.execute(() -> authorSwingView.getListAuthorsModel().addElement(author));
		JButtonFixture linkButton =
			window.button(JButtonMatcher.withText("Add Paper To Author"));

		// no selection, no paper id -> disabled
		linkButton.requireDisabled();

		// select author but paper id empty -> still disabled
		window.list("authorList").selectItem(0);
		linkButton.requireDisabled();

		// enter paper id with author selected -> enabled
		window.textBox("paperIdTextBox").enterText("10");
		linkButton.requireEnabled();

		// clear selection -> disabled again
		window.list("authorList").clearSelection();
		linkButton.requireDisabled();
	}

	@Test
	public void testAddPaperToAuthorButtonShouldDelegateToController() {
		Author author = new Author("1", "test1");
		GuiActionRunner.execute(() -> authorSwingView.getListAuthorsModel().addElement(author));
		window.list("authorList").selectItem(0);
		window.textBox("paperIdTextBox").enterText("10");
		window.button(JButtonMatcher.withText("Add Paper To Author")).click();
		verify(authorController).addPaperToAuthor(author, "10");
	}
	@Test
	public void testAuthorUpdatedWhenAuthorNotInListDoesNothingToList() {
		Author existing = new Author("1", "test1");
		GuiActionRunner.execute(() -> authorSwingView.getListAuthorsModel().addElement(existing));
		Author notInList = new Author("2", "other");
		notInList.addPaperId("10");
		GuiActionRunner.execute(() -> authorSwingView.authorUpdated(notInList));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(existing.toString());
	}
}