package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import controller.PrescriptionController;
import data.DataStore;
import model.Prescription;
import util.UiStyle;

public class PrescriptionPanel extends JPanel {
    private final PrescriptionController controller;
    private final DataStore store;
    private final Consumer<String> status;

    private final PrescriptionTableModel model;
    private final JTable table;
    private final JTextField search = new JTextField(22);

    public PrescriptionPanel(PrescriptionController controller, DataStore store, Consumer<String> status) {
        super(new BorderLayout(10, 10));
        this.controller = controller;
        this.store = store;
        this.status = status;

        this.model = new PrescriptionTableModel(controller);
        this.table = new JTable(model);
        UiStyle.applyTableStyle(table);

        TableRowSorter<PrescriptionTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JPanel header = new JPanel(new BorderLayout(10, 10));
        JLabel title = new JLabel("Prescriptions");
        UiStyle.setFontIfAvailable(title, "Label.font", java.awt.Font.BOLD, 6f);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.add(new JLabel("Search:"));
        right.add(search);
        JButton addBtn = new JButton("Add");
        right.add(addBtn);

        header.add(title, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        search.getDocument().addDocumentListener((SimpleDocumentListener) e -> {
            String q = search.getText().trim();
            if (q.isEmpty()) sorter.setRowFilter(null);
            else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(q)));
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton editBtn = new JButton("Edit");
        JButton delBtn = new JButton("Delete");
        JButton outputBtn = new JButton("Generate Output File");
        JButton refreshBtn = new JButton("Refresh");
        actions.add(editBtn);
        actions.add(delBtn);
        actions.add(outputBtn);
        actions.add(refreshBtn);

        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit());
        delBtn.addActionListener(e -> onDelete());
        outputBtn.addActionListener(e -> onGenerateOutput());
        refreshBtn.addActionListener(e -> refresh());

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) onEdit();
            }
        });

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);
    }

    private void onAdd() {
        String newId = store.nextPrescriptionId();
        PrescriptionDialog dlg = new PrescriptionDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Add prescription", null, newId);
        dlg.setVisible(true);
        Prescription created = dlg.getResult();
        if (created != null) {
            controller.add(created);
            refresh();
            status.accept("Added prescription " + created.getPrescriptionId());
        }
    }

    private void onEdit() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a prescription to edit."); return; }
        Prescription existing = controller.getAll().get(row);

        PrescriptionDialog dlg = new PrescriptionDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Edit prescription", existing, existing.getPrescriptionId());
        dlg.setVisible(true);
        Prescription updated = dlg.getResult();
        if (updated != null) {
            controller.getAll().set(row, updated);
            refresh();
            status.accept("Updated prescription " + updated.getPrescriptionId());
        }
    }

    private void onDelete() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a prescription to delete."); return; }
        Prescription p = controller.getAll().get(row);
        int ok = JOptionPane.showConfirmDialog(this, "Delete prescription " + p.getPrescriptionId() + "?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            controller.delete(p);
            refresh();
            status.accept("Deleted prescription " + p.getPrescriptionId());
        }
    }

    private void onGenerateOutput() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a prescription first."); return; }
        Prescription p = controller.getAll().get(row);
        try {
            File f = controller.generateOutput(p);
            JOptionPane.showMessageDialog(this, "Saved output file:\n" + f.getPath());
            status.accept("Generated prescription file for " + p.getPrescriptionId());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not generate output file: " + ex.getMessage());
        }
    }

    private void refresh() {
        model.fireTableDataChanged();
        status.accept("Prescriptions: " + controller.getAll().size());
    }

    static class PrescriptionTableModel extends AbstractTableModel {
        private final PrescriptionController controller;
        private final String[] cols = {"ID", "Patient", "Clinician", "Medication", "Dosage", "Status", "Issue date", "Collection date"};

        PrescriptionTableModel(PrescriptionController controller) { this.controller = controller; }

        @Override public int getRowCount() { return controller.getAll().size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }

        @Override
        public Object getValueAt(int row, int col) {
            Prescription p = controller.getAll().get(row);
            return switch (col) {
                case 0 -> p.getPrescriptionId();
                case 1 -> p.getPatientId();
                case 2 -> p.getClinicianId();
                case 3 -> p.getMedicationName();
                case 4 -> p.getDosage();
                case 5 -> p.getStatus();
                case 6 -> p.getIssueDate();
                case 7 -> p.getCollectionDate();
                default -> "";
            };
        }
    }

// Called by MainFrame after reload to repaint tables
public void refreshFromOutside() {
    model.fireTableDataChanged();
}

}
