package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import controller.ReferralController;
import data.DataStore;
import model.Referral;
import util.UiStyle;

public class ReferralPanel extends JPanel {
    private final ReferralController controller;
    private final DataStore store;
    private final Consumer<String> status;

    private final ReferralTableModel model;
    private final JTable table;
    private final JTextField search = new JTextField(22);

    public ReferralPanel(ReferralController controller, DataStore store, Consumer<String> status) {
        super(new BorderLayout(10, 10));
        this.controller = controller;
        this.store = store;
        this.status = status;

        this.model = new ReferralTableModel(controller);
        this.table = new JTable(model);
        UiStyle.applyTableStyle(table);

        TableRowSorter<ReferralTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JPanel header = new JPanel(new BorderLayout(10, 10));
        JLabel title = new JLabel("Referrals");
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
        JButton delBtn = new JButton("Delete");
        JButton outputBtn = new JButton("Generate Referral Letter");
        JButton refreshBtn = new JButton("Refresh");
        actions.add(delBtn);
        actions.add(outputBtn);
        actions.add(refreshBtn);

        addBtn.addActionListener(e -> onAdd());
        delBtn.addActionListener(e -> onDelete());
        outputBtn.addActionListener(e -> onGenerateOutput());
        refreshBtn.addActionListener(e -> refresh());

        add(header, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        refresh();
    }

    private void onAdd() {
        String newId = store.nextReferralId();
        ReferralDialog dlg = new ReferralDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this),
                "Add referral", null, newId);
        dlg.setVisible(true);
        Referral created = dlg.getResult();
        if (created != null) {
            controller.add(created);
            refresh();
            status.accept("Added referral " + created.getReferralId() + " (queue size: " + controller.queueSize() + ")");
        }
    }

    private void onDelete() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a referral to delete."); return; }
        Referral r = controller.getAll().get(row);
        int ok = JOptionPane.showConfirmDialog(this, "Delete referral " + r.getReferralId() + "?", "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            controller.delete(r);
            refresh();
            status.accept("Deleted referral " + r.getReferralId());
        }
    }

    private void onGenerateOutput() {
        int row = ViewUtil.selectedModelRow(table);
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select a referral first."); return; }
        Referral r = controller.getAll().get(row);
        try {
            File f = controller.generateOutput(r);
            JOptionPane.showMessageDialog(this, "Saved referral letter:\n" + f.getPath());
            status.accept("Generated referral file for " + r.getReferralId() + " (queue size: " + controller.queueSize() + ")");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not generate referral letter: " + ex.getMessage());
        }
    }

    private void refresh() {
        model.fireTableDataChanged();
        status.accept("Referrals: " + controller.getAll().size() + " | Queue: " + controller.queueSize());
    }

    static class ReferralTableModel extends AbstractTableModel {
        private final ReferralController controller;
        private final String[] cols = {"ID", "Patient", "From Clinician", "To Clinician", "Urgency", "Status", "Date"};

        ReferralTableModel(ReferralController controller) { this.controller = controller; }

        @Override public int getRowCount() { return controller.getAll().size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int c) { return cols[c]; }

        @Override
        public Object getValueAt(int row, int col) {
            Referral r = controller.getAll().get(row);
            return switch (col) {
                case 0 -> r.getReferralId();
                case 1 -> r.getPatientId();
                case 2 -> r.getReferringClinicianId();
                case 3 -> r.getReferredToClinicianId();
                case 4 -> r.getUrgencyLevel();
                case 5 -> r.getStatus();
                case 6 -> r.getReferralDate();
                default -> "";
            };
        }
    }

// Called by MainFrame after reload to repaint tables
public void refreshFromOutside() {
    model.fireTableDataChanged();
}

}
