package data;

import java.util.ArrayList;
import java.util.List;

import model.*;

public class DataStore {
    private final List<Patient> patients = new ArrayList<>();
    private final List<Clinician> clinicians = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();
    private final List<Prescription> prescriptions = new ArrayList<>();
    private final List<Referral> referrals = new ArrayList<>();
    private final List<Facility> facilities = new ArrayList<>();
    private final List<Staff> staff = new ArrayList<>();

    public List<Patient> getPatients() { return patients; }
    public List<Clinician> getClinicians() { return clinicians; }
    public List<Appointment> getAppointments() { return appointments; }
    public List<Prescription> getPrescriptions() { return prescriptions; }
    public List<Referral> getReferrals() { return referrals; }
    public List<Facility> getFacilities() { return facilities; }
    public List<Staff> getStaff() { return staff; }

    public void clearAll() {
        patients.clear();
        clinicians.clear();
        appointments.clear();
        prescriptions.clear();
        referrals.clear();
        facilities.clear();
        staff.clear();
    }

    // --- Find helpers (used by controllers & output writers)
    public Patient findPatientById(String id) {
        return patients.stream().filter(p -> p.getPatientId().equals(id)).findFirst().orElse(null);
    }
    public Clinician findClinicianById(String id) {
        return clinicians.stream().filter(c -> c.getClinicianId().equals(id)).findFirst().orElse(null);
    }
    public Facility findFacilityById(String id) {
        return facilities.stream().filter(f -> f.getFacilityId().equals(id)).findFirst().orElse(null);
    }

    // --- ID generation for new records (keeps it simple for coursework)
    public String nextId(String prefix, List<String> existingIds) {
        int max = 0;
        for (String s : existingIds) {
            try {
                // Extract trailing number: e.g. PAT-0012 -> 12
                String digits = s.replaceAll("\\D+", "");
                if (!digits.isEmpty()) max = Math.max(max, Integer.parseInt(digits));
            } catch (Exception ignored) {}
        }
        return String.format("%s-%04d", prefix, max + 1);
    }

    public String nextPatientId() {
        List<String> ids = patients.stream().map(Patient::getPatientId).toList();
        return nextId("PAT", ids);
    }

    public String nextFacilityId() {
    List<String> ids = facilities.stream().map(Facility::getFacilityId).toList();
    return nextId("FAC", ids);
    }

    public String nextStaffId() {
    List<String> ids = staff.stream().map(Staff::getStaffId).toList();
    return nextId("STF", ids);
    }

    public String nextPrescriptionId() {
        List<String> ids = prescriptions.stream().map(Prescription::getPrescriptionId).toList();
        return nextId("PRX", ids);
    }
    
    public String nextAppointmentId() {
        List<String> ids = appointments.stream().map(Appointment::getAppointmentId).toList();
        return nextId("APT", ids);
    }

    public String nextReferralId() {
        List<String> ids = referrals.stream().map(Referral::getReferralId).toList();
        return nextId("REF", ids);
    }
}
