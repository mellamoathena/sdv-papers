package com.athena.sdvpapers.repository;

import java.util.List;
import com.athena.sdvpapers.Paper;

public interface PaperRepository {

	List<Paper> findAll();

	Paper findById(String id);

	void save(Paper paper);

	void delete(String id);
}