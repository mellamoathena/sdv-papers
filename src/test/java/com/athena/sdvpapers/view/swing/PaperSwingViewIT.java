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

import com.athena.sdvpapers.Paper;
import com.athena.sdvpapers.controller.PaperController;
import com.athena.sdvpapers.repository.mongo.PaperMongoRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@RunWith(GUITestRunner.class)
public class PaperSwingViewIT extends AssertJSwingJUnitTestCase {

	private static MongoDBContainer mongo = new MongoDBContainer("mongo:5");

	private MongoClient mongoClient;
	private FrameFixture window;
	private PaperSwingView paperSwingView;
	private PaperController paperController;
	private PaperMongoRepository paperRepository;

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
		paperRepository = new PaperMongoRepository(mongoClient);
		for (Paper paper : paperRepository.findAll()) {
			paperRepository.delete(paper.getId());
		}
		GuiActionRunner.execute(() -> {
			paperSwingView = new PaperSwingView();
			paperController = new PaperController(paperSwingView, paperRepository);
			paperSwingView.setPaperController(paperController);
			return paperSwingView;
		});
		window = new FrameFixture(robot(), paperSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	@Test
	public void testAllPapers() {
		Paper paper1 = new Paper("1", "test1", 2021);
		Paper paper2 = new Paper("2", "test2", 2022);
		paperRepository.save(paper1);
		paperRepository.save(paper2);
		GuiActionRunner.execute(() -> paperController.allPapers());
		assertThat(window.list().contents())
			.containsExactly(paper1.toString(), paper2.toString());
	}

	@Test
	public void testAddButtonSuccess() {
		window.textBox("idTextBox").enterText("1");
		window.textBox("titleTextBox").enterText("test");
		window.textBox("yearTextBox").enterText("2021");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents())
			.containsExactly(new Paper("1", "test", 2021).toString());
	}

	@Test
	public void testAddButtonError() {
		paperRepository.save(new Paper("1", "existing", 2020));
		window.textBox("idTextBox").enterText("1");
		window.textBox("titleTextBox").enterText("test");
		window.textBox("yearTextBox").enterText("2021");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents()).isEmpty();
		window.label("errorMessageLabel")
			.requireText("Already existing paper with id 1: " + new Paper("1", "existing", 2020));
	}

	@Test
	public void testDeleteButtonSuccess() {
		GuiActionRunner.execute(
			() -> paperController.newPaper(new Paper("1", "toremove", 2021)));
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.list().contents()).isEmpty();
	}

	@Test
	public void testDeleteButtonError() {
		Paper paper = new Paper("1", "non existent", 2021);
		GuiActionRunner.execute(
			() -> paperSwingView.getListPapersModel().addElement(paper));
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.list().contents())
			.containsExactly(paper.toString());
		window.label("errorMessageLabel")
			.requireText("Paper not found: " + paper);
	}
}