package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import model.Patient;

public class PatientDialog extends JDialog {
    // Only fields that are necessary / shown in the listing (plus a couple related IDs).
    private final JTextField firstName = new JTextField(18);
    private final JTextField lastName = new JTextField(18);
    private final JTextField dob = new JTextField(18);
    private final JTextField nhs = new JTextField(18);
    private final JTextField gender = new JTextField(18);
    private final JTextField phone = new JTextField(18);
    private final JTextField email = new JTextField(18);
    private final JTextField postcode = new JTextField(18);

    private Patient result;

    public PatientDialog(java.awt.Frame owner, String title, Patient existing, String patientId) {
        super(owner, title, true);

        if (existing != null) {
            firstName.setText(existing.getFirstName());
            lastName.setText(existing.getLastName());
            dob.setText(existing.getDateOfBirth());
            nhs.setText(existing.getNhsNumber());
            gender.setText(existing.getGender());
            phone.setText(existing.getPhoneNumber());
            email.setText(existing.getEmail());
            postcode.setText(existing.getPostcode());
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int r = 0;
        addRow(panel, gc, r++, "First name*", firstName);
        addRow(panel, gc, r++, "Last name*", lastName);
        addRow(panel, gc, r++, "DOB", dob);
        addRow(panel, gc, r++, "NHS number", nhs);
        addRow(panel, gc, r++, "Gender", gender);
        addRow(panel, gc, r++, "Phone", phone);
        addRow(panel, gc, r++, "Email", email);
        addRow(panel, gc, r++, "Postcode", postcode);

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        save.addActionListener(e -> {
            if (firstName.getText().trim().isEmpty() || lastName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "First name and last name are required.");
                return;
            }

            // For the CSV columns we are not collecting here, keep existing values if editing,
            // otherwise set them to empty strings.
            String address = existing != null ? existing.getAddress() : "";
            String emergencyName = existing != null ? existing.getEmergencyContactName() : "";
            String emergencyPhone = existing != null ? existing.getEmergencyContactPhone() : "";
            String regDate = existing != null ? existing.getRegistrationDate() : java.time.LocalDate.now().toString();
            String gpSurgery = existing != null ? existing.getGpSurgeryId() : "";

            result = new Patient(
                patientId,
                firstName.getText().trim(),
                lastName.getText().trim(),
                dob.getText().trim(),
                nhs.getText().trim(),
                gender.getText().trim(),
                phone.getText().trim(),
                email.getText().trim(),
                address,
                postcode.getText().trim(),
                emergencyName,
                emergencyPhone,
                regDate,
                gpSurgery
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

    public Patient getResult() { return result; }
}
