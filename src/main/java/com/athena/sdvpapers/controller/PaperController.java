package com.athena.sdvpapers.controller;

import com.athena.sdvpapers.Paper;
import com.athena.sdvpapers.repository.PaperRepository; // the interface, not the Mongo class
import com.athena.sdvpapers.view.PaperView; // the UI contract

public class PaperController {

	// Dependencies held as interfaces, so they can be mocked in tests
	private PaperView paperView;
	private PaperRepository paperRepository;

	// Constructor injection, caller (or Mockito) passes in the view and repository
	public PaperController(PaperView paperView, PaperRepository paperRepository) {
		this.paperView = paperView;
		this.paperRepository = paperRepository;
	}

	// Ask the repo for all papers, hand them to the view to display
	public void allPapers() {
		paperView.showAllPapers(paperRepository.findAll());
	}

	public void newPaper(Paper paper) {
		// Defensive check: is there already a paper with this id?
		Paper existing = paperRepository.findById(paper.getId());
		if (existing != null) {
			// Yes -> tell the view to show an error, and stop (don't save)
			paperView.showError(
				"Already existing paper with id " + paper.getId(),
				existing);
			return;
		}
		// No -> save it, then notify the view it was added
		paperRepository.save(paper);
		paperView.paperAdded(paper);
	}

	public void deletePaper(Paper paper) {
		// Defensive check: does this paper actually exist?
		if (paperRepository.findById(paper.getId()) == null) {
			// No -> tell the view "not found", and stop (don't delete)
			paperView.showErrorPaperNotFound(paper);
			return;
		}
		// Yes -> delete it then notify the view it was removed
		paperRepository.delete(paper.getId());
		paperView.paperRemoved(paper);
	}
}