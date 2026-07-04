package com.athena.sdvpapers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Author {
	private String id;
	private String name;
	private List<Paper> papers = new ArrayList<>();

	public Author(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Paper> getPapers() {
		return papers;
	}

	public void addPaper(Paper paper) {
		papers.add(paper);
	}
}
