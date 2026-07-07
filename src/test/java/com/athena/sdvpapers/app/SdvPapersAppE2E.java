package com.athena.sdvpapers.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@RunWith(GUITestRunner.class)
public class SdvPapersAppE2E extends AssertJSwingJUnitTestCase {

	@SuppressWarnings("resource")
	private static final MongoDBContainer mongo = new MongoDBContainer("mongo:5");

	private static final String DB_NAME = "test-db";
	private static final String COLLECTION_NAME = "test-collection";

	private MongoClient mongoClient;
	private FrameFixture window;

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
		String containerHost = mongo.getHost();
		Integer containerPort = mongo.getFirstMappedPort();
		mongoClient = MongoClients.create(
			"mongodb://" + containerHost + ":" + containerPort);
		// start with an empty database
		mongoClient.getDatabase(DB_NAME).drop();

		// pre-populate BEFORE launching, so start() shows them
		addTestAuthorToDatabase("1", "first author");
		addTestAuthorToDatabase("2", "second author");

		// launch the app through its main, referencing it only by class-name string
		application("com.athena.sdvpapers.app.SdvPapersApp")
			.withArgs(
				"--mongo-host=" + containerHost,
				"--mongo-port=" + containerPort.toString(),
				"--db-name=" + DB_NAME,
				"--db-collection=" + COLLECTION_NAME)
			.start();

		// find the app's window by its title
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Author View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	@Test @GUITest
	public void testOnStartAllDatabaseElementsAreShown() {
		assertThat(window.list().contents())
			.anyMatch(e -> e.contains("1") && e.contains("first author"))
			.anyMatch(e -> e.contains("2") && e.contains("second author"));
	}

	@Test @GUITest
	public void testAddButtonSuccess() {
		window.textBox("idTextBox").enterText("10");
		window.textBox("nameTextBox").enterText("new author");
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents())
			.anyMatch(e -> e.contains("10") && e.contains("new author"));
	}

	@Test @GUITest
	public void testAddPaperToAuthorSuccess() {
		window.list("authorList").selectItem(0);
		window.textBox("paperIdTextBox").enterText("100");
		window.button(JButtonMatcher.withText("Add Paper To Author")).click();
		assertThat(window.list().contents())
			.anyMatch(e -> e.contains("100"));
	}

	private void addTestAuthorToDatabase(String id, String name) {
		mongoClient.getDatabase(DB_NAME)
			.getCollection(COLLECTION_NAME)
			.insertOne(new Document()
				.append("id", id)
				.append("name", name));
	}
}