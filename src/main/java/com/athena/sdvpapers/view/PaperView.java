package com.athena.sdvpapers.view;

import java.util.List;
import com.athena.sdvpapers.Paper;

public interface PaperView {

	void showAllPapers(List<Paper> papers);

	void paperAdded(Paper paper);

	void paperRemoved(Paper paper);

	void showError(String message, Paper paper);

	void showErrorPaperNotFound(Paper paper);
}