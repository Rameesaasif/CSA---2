package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import model.Appointment;

public class AppointmentDialog extends JDialog {
    private final JTextField patientId = new JTextField(18);
    private final JTextField clinicianId = new JTextField(18);
    private final JTextField facilityId = new JTextField(18);
    private final JTextField date = new JTextField(18);
    private final JTextField time = new JTextField(18);
    private final JTextField duration = new JTextField(18);
    private final JTextField type = new JTextField(18);
    private final JTextField status = new JTextField(18);
    private final JTextField reason = new JTextField(18);
    private final JTextField notes = new JTextField(18);

    private Appointment result;

    public AppointmentDialog(java.awt.Frame owner, String dialogTitle, Appointment existing, String appointmentId) {
        super(owner, dialogTitle, true);

        if (existing != null) {
            patientId.setText(existing.getPatientId());
            clinicianId.setText(existing.getClinicianId());
            facilityId.setText(existing.getFacilityId());
            date.setText(existing.getAppointmentDate());
            time.setText(existing.getAppointmentTime());
            duration.setText(existing.getDurationMinutes());
            type.setText(existing.getAppointmentType());
            status.setText(existing.getStatus());
            reason.setText(existing.getReasonForVisit());
            notes.setText(existing.getNotes());
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int r = 0;
        addRow(panel, gc, r++, "Patient ID", patientId);
        addRow(panel, gc, r++, "Clinician ID", clinicianId);
        addRow(panel, gc, r++, "Facility ID", facilityId);
        addRow(panel, gc, r++, "Date (YYYY-MM-DD)", date);
        addRow(panel, gc, r++, "Time (HH:MM)", time);
        addRow(panel, gc, r++, "Duration (minutes)", duration);
        addRow(panel, gc, r++, "Type", type);
        addRow(panel, gc, r++, "Status", status);
        addRow(panel, gc, r++, "Reason for visit", reason);
        addRow(panel, gc, r++, "Notes", notes);

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        save.addActionListener(e -> {
            if (patientId.getText().trim().isEmpty() || clinicianId.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Patient ID and Clinician ID are required.");
                return;
            }
            String today = java.time.LocalDate.now().toString();
            result = new Appointment(
                appointmentId,
                patientId.getText().trim(),
                clinicianId.getText().trim(),
                facilityId.getText().trim(),
                date.getText().trim(),
                time.getText().trim(),
                duration.getText().trim(),
                type.getText().trim(),
                status.getText().trim(),
                reason.getText().trim(),
                notes.getText().trim(),
                today,
                today
            );
            dispose();
        });

        cancel.addActionListener(e -> dispose());

        gc.gridx = 0; gc.gridy = r;
        panel.add(save, gc);
        gc.gridx = 1;
        panel.add(cancel, gc);

        setContentPane(panel);
        pack();
        setLocationRelativeTo(owner);
    }

    private void addRow(JPanel panel, GridBagConstraints gc, int row, String label, JTextField field) {
        gc.gridx = 0; gc.gridy = row; gc.weightx = 0;
        panel.add(new JLabel(label), gc);
        gc.gridx = 1; gc.weightx = 1;
        panel.add(field, gc);
    }

    public Appointment getResult() { return result; }
}
