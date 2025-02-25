package com.example;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer(String text) {
        // Supprimer setText(text), car il sera défini dynamiquement dans getTableCellRendererComponent()
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(column == 4 ? "Modifier" : "Supprimer");
        return this;
    }
}