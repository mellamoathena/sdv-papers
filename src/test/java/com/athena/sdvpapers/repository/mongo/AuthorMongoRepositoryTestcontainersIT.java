package com.athena.sdvpapers.repository.mongo;

import static com.athena.sdvpapers.repository.mongo.AuthorMongoRepository.AUTHOR_COLLECTION_NAME;
import static com.athena.sdvpapers.repository.mongo.AuthorMongoRepository.SDVPAPERS_DB_NAME;
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

import com.athena.sdvpapers.Author;
import com.athena.sdvpapers.Paper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Testcontainers
class AuthorMongoRepositoryTestcontainersIT {

	@SuppressWarnings("resource")
	@Container
	static final MongoDBContainer mongo =
		new MongoDBContainer("mongo:5")
			.withStartupAttempts(5)
			.withStartupTimeout(Duration.ofMinutes(5));

	private MongoClient client;
	private AuthorMongoRepository authorRepository;
	private MongoCollection<Document> authorCollection;

	@BeforeEach
	void setup() {
		client = MongoClients.create(
			"mongodb://" + mongo.getHost() + ":" + mongo.getFirstMappedPort());
		authorRepository = new AuthorMongoRepository(client);
		MongoDatabase database = client.getDatabase(SDVPAPERS_DB_NAME);
		database.drop();
		authorCollection = database.getCollection(AUTHOR_COLLECTION_NAME);
	}

	@AfterEach
	void tearDown() {
		client.close();
	}

	@Test
	void testFindAll() {
		addTestAuthorToDatabase("1", "test1");
		addTestAuthorToDatabase("2", "test2");
		assertThat(authorRepository.findAll())
			.containsExactly(
				new Author("1", "test1"),
				new Author("2", "test2"));
	}

	@Test
	void testSave() {
		Author author = new Author("1", "added author");
		authorRepository.save(author);
		assertThat(readAllAuthorsFromDatabase())
			.containsExactly(author);
	}
	@Test
	void testFindById() {
		addTestAuthorToDatabase("1", "test1");
		addTestAuthorToDatabase("2", "test2");
		assertThat(authorRepository.findById("2"))
			.isEqualTo(new Author("2", "test2"));
	}

	@Test
	void testDelete() {
		addTestAuthorToDatabase("1", "test1");
		authorRepository.delete("1");
		assertThat(readAllAuthorsFromDatabase())
			.isEmpty();
	}

	private void addTestAuthorToDatabase(String id, String name) {
		authorCollection.insertOne(
			new Document()
				.append("id", id)
				.append("name", name));
	}
	@Test
	void testFindByIdNotFound() {
		assertThat(authorRepository.findById("1")).isNull();
	}
	private List<Author> readAllAuthorsFromDatabase() {
		return StreamSupport
			.stream(authorCollection.find().spliterator(), false)
			.map(d -> new Author("" + d.get("id"), "" + d.get("name")))
			.collect(Collectors.toList());
	}
	@Test
	void testSaveAuthorWithPapersStoresPaperIds() {
		Author author = new Author("1", "Bondavalli");
		author.addPaper(new Paper("10", "IDPS for SDV", 2021));
		author.addPaper(new Paper("20", "Anomaly Detection", 2022));
		authorRepository.save(author);
		Document savedDoc = authorCollection.find().first();
		assertThat(savedDoc.getList("paperIds", String.class))
			.containsExactly("10", "20");
	}
}