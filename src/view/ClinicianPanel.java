package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import controller.ClinicianController;
import model.Clinician;
import util.UiStyle;

public class ClinicianPanel extends JPanel {
    private final ClinicianController controller;
    private final Consumer<String> status;

    private final ClinicianTableModel model;
    private final JTable table;
    private final JTextField search = new JTextField(22);

    public ClinicianPanel(ClinicianController controller, Consumer<String> status) {
        super(new BorderLayout(10, 10));
        this.controller = controller;
        this.status = status;

        this.model = new ClinicianTableModel(controller);
        this.table = new JTable(model);
        UiStyle.applyTableStyle(table);

        TableRowSorter<ClinicianTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JPanel header = new JPanel(new BorderLayout(10, 10));
        JLabel title = new JLabel("Clinicians");
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
        // Keep it simple: ask for an ID (or you can build a generator if your dataset uses a pattern).
        String id = JOptionPane.showInputDialog(this, "Enter clinician ID (e.g., CLN-0001):");
        if (id == null || id.trim().isEmpty()) return;

        ClinicianDialog dlg = new ClinicianDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Add clinician", null, id.trim());
        dlg.setVisible(true);
        Clinician created = dlg.getResult();
        if (created != null) {
            controller.add(created);
            refresh();
            status.accept("Added clinician " + created.getClinicianId());
        }
    }

    private void onEdit() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a clinician to edit."); return; }
        Clinician existing = controller.getAll().get(row);

        ClinicianDialog dlg = new ClinicianDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Edit clinician", existing, existing.getClinicianId());
        dlg.setVisible(true);
        Clinician updated = dlg.getResult();
        if (updated != null) {
            controller.getAll().set(row, updated);
            refresh();
            status.accept("Updated clinician " + updated.getClinicianId());
        }
    }

    private void onDelete() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a clinician to delete."); return; }
        Clinician c = controller.getAll().get(row);
        int ok = JOptionPane.showConfirmDialog(this, "Delete clinician " + c.getClinicianId() + "?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            controller.delete(c);
            refresh();
            status.accept("Deleted clinician " + c.getClinicianId());
        }
    }

    private void refresh() {
        model.fireTableDataChanged();
        status.accept("Clinicians: " + controller.getAll().size());
    }

    static class ClinicianTableModel extends AbstractTableModel {
        private final ClinicianController controller;
        private final String[] cols = {"ID", "Name", "Title", "Speciality", "Phone", "Email", "Workplace"};

        ClinicianTableModel(ClinicianController controller) { this.controller = controller; }

        @Override public int getRowCount() { return controller.getAll().size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }

        @Override
        public Object getValueAt(int row, int col) {
            Clinician c = controller.getAll().get(row);
            return switch (col) {
                case 0 -> c.getClinicianId();
                case 1 -> c.getFirstName() + " " + c.getLastName();
                case 2 -> c.getTitle();
                case 3 -> c.getSpeciality();
                case 4 -> c.getPhoneNumber();
                case 5 -> c.getEmail();
                case 6 -> c.getWorkplaceType() + " " + c.getWorkplaceId();
                default -> "";
            };
        }
    }

// Called by MainFrame after reload to repaint tables
public void refreshFromOutside() {
    model.fireTableDataChanged();
}

}
