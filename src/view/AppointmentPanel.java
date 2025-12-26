package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import controller.AppointmentController;
import data.DataStore;
import model.Appointment;
import util.UiStyle;

public class AppointmentPanel extends JPanel {
    private final AppointmentController controller;
    private final DataStore store;
    private final Consumer<String> status;

    private final AppointmentTableModel model;
    private final JTable table;
    private final JTextField search = new JTextField(22);

    public AppointmentPanel(AppointmentController controller, DataStore store, Consumer<String> status) {
        super(new BorderLayout(10, 10));
        this.controller = controller;
        this.store = store;
        this.status = status;

        this.model = new AppointmentTableModel(controller);
        this.table = new JTable(model);
        UiStyle.applyTableStyle(table);

        TableRowSorter<AppointmentTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JPanel header = new JPanel(new BorderLayout(10, 10));
        JLabel title = new JLabel("Appointments");
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
        String newId = store.nextAppointmentId();
        AppointmentDialog dlg = new AppointmentDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Add appointment", null, newId);
        dlg.setVisible(true);
        Appointment created = dlg.getResult();
        if (created != null) {
            controller.add(created);
            refresh();
            status.accept("Added appointment " + created.getAppointmentId());
        }
    }

    private void onEdit() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select an appointment to edit."); return; }
        Appointment existing = controller.getAll().get(row);

        AppointmentDialog dlg = new AppointmentDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Edit appointment", existing, existing.getAppointmentId());
        dlg.setVisible(true);
        Appointment updated = dlg.getResult();
        if (updated != null) {
            controller.getAll().set(row, updated);
            refresh();
            status.accept("Updated appointment " + updated.getAppointmentId());
        }
    }

    private void onDelete() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select an appointment to delete."); return; }
        Appointment a = controller.getAll().get(row);
        int ok = JOptionPane.showConfirmDialog(this, "Delete appointment " + a.getAppointmentId() + "?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            controller.delete(a);
            refresh();
            status.accept("Deleted appointment " + a.getAppointmentId());
        }
    }

    private void refresh() {
        model.fireTableDataChanged();
        status.accept("Appointments: " + controller.getAll().size());
    }

    static class AppointmentTableModel extends AbstractTableModel {
        private final AppointmentController controller;
        private final String[] cols = {"ID", "Patient", "Clinician", "Facility", "Date", "Time", "Duration", "Status", "Reason"};

        AppointmentTableModel(AppointmentController controller) { this.controller = controller; }

        @Override public int getRowCount() { return controller.getAll().size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }

        @Override
        public Object getValueAt(int row, int col) {
            Appointment a = controller.getAll().get(row);
            return switch (col) {
                case 0 -> a.getAppointmentId();
                case 1 -> a.getPatientId();
                case 2 -> a.getClinicianId();
                case 3 -> a.getFacilityId();
                case 4 -> a.getAppointmentDate();
                case 5 -> a.getAppointmentTime();
                case 6 -> a.getDurationMinutes();
                case 7 -> a.getStatus();
                case 8 -> a.getReasonForVisit();
                default -> "";
            };
        }
    }

// Called by MainFrame after reload to repaint tables
public void refreshFromOutside() {
    model.fireTableDataChanged();
}

}
