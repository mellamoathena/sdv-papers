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

	private PaperController paperController;
	
	DefaultListModel<Paper> getListPapersModel() {
		return listPapersModel;
	}

	public void setPaperController(PaperController paperController) {
		this.paperController = paperController;
	}

	public PaperSwingView() {
		setTitle("Paper View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblId = new JLabel("id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 0;
		contentPane.add(lblId, gbc_lblId);

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
		GridBagConstraints gbc_txtId = new GridBagConstraints();
		gbc_txtId.insets = new Insets(0, 0, 5, 0);
		gbc_txtId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtId.gridx = 1;
		gbc_txtId.gridy = 0;
		contentPane.add(txtId, gbc_txtId);
		txtId.setColumns(10);

		JLabel lblTitle = new JLabel("title");
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.anchor = GridBagConstraints.EAST;
		gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 1;
		contentPane.add(lblTitle, gbc_lblTitle);

		txtTitle = new JTextField();
		txtTitle.addKeyListener(btnAddEnabler);
		txtTitle.setName("titleTextBox");
		GridBagConstraints gbc_txtTitle = new GridBagConstraints();
		gbc_txtTitle.insets = new Insets(0, 0, 5, 0);
		gbc_txtTitle.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtTitle.gridx = 1;
		gbc_txtTitle.gridy = 1;
		contentPane.add(txtTitle, gbc_txtTitle);
		txtTitle.setColumns(10);

		JLabel lblYear = new JLabel("year");
		GridBagConstraints gbc_lblYear = new GridBagConstraints();
		gbc_lblYear.anchor = GridBagConstraints.EAST;
		gbc_lblYear.insets = new Insets(0, 0, 5, 5);
		gbc_lblYear.gridx = 0;
		gbc_lblYear.gridy = 2;
		contentPane.add(lblYear, gbc_lblYear);

		txtYear = new JTextField();
		txtYear.addKeyListener(btnAddEnabler);
		txtYear.setName("yearTextBox");
		GridBagConstraints gbc_txtYear = new GridBagConstraints();
		gbc_txtYear.insets = new Insets(0, 0, 5, 0);
		gbc_txtYear.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtYear.gridx = 1;
		gbc_txtYear.gridy = 2;
		contentPane.add(txtYear, gbc_txtYear);
		txtYear.setColumns(10);

		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		btnAdd.addActionListener(
			e -> paperController.newPaper(
				new Paper(
					txtId.getText(),
					txtTitle.getText(),
					Integer.parseInt(txtYear.getText()))));
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridwidth = 2;
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 3;
		contentPane.add(btnAdd, gbc_btnAdd);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 4;
		contentPane.add(scrollPane, gbc_scrollPane);

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
		GridBagConstraints gbc_btnDeleteSelected = new GridBagConstraints();
		gbc_btnDeleteSelected.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteSelected.gridwidth = 2;
		gbc_btnDeleteSelected.gridx = 0;
		gbc_btnDeleteSelected.gridy = 5;
		contentPane.add(btnDeleteSelected, gbc_btnDeleteSelected);

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setForeground(Color.RED);
		lblErrorMessage.setName("errorMessageLabel");
		GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
		gbc_lblErrorMessage.gridwidth = 2;
		gbc_lblErrorMessage.insets = new Insets(0, 0, 0, 5);
		gbc_lblErrorMessage.gridx = 0;
		gbc_lblErrorMessage.gridy = 6;
		contentPane.add(lblErrorMessage, gbc_lblErrorMessage);
	}

	@Override
	public void showAllPapers(List<Paper> papers) {
		papers.stream().forEach(listPapersModel::addElement);
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

	// No book template for an int field; validate year to avoid parse crash
	private boolean isValidYear(String text) {
		try {
			Integer.parseInt(text.trim());
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}