package com.athena.sdvpapers.app;

import java.awt.EventQueue;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.athena.sdvpapers.controller.AuthorController;
import com.athena.sdvpapers.repository.mongo.AuthorMongoRepository;
import com.athena.sdvpapers.view.swing.AuthorSwingView;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class SdvPapersApp implements Callable<Integer> {

	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = AuthorMongoRepository.SDVPAPERS_DB_NAME;

	@Option(names = { "--db-collection" }, description = "Collection name")
	private String collectionName = AuthorMongoRepository.AUTHOR_COLLECTION_NAME;

	public static void main(String[] args) {
		new CommandLine(new SdvPapersApp()).execute(args);
	}

	@Override
	public Integer call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				MongoClient client = MongoClients.create(
					"mongodb://" + mongoHost + ":" + mongoPort);
				AuthorMongoRepository authorRepository =
					new AuthorMongoRepository(client, databaseName, collectionName);
				AuthorSwingView authorView = new AuthorSwingView();
				AuthorController authorController =
					new AuthorController(authorView, authorRepository);
				authorView.setAuthorController(authorController);
				authorView.start();
			} catch (Exception e) {
				Logger.getLogger(SdvPapersApp.class.getName())
					.log(Level.SEVERE, "Exception while starting application", e);
			}
		});
		return 0;
	}
}