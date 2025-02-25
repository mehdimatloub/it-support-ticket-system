package com.example;

import javax.swing.*;
import java.awt.*;

public class TicketForm extends JFrame {
    private MainFrame mainFrame;
    private Ticket existingTicket; // Sera null en mode création, non-null en mode modification

    public TicketForm(MainFrame mainFrame, Ticket ticket) {
        this.mainFrame = mainFrame;
        this.existingTicket = ticket;
        initUI();
    }

    private void initUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle(existingTicket == null ? "Créer un Ticket" : "Modifier un Ticket");
        setSize(400, 350);  // Augmentation de la hauteur pour inclure "Supprimer"
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Champ Titre
        add(new JLabel("Titre:"), gbc);
        gbc.gridx = 1;
        JTextField titleField = new JTextField(20);
        add(titleField, gbc);

        // Champ Description
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        JTextArea descriptionField = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(descriptionField);
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Champ Priorité
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Priorité:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        add(priorityBox, gbc);

        // Champ Catégorie
        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Catégorie:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> categoryBox = new JComboBox<>(new String[]{"Network", "Hardware", "Software", "Other"});
        add(categoryBox, gbc);

        // Si en mode modification, pré-remplir les champs
        if (existingTicket != null) {
            titleField.setText(existingTicket.getTitle());
            descriptionField.setText(existingTicket.getDescription());
            priorityBox.setSelectedItem(existingTicket.getPriority().toString());

            if (existingTicket.getCategory() != null) {
                categoryBox.setSelectedItem(existingTicket.getCategory().getCategoryName());
            }
        }

        // Panel pour les boutons
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();

        // Bouton "Créer" ou "Modifier"
        JButton submitButton = new JButton(existingTicket == null ? "Créer" : "Modifier");
        submitButton.addActionListener(e -> {
            if (existingTicket == null) {
                createTicket(titleField, descriptionField, priorityBox, categoryBox);
            } else {
                updateTicket(titleField, descriptionField, priorityBox, categoryBox);
            }
        });
        buttonPanel.add(submitButton);



        add(buttonPanel, gbc);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Mode création (reste identique)
    private void createTicket(JTextField titleField, JTextArea descriptionField,
                              JComboBox<String> priorityBox, JComboBox<String> categoryBox) {
        try {
            Ticket ticket = new Ticket();
            ticket.setTitle(titleField.getText());
            ticket.setDescription(descriptionField.getText());

            String selectedPriority = priorityBox.getSelectedItem().toString();
            selectedPriority = selectedPriority.substring(0, 1).toUpperCase() + selectedPriority.substring(1).toLowerCase();
            ticket.setPriority(Priority.valueOf(selectedPriority));

            ticket.setStatus(Status.NEW);

            String categoryName = categoryBox.getSelectedItem().toString();
            Category category = CategoryService.getCategoryByName(categoryName);
            ticket.setCategory(category);

            User user = TicketService.getUserByUsername("employee");
            ticket.setUser(user);

            Ticket createdTicket = TicketService.createTicket(ticket);

            JOptionPane.showMessageDialog(this, "Ticket créé avec succès ! ID: " + createdTicket.getId());

            if (mainFrame != null) {
                mainFrame.refreshTickets();
            }
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la création du ticket : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Mode modification (update)
    private void updateTicket(JTextField titleField, JTextArea descriptionField,
                              JComboBox<String> priorityBox, JComboBox<String> categoryBox) {
        try {
            existingTicket.setTitle(titleField.getText());
            existingTicket.setDescription(descriptionField.getText());

            String selectedPriority = priorityBox.getSelectedItem().toString();
            selectedPriority = selectedPriority.substring(0, 1).toUpperCase() + selectedPriority.substring(1).toLowerCase();
            existingTicket.setPriority(Priority.valueOf(selectedPriority));

            String categoryName = categoryBox.getSelectedItem().toString();
            Category category = CategoryService.getCategoryByName(categoryName);
            existingTicket.setCategory(category);

            Ticket updatedTicket = TicketService.updateTicket(existingTicket);
            JOptionPane.showMessageDialog(this, "Ticket mis à jour avec succès ! ID: " + updatedTicket.getId());

            if (mainFrame != null) {
                mainFrame.refreshTickets();
            }
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du ticket : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Mode suppression (delete)
    private void deleteTicket() {
        try {
            int confirm = JOptionPane.showConfirmDialog(
                    this, "Voulez-vous vraiment supprimer ce ticket ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                TicketService.deleteTicket(existingTicket.getId());
                JOptionPane.showMessageDialog(this, "Ticket supprimé avec succès !");

                if (mainFrame != null) {
                    mainFrame.refreshTickets();
                }
                dispose();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la suppression du ticket : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
