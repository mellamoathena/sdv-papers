package com.athena.sdvpapers.repository;

import java.util.List;
import com.athena.sdvpapers.Author;

public interface AuthorRepository {

	List<Author> findAll();

	Author findById(String id);

	void save(Author author);

	void delete(String id);
}