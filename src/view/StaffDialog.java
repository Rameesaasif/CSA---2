package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

import model.Staff;

public class StaffDialog extends JDialog {
    // Only fields needed for listing/relationships.
    private final JTextField firstName = new JTextField(18);
    private final JTextField lastName = new JTextField(18);
    private final JTextField role = new JTextField(18);
    private final JTextField department = new JTextField(18);
    private final JTextField facilityId = new JTextField(18);
    private final JTextField phone = new JTextField(18);
    private final JTextField email = new JTextField(18);

    private Staff result;

    public StaffDialog(java.awt.Frame owner, String dialogTitle, Staff existing, String staffId) {
        super(owner, dialogTitle, true);

        // Preserve non-listed fields.
        String employmentStatus = "";
        String startDate = "";
        String lineManager = "";
        String accessLevel = "";

        if (existing != null) {
            firstName.setText(existing.getFirstName());
            lastName.setText(existing.getLastName());
            role.setText(existing.getRole());
            department.setText(existing.getDepartment());
            facilityId.setText(existing.getFacilityId());
            phone.setText(existing.getPhoneNumber());
            email.setText(existing.getEmail());

            employmentStatus = existing.getEmploymentStatus();
            startDate = existing.getStartDate();
            lineManager = existing.getLineManager();
            accessLevel = existing.getAccessLevel();
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int r = 0;
        row(panel, gc, r++, "First name*", firstName);
        row(panel, gc, r++, "Last name*", lastName);
        row(panel, gc, r++, "Role", role);
        row(panel, gc, r++, "Department", department);
        row(panel, gc, r++, "Facility ID", facilityId);
        row(panel, gc, r++, "Phone", phone);
        row(panel, gc, r++, "Email", email);

        JButton save = new JButton("Save");
        JButton cancel = new JButton("Cancel");

        final String employmentStatusFinal = employmentStatus;
        final String startDateFinal = startDate;
        final String lineManagerFinal = lineManager;
        final String accessLevelFinal = accessLevel;

        save.addActionListener(e -> {
            if (firstName.getText().trim().isEmpty() || lastName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "First name and Last name are required.");
                return;
            }

            result = new Staff(
                staffId,
                firstName.getText().trim(),
                lastName.getText().trim(),
                role.getText().trim(),
                department.getText().trim(),
                facilityId.getText().trim(),
                phone.getText().trim(),
                email.getText().trim(),
                employmentStatusFinal,
                startDateFinal,
                lineManagerFinal,
                accessLevelFinal
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

    public Staff getResult() { return result; }
}