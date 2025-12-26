package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Prescription;

public class PrescriptionDialog extends JDialog {
    private final JTextField patientId = new JTextField(18);
    private final JTextField clinicianId = new JTextField(18);
    private final JTextField appointmentId = new JTextField(18);
    private final JTextField prescriptionDate = new JTextField(18);
    private final JTextField medicationName = new JTextField(18);
    private final JTextField dosage = new JTextField(18);
    private final JTextField frequency = new JTextField(18);
    private final JTextField durationDays = new JTextField(18);
    private final JTextField quantity = new JTextField(18);
    private final JTextField instructions = new JTextField(18);
    private final JTextField pharmacyName = new JTextField(18);
    private final JTextField status = new JTextField(18);
    private final JTextField issueDate = new JTextField(18);
    private final JTextField collectionDate = new JTextField(18);

    private Prescription result;

    public PrescriptionDialog(java.awt.Frame owner, String title, Prescription existing, String newId) {
        super(owner, title, true);

        if (existing != null) {
            patientId.setText(existing.getPatientId());
            clinicianId.setText(existing.getClinicianId());
            appointmentId.setText(existing.getAppointmentId());
            prescriptionDate.setText(existing.getPrescriptionDate());
            medicationName.setText(existing.getMedicationName());
            dosage.setText(existing.getDosage());
            frequency.setText(existing.getFrequency());
            durationDays.setText(existing.getDurationDays());
            quantity.setText(existing.getQuantity());
            instructions.setText(existing.getInstructions());
            pharmacyName.setText(existing.getPharmacyName());
            status.setText(existing.getStatus());
            issueDate.setText(existing.getIssueDate());
            collectionDate.setText(existing.getCollectionDate());
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int r = 0;
        addRow(panel, gc, r++, "Patient ID", patientId);
        addRow(panel, gc, r++, "Clinician ID", clinicianId);
        addRow(panel, gc, r++, "Appointment ID", appointmentId);
        addRow(panel, gc, r++, "Prescription date", prescriptionDate);
        addRow(panel, gc, r++, "Medication name", medicationName);
        addRow(panel, gc, r++, "Dosage", dosage);
        addRow(panel, gc, r++, "Frequency", frequency);
        addRow(panel, gc, r++, "Duration days", durationDays);
        addRow(panel, gc, r++, "Quantity", quantity);
        addRow(panel, gc, r++, "Instructions", instructions);
        addRow(panel, gc, r++, "Pharmacy name", pharmacyName);
        addRow(panel, gc, r++, "Status", status);
        addRow(panel, gc, r++, "Issue date", issueDate);
        addRow(panel, gc, r++, "Collection date", collectionDate);

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        save.addActionListener(e -> {
            if (patientId.getText().trim().isEmpty() || medicationName.getText().trim().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Patient ID and Medication name are required.");
                return;
            }
            result = new Prescription(
                newId,
                patientId.getText().trim(),
                clinicianId.getText().trim(),
                appointmentId.getText().trim(),
                prescriptionDate.getText().trim(),
                medicationName.getText().trim(),
                dosage.getText().trim(),
                frequency.getText().trim(),
                durationDays.getText().trim(),
                quantity.getText().trim(),
                instructions.getText().trim(),
                pharmacyName.getText().trim(),
                status.getText().trim(),
                issueDate.getText().trim(),
                collectionDate.getText().trim()
            );
            dispose();
        });
        cancel.addActionListener(e -> dispose());

        gc.gridx = 0; gc.gridy = r; gc.gridwidth = 1;
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

    public Prescription getResult() { return result; }
}
