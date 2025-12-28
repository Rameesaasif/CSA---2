package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import controller.FacilityController;
import data.DataStore;
import model.Facility;
import util.UiStyle;

public class FacilityPanel extends JPanel {
    private final FacilityController controller;
    private final DataStore store;
    private final Consumer<String> status;

    private final JTable table;
    private final JTextField search = new JTextField(22);
    private final FacilityTableModel model;

    public FacilityPanel(FacilityController controller, DataStore store, Consumer<String> status) {
        super(new BorderLayout(10, 10));
        this.controller = controller;
        this.store = store;
        this.status = status;

        this.model = new FacilityTableModel(controller);
        this.table = new JTable(model);
        UiStyle.applyTableStyle(table);

        TableRowSorter<FacilityTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JPanel header = new JPanel(new BorderLayout(10, 10));
        JLabel title = new JLabel("Facilities");
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

        refresh();
    }

    private void onAdd() {
        String newId = store.nextFacilityId();
        FacilityDialog dlg = new FacilityDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Add facility", null, newId);
        dlg.setVisible(true);
        Facility created = dlg.getResult();
        if (created != null) {
            controller.add(created);
            refresh();
            status.accept("Added facility " + created.getFacilityId());
        }
    }

    private void onEdit() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a facility first."); return; }

        Facility existing = controller.getAll().get(row);
        FacilityDialog dlg = new FacilityDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Edit facility", existing, existing.getFacilityId());
        dlg.setVisible(true);
        Facility updated = dlg.getResult();
        if (updated != null) {
            controller.getAll().set(row, updated);
            refresh();
            status.accept("Updated facility " + updated.getFacilityId());
        }
    }

    private void onDelete() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a facility first."); return; }

        Facility existing = controller.getAll().get(row);
        int ok = JOptionPane.showConfirmDialog(this, "Delete " + existing.getFacilityId() + "?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            controller.delete(existing);
            refresh();
            status.accept("Deleted facility " + existing.getFacilityId());
        }
    }

    public void refresh() {
        model.fireTableDataChanged();
        status.accept("Facilities: " + controller.getAll().size());
    }

    static class FacilityTableModel extends AbstractTableModel {
        private final FacilityController controller;
        private final String[] cols = {"Facility ID", "Facility Name", "Type", "Postcode", "Phone", "Email"};

        FacilityTableModel(FacilityController controller) {
            this.controller = controller;
        }

        @Override public int getRowCount() { return controller.getAll().size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }

        @Override public Object getValueAt(int row, int col) {
            Facility f = controller.getAll().get(row);
            return switch (col) {
                case 0 -> f.getFacilityId();
                case 1 -> f.getFacilityName();
                case 2 -> f.getFacilityType();
                case 3 -> f.getPostcode();
                case 4 -> f.getPhoneNumber();
                case 5 -> f.getEmail();
                default -> "";
            };
        }
    }
}