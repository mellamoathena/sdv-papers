package com.athena.sdvpapers.repository.mongo;

import static com.athena.sdvpapers.repository.mongo.PaperMongoRepository.PAPER_COLLECTION_NAME;
import static com.athena.sdvpapers.repository.mongo.PaperMongoRepository.SDVPAPERS_DB_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.athena.sdvpapers.Paper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Testcontainers
class PaperMongoRepositoryTestcontainersIT {

	@Container
	static final MongoDBContainer mongo =
		new MongoDBContainer("mongo:5")
			.withStartupAttempts(5)
			.withStartupTimeout(Duration.ofMinutes(5));

	private MongoClient client;
	private PaperMongoRepository paperRepository;
	private MongoCollection<Document> paperCollection;

	@BeforeEach
	void setup() {
		client = MongoClients.create(
			"mongodb://" + mongo.getHost() + ":" + mongo.getFirstMappedPort());
		paperRepository = new PaperMongoRepository(client);
		MongoDatabase database = client.getDatabase(SDVPAPERS_DB_NAME);
		// make sure we always start with a clean database
		database.drop();
		paperCollection = database.getCollection(PAPER_COLLECTION_NAME);
	}

	@AfterEach
	void tearDown() {
		client.close();
	}

	@Test
	void testFindAll() {
		addTestPaperToDatabase("1", "test1", 2021);
		addTestPaperToDatabase("2", "test2", 2022);
		assertThat(paperRepository.findAll())
			.containsExactly(
				new Paper("1", "test1", 2021),
				new Paper("2", "test2", 2022));
	}

	@Test
	void testSave() {
		Paper paper = new Paper("1", "added paper", 2021);
		paperRepository.save(paper);
		assertThat(readAllPapersFromDatabase())
			.containsExactly(paper);
	}

	private void addTestPaperToDatabase(String id, String title, int year) {
		paperCollection.insertOne(
			new Document()
				.append("id", id)
				.append("title", title)
				.append("year", year));
	}

	private List<Paper> readAllPapersFromDatabase() {
		return StreamSupport
			.stream(paperCollection.find().spliterator(), false)
			.map(d -> new Paper("" + d.get("id"), "" + d.get("title"), (int) d.get("year")))
			.collect(Collectors.toList());
	}
}