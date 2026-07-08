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

import com.athena.sdvpapers.Author;
import com.athena.sdvpapers.controller.AuthorController;
import com.athena.sdvpapers.view.AuthorView;

public class AuthorSwingView extends JFrame implements AuthorView {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField txtId;
	private JTextField txtName;
	private JButton btnAdd;
	private JList<Author> listAuthors;
	private JScrollPane scrollPane;
	private JButton btnDeleteSelected;
	private JTextField txtPaperId;
	private JButton btnAddPaperToAuthor;
	private JLabel lblErrorMessage;
	private DefaultListModel<Author> listAuthorsModel;
	private transient AuthorController authorController;

	DefaultListModel<Author> getListAuthorsModel() {
		return listAuthorsModel;
	}

	public void setAuthorController(AuthorController authorController) {
		this.authorController = authorController;
	}

	public void start() {
		setVisible(true);
		authorController.allAuthors();
	}

	public AuthorSwingView() {
		setTitle("Author View");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gblContentPane = new GridBagLayout();
		gblContentPane.columnWidths = new int[] { 0, 0, 0 };
		gblContentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gblContentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gblContentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
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
					!txtName.getText().trim().isEmpty());
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

		JLabel lblName = new JLabel("name");
		GridBagConstraints gbcLblName = new GridBagConstraints();
		gbcLblName.anchor = GridBagConstraints.EAST;
		gbcLblName.insets = new Insets(0, 0, 5, 5);
		gbcLblName.gridx = 0;
		gbcLblName.gridy = 1;
		contentPane.add(lblName, gbcLblName);

		txtName = new JTextField();
		txtName.addKeyListener(btnAddEnabler);
		txtName.setName("nameTextBox");
		GridBagConstraints gbcTxtName = new GridBagConstraints();
		gbcTxtName.insets = new Insets(0, 0, 5, 0);
		gbcTxtName.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtName.gridx = 1;
		gbcTxtName.gridy = 1;
		contentPane.add(txtName, gbcTxtName);
		txtName.setColumns(10);

		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		btnAdd.addActionListener(
			e -> authorController.newAuthor(new Author(txtId.getText(), txtName.getText())));
		GridBagConstraints gbcBtnAdd = new GridBagConstraints();
		gbcBtnAdd.insets = new Insets(0, 0, 5, 0);
		gbcBtnAdd.gridwidth = 2;
		gbcBtnAdd.gridx = 0;
		gbcBtnAdd.gridy = 2;
		contentPane.add(btnAdd, gbcBtnAdd);

		scrollPane = new JScrollPane();
		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		gbcScrollPane.insets = new Insets(0, 0, 5, 0);
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		gbcScrollPane.gridwidth = 2;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 3;
		contentPane.add(scrollPane, gbcScrollPane);

		listAuthorsModel = new DefaultListModel<>();
		listAuthors = new JList<>(listAuthorsModel);
		listAuthors.addListSelectionListener(e -> {
			btnDeleteSelected.setEnabled(listAuthors.getSelectedIndex() != -1);
			updateLinkButton();
		});
		listAuthors.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listAuthors.setName("authorList");
		scrollPane.setViewportView(listAuthors);

		btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.setEnabled(false);
		btnDeleteSelected.addActionListener(
			e -> authorController.deleteAuthor(listAuthors.getSelectedValue()));
		GridBagConstraints gbcBtnDeleteSelected = new GridBagConstraints();
		gbcBtnDeleteSelected.insets = new Insets(0, 0, 5, 0);
		gbcBtnDeleteSelected.gridwidth = 2;
		gbcBtnDeleteSelected.gridx = 0;
		gbcBtnDeleteSelected.gridy = 4;
		contentPane.add(btnDeleteSelected, gbcBtnDeleteSelected);

		txtPaperId = new JTextField();
		txtPaperId.setName("paperIdTextBox");
		txtPaperId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateLinkButton();
			}
		});
		GridBagConstraints gbcTxtPaperId = new GridBagConstraints();
		gbcTxtPaperId.insets = new Insets(0, 0, 5, 0);
		gbcTxtPaperId.fill = GridBagConstraints.HORIZONTAL;
		gbcTxtPaperId.gridwidth = 2;
		gbcTxtPaperId.gridx = 0;
		gbcTxtPaperId.gridy = 5;
		contentPane.add(txtPaperId, gbcTxtPaperId);
		txtPaperId.setColumns(10);

		btnAddPaperToAuthor = new JButton("Add Paper To Author");
		btnAddPaperToAuthor.setEnabled(false);
		btnAddPaperToAuthor.addActionListener(
			e -> authorController.addPaperToAuthor(
				listAuthors.getSelectedValue(), txtPaperId.getText()));
		GridBagConstraints gbcBtnAddPaperToAuthor = new GridBagConstraints();
		gbcBtnAddPaperToAuthor.insets = new Insets(0, 0, 5, 0);
		gbcBtnAddPaperToAuthor.gridwidth = 2;
		gbcBtnAddPaperToAuthor.gridx = 0;
		gbcBtnAddPaperToAuthor.gridy = 6;
		contentPane.add(btnAddPaperToAuthor, gbcBtnAddPaperToAuthor);

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setForeground(Color.RED);
		lblErrorMessage.setName("errorMessageLabel");
		GridBagConstraints gbcLblErrorMessage = new GridBagConstraints();
		gbcLblErrorMessage.gridwidth = 2;
		gbcLblErrorMessage.insets = new Insets(0, 0, 0, 5);
		gbcLblErrorMessage.gridx = 0;
		gbcLblErrorMessage.gridy = 7;
		contentPane.add(lblErrorMessage, gbcLblErrorMessage);
	}

	private void updateLinkButton() {
		btnAddPaperToAuthor.setEnabled(
			listAuthors.getSelectedValue() != null &&
			!txtPaperId.getText().trim().isEmpty());
	}

	@Override
	public void showAllAuthors(List<Author> authors) {
		authors.forEach(listAuthorsModel::addElement);
	}

	@Override
	public void showError(String message, Author author) {
		lblErrorMessage.setText(message + ": " + author);
	}

	@Override
	public void authorAdded(Author author) {
		listAuthorsModel.addElement(author);
		resetErrorLabel();
	}

	@Override
	public void authorRemoved(Author author) {
		listAuthorsModel.removeElement(author);
		resetErrorLabel();
	}

	@Override
	public void authorUpdated(Author author) {
		for (int i = 0; i < listAuthorsModel.getSize(); i++) {
			if (listAuthorsModel.getElementAt(i).getId().equals(author.getId())) {
				listAuthorsModel.setElementAt(author, i);
				break;
			}
		}
		resetErrorLabel();
	}

	@Override
	public void showErrorAuthorNotFound(Author author) {
		lblErrorMessage.setText("Author not found: " + author);
	}

	private void resetErrorLabel() {
		lblErrorMessage.setText(" ");
	}
}