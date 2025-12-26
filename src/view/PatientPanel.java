package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import controller.PatientController;
import data.DataStore;
import model.Patient;
import util.UiStyle;

public class PatientPanel extends JPanel {
    private final PatientController controller;
    private final DataStore store;
    private final Consumer<String> status;

    private final PatientTableModel model;
    private final JTable table;
    private final JTextField search = new JTextField(22);

    public PatientPanel(PatientController controller, DataStore store, Consumer<String> status) {
        super(new BorderLayout(10, 10));
        this.controller = controller;
        this.store = store;
        this.status = status;

        this.model = new PatientTableModel(controller);
        this.table = new JTable(model);
        UiStyle.applyTableStyle(table);

        TableRowSorter<PatientTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JPanel header = new JPanel(new BorderLayout(10, 10));
        JLabel title = new JLabel("Patients");
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
        JButton refreshBtn = new JButton("Refresh");
        actions.add(editBtn);
        actions.add(delBtn);
        actions.add(refreshBtn);

        addBtn.addActionListener(e -> onAdd());
        editBtn.addActionListener(e -> onEdit());
        delBtn.addActionListener(e -> onDelete());
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
        String newId = store.nextPatientId();
        PatientDialog dlg = new PatientDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Add patient", null, newId);
        dlg.setVisible(true);
        Patient created = dlg.getResult();
        if (created != null) {
            controller.add(created);
            refresh();
            status.accept("Added patient " + created.getPatientId());
        }
    }

    private void onEdit() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a patient to edit."); return; }
        Patient existing = controller.getAll().get(row);

        PatientDialog dlg = new PatientDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Edit patient", existing, existing.getPatientId());
        dlg.setVisible(true);
        Patient updated = dlg.getResult();
        if (updated != null) {
            controller.getAll().set(row, updated);
            refresh();
            status.accept("Updated patient " + updated.getPatientId());
        }
    }

    private void onDelete() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a patient to delete."); return; }
        Patient p = controller.getAll().get(row);
        int ok = JOptionPane.showConfirmDialog(this, "Delete patient " + p.getPatientId() + "?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            controller.delete(p);
            refresh();
            status.accept("Deleted patient " + p.getPatientId());
        }
    }

    private void refresh() {
        model.fireTableDataChanged();
        status.accept("Patients: " + controller.getAll().size());
    }

    static class PatientTableModel extends AbstractTableModel {
        private final PatientController controller;
        private final String[] cols = {"ID", "First name", "Last name", "DOB", "NHS", "Gender", "Phone", "Email", "Postcode"};

        PatientTableModel(PatientController controller) { this.controller = controller; }

        @Override public int getRowCount() { return controller.getAll().size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }

        @Override
        public Object getValueAt(int row, int col) {
            Patient p = controller.getAll().get(row);
            return switch (col) {
                case 0 -> p.getPatientId();
                case 1 -> p.getFirstName();
                case 2 -> p.getLastName();
                case 3 -> p.getDateOfBirth();
                case 4 -> p.getNhsNumber();
                case 5 -> p.getGender();
                case 6 -> p.getPhoneNumber();
                case 7 -> p.getEmail();
                case 8 -> p.getPostcode();
                default -> "";
            };
        }
    }

// Called by MainFrame after reload to repaint tables
public void refreshFromOutside() {
    model.fireTableDataChanged();
}

}
