package com.example;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButton button;
    private Ticket currentTicket;
    private MainFrame mainFrame;

    public DeleteButtonEditor(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        button = new JButton("Supprimer");
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof Ticket) {
            currentTicket = (Ticket) value;
        }
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return currentTicket; // Retourne le ticket au lieu du texte du bouton
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentTicket != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    null, "Voulez-vous vraiment supprimer ce ticket ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    TicketService.deleteTicket(currentTicket.getId());
                    JOptionPane.showMessageDialog(null, "Ticket supprimé avec succès !");
                    mainFrame.refreshTickets();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erreur lors de la suppression : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        fireEditingCanceled(); // Annule l'édition pour éviter un rafraîchissement incorrect du tableau
    }
}
