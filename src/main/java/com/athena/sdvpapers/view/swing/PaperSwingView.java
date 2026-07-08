package com.athena.sdvpapers.view.swing;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.athena.sdvpapers.Paper;
import com.athena.sdvpapers.controller.PaperController;
import com.athena.sdvpapers.view.PaperView;

public class PaperSwingView extends JFrame implements PaperView {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtTitle;
	private JTextField txtYear;
	private JButton btnAdd;
	private JList<Paper> listPapers;
	private JScrollPane scrollPane;
	private JButton btnDeleteSelected;
	private JLabel lblErrorMessage;
	private DefaultListModel<Paper> listPapersModel;
	private transient PaperController paperController;

	DefaultListModel<Paper> getListPapersModel() {
		return listPapersModel;
	}

	public void setPaperController(PaperController paperController) {
		this.paperController = paperController;
	}

	public PaperSwingView() {
		setTitle("Paper View");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gblContentPane = new GridBagLayout();
		gblContentPane.columnWidths = new int[] { 0, 0, 0 };
		gblContentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gblContentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gblContentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gblContentPane);

		JLabel lblId = new JLabel("id");
		GridBagConstraints gbcLblId = new GridBagConstraints();
		gbcLblId.insets = new Insets(0, 0, 5, 5);
		gbcLblId.anchor = GridBagConstraints.EAST;
		gbcLblId.gridx = 0;
		gbcLblId.gridy = 0;
		contentPane.add(lblId, gbcLblId);

		txtId = new JTextField();
		KeyAdapter btnAddEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAdd.setEnabled(
					!txtId.getText().trim().isEmpty() &&
					!txtTitle.getText().trim().isEmpty() &&
					isValidYear(txtYear.getText()));
			}
		};
		txtId.addKeyListener(btnAddEnabler);
		txtId.setName("idTextBox");
		GridBagConstraints gbcTxtId = new GridBagConstraints();
		gbcTxtId.insets = new Insets(0, 0, 5, 0);
		gbcTxtId.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtId.gridx = 1;
		gbcTxtId.gridy = 0;
		contentPane.add(txtId, gbcTxtId);
		txtId.setColumns(10);

		JLabel lblTitle = new JLabel("title");
		GridBagConstraints gbcLblTitle = new GridBagConstraints();
		gbcLblTitle.anchor = GridBagConstraints.EAST;
		gbcLblTitle.insets = new Insets(0, 0, 5, 5);
		gbcLblTitle.gridx = 0;
		gbcLblTitle.gridy = 1;
		contentPane.add(lblTitle, gbcLblTitle);

		txtTitle = new JTextField();
		txtTitle.addKeyListener(btnAddEnabler);
		txtTitle.setName("titleTextBox");
		GridBagConstraints gbcTxtTitle = new GridBagConstraints();
		gbcTxtTitle.insets = new Insets(0, 0, 5, 0);
		gbcTxtTitle.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtTitle.gridx = 1;
		gbcTxtTitle.gridy = 1;
		contentPane.add(txtTitle, gbcTxtTitle);
		txtTitle.setColumns(10);

		JLabel lblYear = new JLabel("year");
		GridBagConstraints gbcLblYear = new GridBagConstraints();
		gbcLblYear.anchor = GridBagConstraints.EAST;
		gbcLblYear.insets = new Insets(0, 0, 5, 5);
		gbcLblYear.gridx = 0;
		gbcLblYear.gridy = 2;
		contentPane.add(lblYear, gbcLblYear);

		txtYear = new JTextField();
		txtYear.addKeyListener(btnAddEnabler);
		txtYear.setName("yearTextBox");
		GridBagConstraints gbcTxtYear = new GridBagConstraints();
		gbcTxtYear.insets = new Insets(0, 0, 5, 0);
		gbcTxtYear.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtYear.gridx = 1;
		gbcTxtYear.gridy = 2;
		contentPane.add(txtYear, gbcTxtYear);
		txtYear.setColumns(10);

		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		btnAdd.addActionListener(
			e -> paperController.newPaper(
				new Paper(
					txtId.getText(),
					txtTitle.getText(),
					Integer.parseInt(txtYear.getText()))));
		GridBagConstraints gbcBtnAdd = new GridBagConstraints();
		gbcBtnAdd.insets = new Insets(0, 0, 5, 0);
		gbcBtnAdd.gridwidth = 2;
		gbcBtnAdd.gridx = 0;
		gbcBtnAdd.gridy = 3;
		contentPane.add(btnAdd, gbcBtnAdd);

		scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.insets = new Insets(0, 0, 5, 0);
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridwidth = 2;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 4;
		contentPane.add(scrollPane, gbcScrollPane);

		listPapersModel = new DefaultListModel<>();
		listPapers = new JList<>(listPapersModel);
		listPapers.addListSelectionListener(
			e -> btnDeleteSelected.setEnabled(listPapers.getSelectedIndex() != -1));
		listPapers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPapers.setName("paperList");
		scrollPane.setViewportView(listPapers);

		btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.setEnabled(false);
		btnDeleteSelected.addActionListener(
			e -> paperController.deletePaper(listPapers.getSelectedValue()));
		GridBagConstraints gbcBtnDeleteSelected = new GridBagConstraints();
		gbcBtnDeleteSelected.insets = new Insets(0, 0, 5, 0);
		gbcBtnDeleteSelected.gridwidth = 2;
		gbcBtnDeleteSelected.gridx = 0;
		gbcBtnDeleteSelected.gridy = 5;
		contentPane.add(btnDeleteSelected, gbcBtnDeleteSelected);

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setForeground(Color.RED);
		lblErrorMessage.setName("errorMessageLabel");
		GridBagConstraints gbcLblErrorMessage = new GridBagConstraints();
		gbcLblErrorMessage.gridwidth = 2;
		gbcLblErrorMessage.insets = new Insets(0, 0, 0, 5);
		gbcLblErrorMessage.gridx = 0;
		gbcLblErrorMessage.gridy = 6;
		contentPane.add(lblErrorMessage, gbcLblErrorMessage);
	}

	@Override
	public void showAllPapers(List<Paper> papers) {
		papers.forEach(listPapersModel::addElement);
	}

	@Override
	public void showError(String message, Paper paper) {
		lblErrorMessage.setText(message + ": " + paper);
	}

	@Override
	public void paperAdded(Paper paper) {
		listPapersModel.addElement(paper);
		resetErrorLabel();
	}

	@Override
	public void paperRemoved(Paper paper) {
		listPapersModel.removeElement(paper);
		resetErrorLabel();
	}

	@Override
	public void showErrorPaperNotFound(Paper paper) {
		lblErrorMessage.setText("Paper not found: " + paper);
	}

	private void resetErrorLabel() {
		lblErrorMessage.setText(" ");
	}

	private boolean isValidYear(String text) {
		try {
			Integer.parseInt(text.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}