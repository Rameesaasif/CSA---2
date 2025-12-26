package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Referral;

public class ReferralDialog extends JDialog {
    private final JTextField patientId = new JTextField(18);
    private final JTextField fromClinicianId = new JTextField(18);
    private final JTextField toClinicianId = new JTextField(18);
    private final JTextField fromFacilityId = new JTextField(18);
    private final JTextField toFacilityId = new JTextField(18);
    private final JTextField referralDate = new JTextField(18);
    private final JTextField urgency = new JTextField(18);
    private final JTextField reason = new JTextField(18);
    private final JTextField summary = new JTextField(18);
    private final JTextField investigations = new JTextField(18);
    private final JTextField status = new JTextField(18);
    private final JTextField appointmentId = new JTextField(18);
    private final JTextField notes = new JTextField(18);

    private Referral result;

    public ReferralDialog(java.awt.Frame owner, String title, Referral existing, String newId) {
        super(owner, title, true);

        if (existing != null) {
            patientId.setText(existing.getPatientId());
            fromClinicianId.setText(existing.getReferringClinicianId());
            toClinicianId.setText(existing.getReferredToClinicianId());
            fromFacilityId.setText(existing.getReferringFacilityId());
            toFacilityId.setText(existing.getReferredToFacilityId());
            referralDate.setText(existing.getReferralDate());
            urgency.setText(existing.getUrgencyLevel());
            reason.setText(existing.getReferralReason());
            summary.setText(existing.getClinicalSummary());
            investigations.setText(existing.getRequestedInvestigations());
            status.setText(existing.getStatus());
            appointmentId.setText(existing.getAppointmentId());
            notes.setText(existing.getNotes());
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int r = 0;
        addRow(panel, gc, r++, "Patient ID", patientId);
        addRow(panel, gc, r++, "Referring clinician ID", fromClinicianId);
        addRow(panel, gc, r++, "Referred-to clinician ID", toClinicianId);
        addRow(panel, gc, r++, "Referring facility ID", fromFacilityId);
        addRow(panel, gc, r++, "Referred-to facility ID", toFacilityId);
        addRow(panel, gc, r++, "Referral date", referralDate);
        addRow(panel, gc, r++, "Urgency level", urgency);
        addRow(panel, gc, r++, "Referral reason", reason);
        addRow(panel, gc, r++, "Clinical summary", summary);
        addRow(panel, gc, r++, "Requested investigations", investigations);
        addRow(panel, gc, r++, "Status", status);
        addRow(panel, gc, r++, "Appointment ID", appointmentId);
        addRow(panel, gc, r++, "Notes", notes);

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");
        save.addActionListener(e -> {
            if (patientId.getText().trim().isEmpty() || reason.getText().trim().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "Patient ID and Referral reason are required.");
                return;
            }
            // created/updated timestamps can be simple for coursework
            String now = java.time.LocalDate.now().toString();
            result = new Referral(
                newId,
                patientId.getText().trim(),
                fromClinicianId.getText().trim(),
                toClinicianId.getText().trim(),
                fromFacilityId.getText().trim(),
                toFacilityId.getText().trim(),
                referralDate.getText().trim(),
                urgency.getText().trim(),
                reason.getText().trim(),
                summary.getText().trim(),
                investigations.getText().trim(),
                status.getText().trim(),
                appointmentId.getText().trim(),
                notes.getText().trim(),
                now,
                now
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

    public Referral getResult() { return result; }
}
