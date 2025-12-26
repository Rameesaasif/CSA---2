package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import model.Clinician;

public class ClinicianDialog extends JDialog {
    // Only fields needed for listings/related screens.
    private final JTextField firstName = new JTextField(18);
    private final JTextField lastName = new JTextField(18);
    private final JTextField title = new JTextField(18);
    private final JTextField speciality = new JTextField(18);
    private final JTextField phone = new JTextField(18);
    private final JTextField email = new JTextField(18);
    private final JTextField workplaceId = new JTextField(18);
    private final JTextField workplaceType = new JTextField(18);

    private Clinician result;

    public ClinicianDialog(java.awt.Frame owner, String dialogTitle, Clinician existing, String clinicianId) {
        super(owner, dialogTitle, true);

        if (existing != null) {
            firstName.setText(existing.getFirstName());
            lastName.setText(existing.getLastName());
            title.setText(existing.getTitle());
            speciality.setText(existing.getSpeciality());
            phone.setText(existing.getPhoneNumber());
            email.setText(existing.getEmail());
            workplaceId.setText(existing.getWorkplaceId());
            workplaceType.setText(existing.getWorkplaceType());
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int r = 0;
        addRow(panel, gc, r++, "First name*", firstName);
        addRow(panel, gc, r++, "Last name*", lastName);
        addRow(panel, gc, r++, "Title", title);
        addRow(panel, gc, r++, "Speciality", speciality);
        addRow(panel, gc, r++, "Phone", phone);
        addRow(panel, gc, r++, "Email", email);
        addRow(panel, gc, r++, "Workplace type", workplaceType);
        addRow(panel, gc, r++, "Workplace ID", workplaceId);

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        save.addActionListener(e -> {
            if (firstName.getText().trim().isEmpty() || lastName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "First name and last name are required.");
                return;
            }

            // Keep non-collected CSV fields from existing if editing, else blank defaults.
            String gmc = existing != null ? existing.getGmcNumber() : "";
            String employmentStatus = existing != null ? existing.getEmploymentStatus() : "";
            String startDate = existing != null ? existing.getStartDate() : java.time.LocalDate.now().toString();

            result = new Clinician(
                clinicianId,
                firstName.getText().trim(),
                lastName.getText().trim(),
                title.getText().trim(),
                speciality.getText().trim(),
                gmc,
                phone.getText().trim(),
                email.getText().trim(),
                workplaceId.getText().trim(),
                workplaceType.getText().trim(),
                employmentStatus,
                startDate
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

    public Clinician getResult() { return result; }
}
