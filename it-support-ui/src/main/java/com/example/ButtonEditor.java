package com.example;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private JButton button;
    private Ticket currentTicket;
    private MainFrame mainFrame;

    public ButtonEditor(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        button = new JButton("Modifier");
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
        return currentTicket; // On retourne l'objet Ticket au lieu du texte du bouton
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (currentTicket != null) {
            new TicketForm(mainFrame, currentTicket);
        }
        fireEditingCanceled(); // On annule l'édition pour éviter une mise à jour incorrecte du modèle
    }
}
