package com.athena.sdvpapers.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.athena.sdvpapers.Paper;
import com.athena.sdvpapers.repository.PaperRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class PaperMongoRepository implements PaperRepository {

	public static final String PAPER_COLLECTION_NAME = "paper";
	public static final String SDVPAPERS_DB_NAME = "sdvpapers";

	private MongoCollection<Document> paperCollection;

	public PaperMongoRepository(MongoClient client) {
		paperCollection = client
			.getDatabase(SDVPAPERS_DB_NAME)
			.getCollection(PAPER_COLLECTION_NAME);
	}

	@Override
	public List<Paper> findAll() {
		return StreamSupport
			.stream(paperCollection.find().spliterator(), false)
			.map(this::fromDocumentToPaper)
			.collect(Collectors.toList());
	}

	private Paper fromDocumentToPaper(Document d) {
		return new Paper("" + d.get("id"), "" + d.get("title"), (int) d.get("year"));
	}

	@Override
	public Paper findById(String id) {
		Document d = paperCollection.find(Filters.eq("id", id)).first();
		if (d != null)
			return fromDocumentToPaper(d);
		return null;
	}

	@Override
	public void save(Paper paper) {
		paperCollection.insertOne(
			new Document()
				.append("id", paper.getId())
				.append("title", paper.getTitle())
				.append("year", paper.getYear()));
	}

	@Override
	public void delete(String id) {
		paperCollection.deleteOne(Filters.eq("id", id));
	}
}