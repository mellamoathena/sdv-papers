package com.athena.sdvpapers.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.athena.sdvpapers.Author;
import com.athena.sdvpapers.repository.AuthorRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class AuthorMongoRepository implements AuthorRepository {

	public static final String AUTHOR_COLLECTION_NAME = "author";
	public static final String SDVPAPERS_DB_NAME = "sdvpapers";

	private MongoCollection<Document> authorCollection;

	public AuthorMongoRepository(MongoClient client) {
		authorCollection = client
			.getDatabase(SDVPAPERS_DB_NAME)
			.getCollection(AUTHOR_COLLECTION_NAME);
	}

	@Override
	public List<Author> findAll() {
		return StreamSupport
			.stream(authorCollection.find().spliterator(), false)
			.map(this::fromDocumentToAuthor)
			.collect(Collectors.toList());
	}

	private Author fromDocumentToAuthor(Document d) {
		return new Author("" + d.get("id"), "" + d.get("name"));
	}

	@Override
	public Author findById(String id) {
		Document d = authorCollection.find(Filters.eq("id", id)).first();
		if (d != null)
			return fromDocumentToAuthor(d);
		return null;
	}

	@Override
	public void save(Author author) {
		authorCollection.insertOne(
			new Document()
				.append("id", author.getId())
				.append("name", author.getName()));
	}

	@Override
	public void delete(String id) {
		authorCollection.deleteOne(Filters.eq("id", id));
	}
}
