package com.athena.sdvpapers;

import java.util.ArrayList;
import java.util.List;

public class Author {
	
	 private String name;
	 public Author(String name) {
	        this.name = name;
	    }

	  public String getName() {
	        return name;
	    }
	  private List<Paper> papers = new ArrayList<>();

	  public List<Paper> getPapers() {
	  	return papers;
	  }
	  public void addPaper(Paper paper) {
			papers.add(paper);
		}
}
