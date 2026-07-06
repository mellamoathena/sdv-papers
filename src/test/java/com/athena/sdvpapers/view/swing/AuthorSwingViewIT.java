package com.athena.sdvpapers.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.athena.sdvpapers.Author;
import com.athena.sdvpapers.controller.AuthorController;
import com.athena.sdvpapers.repository.mongo.AuthorMongoRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@RunWith(GUITestRunner.class)
public class AuthorSwingViewIT extends AssertJSwingJUnitTestCase {

	// Testcontainers MongoDB started once for this test class
	private static MongoDBContainer mongo = new MongoDBContainer("mongo:5");

	private MongoClient mongoClient;
	private FrameFixture window;
	private AuthorSwingView authorSwingView;
	private AuthorController authorController;
	private AuthorMongoRepository authorRepository;

	@org.junit.BeforeClass
	public static void startContainer() {
		mongo.start();
	}

	@org.junit.AfterClass
	public static void stopContainer() {
		mongo.stop();
	}

	@Override
	protected void onSetUp() {
		mongoClient = MongoClients.create(
			"mongodb://" + mongo.getHost() + ":" + mongo.getFirstMappedPort());
		authorRepository = new AuthorMongoRepository(mongoClient);
		// start each test with a clean database
		for (Author author : authorRepository.findAll()) {
			authorRepository.delete(author.getId());
		}
		GuiActionRunner.execute(() -> {
			authorSwingView = new AuthorSwingView();
			authorController = new AuthorController(authorSwingView, authorRepository);
			authorSwingView.setAuthorController(authorController);
			return authorSwingView;
		});
		window = new FrameFixture(robot(), authorSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	@Test
	public void testAllAuthors() {
		Author author1 = new Author("1", "test1");
		Author author2 = new Author("2", "test2");
		authorRepository.save(author1);
		authorRepository.save(author2);
		GuiActionRunner.execute(() -> authorController.allAuthors());
		assertThat(window.list().contents())
			.containsExactly(author1.toString(), author2.toString());
	}

	@Test
	public void testAddButtonSuccess() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents())
			.containsExactly(new Author("1", "test").toString());
	}

	@Test
	public void testAddButtonError() {
		authorRepository.save(new Author("1", "existing"));
		window.textBox("idTextBox").enterText("1");
		window.textBox("nameTextBox").enterText("test");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents()).isEmpty();
		window.label("errorMessageLabel")
			.requireText("Already existing author with id 1: " + new Author("1", "existing"));
	}

	@Test
	public void testDeleteButtonSuccess() {
		GuiActionRunner.execute(
			() -> authorController.newAuthor(new Author("1", "toremove")));
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.list().contents()).isEmpty();
	}

	@Test
	public void testDeleteButtonError() {
		Author author = new Author("1", "non existent");
		GuiActionRunner.execute(
			() -> authorSwingView.getListAuthorsModel().addElement(author));
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.list().contents())
			.containsExactly(author.toString());
		window.label("errorMessageLabel")
			.requireText("Author not found: " + author);
	}
}