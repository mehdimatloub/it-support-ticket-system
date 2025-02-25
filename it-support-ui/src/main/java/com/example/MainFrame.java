package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private JLabel statusLabel;

    public MainFrame() {
        setTitle("IT Support Ticket System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Modèle du tableau avec colonnes "Modifier" et "Supprimer"
        String[] columns = {"ID", "Title", "Priority", "Status", "Modifier", "Supprimer"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4 || column == 5; // Seules les colonnes "Modifier" et "Supprimer" sont interactives
            }
        };

        ticketTable = new JTable(tableModel);

        // Ajout des boutons "Modifier" et "Supprimer"
        ticketTable.getColumn("Modifier").setCellRenderer(new ButtonRenderer("Modifier"));
        ticketTable.getColumn("Modifier").setCellEditor(new ButtonEditor(this));

        ticketTable.getColumn("Supprimer").setCellRenderer(new ButtonRenderer("Supprimer"));
        ticketTable.getColumn("Supprimer").setCellEditor(new DeleteButtonEditor(this));

        // Boutons d'action
        JButton refreshButton = new JButton("Refresh Tickets");
        refreshButton.addActionListener(e -> loadTickets());

        JButton createTicketButton = new JButton("Créer un Ticket");
        createTicketButton.addActionListener(e -> new TicketForm(this, null)); // Mode création

        // Status label
        statusLabel = new JLabel("Click Refresh to load tickets");

        // Barre de menu
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Ajout des composants
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        buttonPanel.add(createTicketButton);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(ticketTable), BorderLayout.CENTER);
        panel.add(statusLabel, BorderLayout.SOUTH);

        add(panel);
        loadTickets(); // Charger les tickets au démarrage
    }

    public void loadTickets() {
        try {
            statusLabel.setText("Loading tickets...");
            List<Ticket> tickets = TicketService.getTickets();
            tableModel.setRowCount(0); // Réinitialiser le tableau

            for (Ticket ticket : tickets) {
                tableModel.addRow(new Object[]{
                        ticket.getId(),
                        ticket.getTitle(),
                        ticket.getPriority(),
                        ticket.getStatus(),
                        ticket, // Passe le ticket pour "Modifier"
                        ticket  // Passe le ticket pour "Supprimer"
                });
            }

            statusLabel.setText("Tickets loaded: " + tickets.size());
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Failed to load tickets: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshTickets() {
        loadTickets();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
