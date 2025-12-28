package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import controller.*;
import data.CsvLoader;
import data.CsvWriter;
import data.DataStore;
import util.UiStyle;

public class MainFrame extends JFrame {
    private final JLabel status = new JLabel("Ready");

    private final DataStore store;

    // keep references so reload can refresh UI
    private PatientPanel patientPanel;
    private ClinicianPanel clinicianPanel;
    private AppointmentPanel appointmentPanel;
    private FacilityPanel facilityPanel;
    private StaffPanel staffPanel;
    private PrescriptionPanel prescriptionPanel;
    private ReferralPanel referralPanel;

    public MainFrame(DataStore store) {
        super("Healthcare Management System - CSA 2");
        this.store = store;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildToolbar(), BorderLayout.NORTH);

        PatientController patientController = new PatientController(store);
        ClinicianController clinicianController = new ClinicianController(store);
        AppointmentController appointmentController = new AppointmentController(store);
        FacilityController facilityController = new FacilityController(store);
        StaffController staffController = new StaffController(store);
        PrescriptionController prescriptionController = new PrescriptionController(store);
        ReferralController referralController = new ReferralController(store);

        patientPanel = new PatientPanel(patientController, store, this::setStatus);
        clinicianPanel = new ClinicianPanel(clinicianController, this::setStatus);
        appointmentPanel = new AppointmentPanel(appointmentController, store, this::setStatus);
        facilityPanel = new FacilityPanel(facilityController, store, this::setStatus);
        staffPanel = new StaffPanel(staffController, store, this::setStatus);
        prescriptionPanel = new PrescriptionPanel(prescriptionController, store, this::setStatus);
        referralPanel = new ReferralPanel(referralController, store, this::setStatus);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Patients", patientPanel);
        tabs.addTab("Clinicians", clinicianPanel);
        tabs.addTab("Appointments", appointmentPanel);
        tabs.addTab("Facilities", facilityPanel);
        tabs.addTab("Staff", staffPanel);
        tabs.addTab("Prescriptions", prescriptionPanel);
        tabs.addTab("Referrals", referralPanel);

        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(UiStyle.pagePadding());
        center.add(tabs, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 12, 6, 12));
        statusBar.add(status, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);

        setStatus(summaryCounts());
    }

    private String summaryCounts() {
        return String.format(
            "Loaded: %d patients, %d clinicians, %d appointments, %d prescriptions, %d referrals",
            store.getPatients().size(),
            store.getClinicians().size(),
            store.getAppointments().size(),
            store.getPrescriptions().size(),
            store.getReferrals().size()
        );
    }

    private JToolBar buildToolbar() {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);

        JButton reload = new JButton("Reload CSV");
        reload.addActionListener(e -> {
            int ok = JOptionPane.showConfirmDialog(
                this,
                "Reload from CSV will discard unsaved changes in memory. Continue?",
                "Reload",
                JOptionPane.YES_NO_OPTION
            );
            if (ok != JOptionPane.YES_OPTION) return;

            try {
                store.clearAll();
                new CsvLoader(store).loadAllFromDataFolder("data");
                patientPanel.refreshFromOutside();
                clinicianPanel.refreshFromOutside();
                appointmentPanel.refreshFromOutside();
                prescriptionPanel.refreshFromOutside();
                referralPanel.refreshFromOutside();
                setStatus("Reloaded from CSV. " + summaryCounts());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Reload failed: " + ex.getMessage());
            }
        });

        JButton saveCsv = new JButton("Save to CSV");
        saveCsv.addActionListener(e -> {
            try {
                new CsvWriter().saveAllToDataFolder("data", store);
                setStatus("Saved data to CSV files in /data");
                JOptionPane.showMessageDialog(this, "Saved all changes to CSV files in /data");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Save failed: " + ex.getMessage());
            }
        });

        JButton about = new JButton("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(
            this,
            "Healthcare Management System\nJava Swing + MVC\n\n- Developed by Rameesa Asif.",
            "About",
            JOptionPane.INFORMATION_MESSAGE
        ));

        JButton quit = new JButton("Exit");
        quit.addActionListener(e -> dispose());

        bar.add(new JLabel("   Data folder: /data   "));
        bar.addSeparator();
        bar.add(reload);
        bar.addSeparator();
        bar.add(saveCsv);
        bar.addSeparator();
        bar.add(about);
        bar.addSeparator();
        bar.add(quit);

        return bar;
    }

    public void setStatus(String text) {
        status.setText(text);
    }
}
