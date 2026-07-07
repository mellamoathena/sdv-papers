package com.athena.sdvpapers.view.swing;

import java.awt.Color;
import java.awt.EventQueue;
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

	private AuthorController authorController;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				AuthorSwingView frame = new AuthorSwingView();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	DefaultListModel<Author> getListAuthorsModel() {
		return listAuthorsModel;
	}

	public void setAuthorController(AuthorController authorController) {
		this.authorController = authorController;
	}

	public AuthorSwingView() {
		setTitle("Author View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
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
					!txtName.getText().trim().isEmpty());
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

		JLabel lblName = new JLabel("name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 1;
		contentPane.add(lblName, gbc_lblName);

		txtName = new JTextField();
		txtName.addKeyListener(btnAddEnabler);
		txtName.setName("nameTextBox");
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(0, 0, 5, 0);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 1;
		contentPane.add(txtName, gbc_txtName);
		txtName.setColumns(10);

		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		btnAdd.addActionListener(
			e -> authorController.newAuthor(new Author(txtId.getText(), txtName.getText())));
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 0);
		gbc_btnAdd.gridwidth = 2;
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 2;
		contentPane.add(btnAdd, gbc_btnAdd);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 3;
		contentPane.add(scrollPane, gbc_scrollPane);

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
		GridBagConstraints gbc_btnDeleteSelected = new GridBagConstraints();
		gbc_btnDeleteSelected.insets = new Insets(0, 0, 5, 0);
		gbc_btnDeleteSelected.gridwidth = 2;
		gbc_btnDeleteSelected.gridx = 0;
		gbc_btnDeleteSelected.gridy = 4;
		contentPane.add(btnDeleteSelected, gbc_btnDeleteSelected);

		txtPaperId = new JTextField();
		txtPaperId.setName("paperIdTextBox");
		txtPaperId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateLinkButton();
			}
		});
		GridBagConstraints gbc_txtPaperId = new GridBagConstraints();
		gbc_txtPaperId.insets = new Insets(0, 0, 5, 0);
		gbc_txtPaperId.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtPaperId.gridwidth = 2;
		gbc_txtPaperId.gridx = 0;
		gbc_txtPaperId.gridy = 5;
		contentPane.add(txtPaperId, gbc_txtPaperId);
		txtPaperId.setColumns(10);

		btnAddPaperToAuthor = new JButton("Add Paper To Author");
		btnAddPaperToAuthor.setEnabled(false);
		btnAddPaperToAuthor.addActionListener(
			e -> authorController.addPaperToAuthor(
				listAuthors.getSelectedValue(), txtPaperId.getText()));
		GridBagConstraints gbc_btnAddPaperToAuthor = new GridBagConstraints();
		gbc_btnAddPaperToAuthor.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddPaperToAuthor.gridwidth = 2;
		gbc_btnAddPaperToAuthor.gridx = 0;
		gbc_btnAddPaperToAuthor.gridy = 6;
		contentPane.add(btnAddPaperToAuthor, gbc_btnAddPaperToAuthor);

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setForeground(Color.RED);
		lblErrorMessage.setName("errorMessageLabel");
		GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
		gbc_lblErrorMessage.gridwidth = 2;
		gbc_lblErrorMessage.insets = new Insets(0, 0, 0, 5);
		gbc_lblErrorMessage.gridx = 0;
		gbc_lblErrorMessage.gridy = 7;
		contentPane.add(lblErrorMessage, gbc_lblErrorMessage);
	}

	private void updateLinkButton() {
		btnAddPaperToAuthor.setEnabled(
			listAuthors.getSelectedValue() != null &&
			!txtPaperId.getText().trim().isEmpty());
	}

	// --- AuthorView interface implementations ---

	@Override
	public void showAllAuthors(List<Author> authors) {
		authors.stream().forEach(listAuthorsModel::addElement);
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