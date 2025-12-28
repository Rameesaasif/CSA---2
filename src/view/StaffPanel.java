package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import controller.StaffController;
import data.DataStore;
import model.Staff;
import util.UiStyle;

public class StaffPanel extends JPanel {
    private final StaffController controller;
    private final DataStore store;
    private final Consumer<String> status;

    private final JTable table;
    private final JTextField search = new JTextField(22);
    private final StaffTableModel model;

    public StaffPanel(StaffController controller, DataStore store, Consumer<String> status) {
        super(new BorderLayout(10, 10));
        this.controller = controller;
        this.store = store;
        this.status = status;

        this.model = new StaffTableModel(controller);
        this.table = new JTable(model);
        UiStyle.applyTableStyle(table);

        TableRowSorter<StaffTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JPanel header = new JPanel(new BorderLayout(10, 10));
        JLabel title = new JLabel("Staff");
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
        String newId = store.nextStaffId();
        StaffDialog dlg = new StaffDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Add staff", null, newId);
        dlg.setVisible(true);
        Staff created = dlg.getResult();
        if (created != null) {
            controller.add(created);
            refresh();
            status.accept("Added staff " + created.getStaffId());
        }
    }

    private void onEdit() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a staff record first."); return; }

        Staff existing = controller.getAll().get(row);
        StaffDialog dlg = new StaffDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Edit staff", existing, existing.getStaffId());
        dlg.setVisible(true);
        Staff updated = dlg.getResult();
        if (updated != null) {
            controller.getAll().set(row, updated);
            refresh();
            status.accept("Updated staff " + updated.getStaffId());
        }
    }

    private void onDelete() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a staff record first."); return; }

        Staff existing = controller.getAll().get(row);
        int ok = JOptionPane.showConfirmDialog(this, "Delete " + existing.getStaffId() + "?",
                "Confirm", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            controller.delete(existing);
            refresh();
            status.accept("Deleted staff " + existing.getStaffId());
        }
    }

    public void refresh() {
        model.fireTableDataChanged();
        status.accept("Staff: " + controller.getAll().size());
    }

    static class StaffTableModel extends AbstractTableModel {
        private final StaffController controller;
        private final String[] cols = {"Staff ID", "First name", "Last name", "Role", "Department", "Facility ID", "Phone", "Email"};

        StaffTableModel(StaffController controller) {
            this.controller = controller;
        }

        @Override public int getRowCount() { return controller.getAll().size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }

        @Override public Object getValueAt(int row, int col) {
            Staff s = controller.getAll().get(row);
            return switch (col) {
                case 0 -> s.getStaffId();
                case 1 -> s.getFirstName();
                case 2 -> s.getLastName();
                case 3 -> s.getRole();
                case 4 -> s.getDepartment();
                case 5 -> s.getFacilityId();
                case 6 -> s.getPhoneNumber();
                case 7 -> s.getEmail();
                default -> "";
            };
        }
    }
}