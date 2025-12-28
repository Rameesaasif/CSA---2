package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import model.Facility;

public class FacilityDialog extends JDialog {
    // Only fields needed for listing/relationships.
    private final JTextField facilityName = new JTextField(18);
    private final JTextField facilityType = new JTextField(18);
    private final JTextField postcode = new JTextField(18);
    private final JTextField phone = new JTextField(18);
    private final JTextField email = new JTextField(18);

    private Facility result;

    public FacilityDialog(java.awt.Frame owner, String dialogTitle, Facility existing, String facilityId) {
        super(owner, dialogTitle, true);

        // Preserve non-listed fields so we don't lose data when editing.
        String address = "";
        String openingHours = "";
        String managerName = "";
        String capacity = "";
        String specialities = "";

        if (existing != null) {
            facilityName.setText(existing.getFacilityName());
            facilityType.setText(existing.getFacilityType());
            postcode.setText(existing.getPostcode());
            phone.setText(existing.getPhoneNumber());
            email.setText(existing.getEmail());

            address = existing.getAddress();
            openingHours = existing.getOpeningHours();
            managerName = existing.getManagerName();
            capacity = existing.getCapacity();
            specialities = existing.getSpecialitiesOffered();
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int r = 0;
        row(panel, gc, r++, "Facility Name*", facilityName);
        row(panel, gc, r++, "Facility Type", facilityType);
        row(panel, gc, r++, "Postcode", postcode);
        row(panel, gc, r++, "Phone", phone);
        row(panel, gc, r++, "Email", email);

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        final String addressFinal = address;
        final String openingHoursFinal = openingHours;
        final String managerNameFinal = managerName;
        final String capacityFinal = capacity;
        final String specialitiesFinal = specialities;

        save.addActionListener(e -> {
            if (facilityName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Facility Name is required.");
                return;
            }

            result = new Facility(
                facilityId,
                facilityName.getText().trim(),
                facilityType.getText().trim(),
                addressFinal,
                postcode.getText().trim(),
                phone.getText().trim(),
                email.getText().trim(),
                openingHoursFinal,
                managerNameFinal,
                capacityFinal,
                specialitiesFinal
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

    private void row(JPanel panel, GridBagConstraints gc, int r, String label, JTextField field) {
        gc.gridx = 0; gc.gridy = r; gc.weightx = 0;
        panel.add(new JLabel(label), gc);
        gc.gridx = 1; gc.weightx = 1;
        panel.add(field, gc);
    }

    public Facility getResult() { return result; }
}