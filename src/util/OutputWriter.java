package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import model.Patient;
import model.Prescription;
import model.Referral;
import model.Clinician;
import model.Facility;

public final class OutputWriter {
    private OutputWriter() {}

    public static void ensureDir(String dir) throws Exception {
        Path p = Path.of(dir);
        if (!Files.exists(p)) Files.createDirectories(p);
    }

    public static File writePrescription(String outDir, Prescription pr, Patient p, Clinician c) throws Exception {
        ensureDir(outDir);

        String filename = "prescription_" + pr.getPrescriptionId() + "_patient_" + pr.getPatientId() + ".txt";
        File file = Path.of(outDir, filename).toFile();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("PRESCRIPTION");
            bw.newLine();
            bw.write("Prescription ID: " + pr.getPrescriptionId());
            bw.newLine();
            bw.write("Issue Date: " + pr.getIssueDate());
            bw.newLine();
            bw.newLine();

            bw.write("PATIENT");
            bw.newLine();
            bw.write(p.getFirstName() + " " + p.getLastName() + " (ID: " + p.getPatientId() + ")");
            bw.newLine();
            bw.write("DOB: " + p.getDateOfBirth() + " | NHS: " + p.getNhsNumber());
            bw.newLine();
            bw.write("Phone: " + p.getPhoneNumber() + " | Email: " + p.getEmail());
            bw.newLine();
            bw.newLine();

            bw.write("PRESCRIBER");
            bw.newLine();
            if (c != null) {
                bw.write(c.getTitle() + " " + c.getFirstName() + " " + c.getLastName() + " (ID: " + c.getClinicianId() + ")");
                bw.newLine();
                bw.write("Speciality: " + c.getSpeciality() + " | GMC: " + c.getGmcNumber());
            } else {
                bw.write("Clinician ID: " + pr.getClinicianId());
            }
            bw.newLine();
            bw.newLine();

            bw.write("MEDICATION");
            bw.newLine();
            bw.write("Name: " + pr.getMedicationName());
            bw.newLine();
            bw.write("Dosage: " + pr.getDosage());
            bw.newLine();
            bw.write("Frequency: " + pr.getFrequency());
            bw.newLine();
            bw.write("Duration (days): " + pr.getDurationDays());
            bw.newLine();
            bw.write("Quantity: " + pr.getQuantity());
            bw.newLine();
            bw.write("Instructions: " + pr.getInstructions());
            bw.newLine();
            bw.newLine();

            bw.write("PHARMACY");
            bw.newLine();
            bw.write("Pharmacy: " + pr.getPharmacyName());
            bw.newLine();
            bw.write("Status: " + pr.getStatus());
            bw.newLine();
            bw.write("Collection date: " + pr.getCollectionDate());
            bw.newLine();
        }

        return file;
    }

    public static File writeReferral(String outDir, Referral r, Patient p, Clinician fromClin, Clinician toClin, Facility fromFac, Facility toFac) throws Exception {
        ensureDir(outDir);

        String filename = "referral_" + r.getReferralId() + "_patient_" + r.getPatientId() + ".txt";
        File file = Path.of(outDir, filename).toFile();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("REFERRAL LETTER");
            bw.newLine();
            bw.write("Referral ID: " + r.getReferralId());
            bw.newLine();
            bw.write("Referral Date: " + r.getReferralDate());
            bw.newLine();
            bw.write("Urgency: " + r.getUrgencyLevel());
            bw.newLine();
            bw.newLine();

            bw.write("PATIENT");
            bw.newLine();
            bw.write(p.getFirstName() + " " + p.getLastName() + " (ID: " + p.getPatientId() + ")");
            bw.newLine();
            bw.write("DOB: " + p.getDateOfBirth() + " | NHS: " + p.getNhsNumber());
            bw.newLine();
            bw.write("Address: " + p.getAddress() + ", " + p.getPostcode());
            bw.newLine();
            bw.write("Phone: " + p.getPhoneNumber() + " | Email: " + p.getEmail());
            bw.newLine();
            bw.newLine();

            bw.write("REFERRING CLINICIAN / FACILITY");
            bw.newLine();
            if (fromClin != null) {
                bw.write(fromClin.getTitle() + " " + fromClin.getFirstName() + " " + fromClin.getLastName() + " (ID: " + fromClin.getClinicianId() + ")");
                bw.newLine();
                bw.write("Speciality: " + fromClin.getSpeciality());
                bw.newLine();
            } else {
                bw.write("Referring clinician ID: " + r.getReferringClinicianId());
                bw.newLine();
            }
            if (fromFac != null) {
                bw.write(fromFac.getFacilityName() + " (" + fromFac.getFacilityType() + ")");
                bw.newLine();
                bw.write(fromFac.getAddress() + ", " + fromFac.getPostcode());
                bw.newLine();
                bw.write("Phone: " + fromFac.getPhoneNumber() + " | Email: " + fromFac.getEmail());
                bw.newLine();
            } else {
                bw.write("Referring facility ID: " + r.getReferringFacilityId());
                bw.newLine();
            }
            bw.newLine();

            bw.write("REFERRED TO");
            bw.newLine();
            if (toClin != null) {
                bw.write(toClin.getTitle() + " " + toClin.getFirstName() + " " + toClin.getLastName() + " (ID: " + toClin.getClinicianId() + ")");
                bw.newLine();
                bw.write("Speciality: " + toClin.getSpeciality());
                bw.newLine();
            } else {
                bw.write("Referred to clinician ID: " + r.getReferredToClinicianId());
                bw.newLine();
            }
            if (toFac != null) {
                bw.write(toFac.getFacilityName() + " (" + toFac.getFacilityType() + ")");
                bw.newLine();
                bw.write(toFac.getAddress() + ", " + toFac.getPostcode());
                bw.newLine();
                bw.write("Phone: " + toFac.getPhoneNumber() + " | Email: " + toFac.getEmail());
                bw.newLine();
            } else {
                bw.write("Referred to facility ID: " + r.getReferredToFacilityId());
                bw.newLine();
            }
            bw.newLine();

            bw.write("CLINICAL DETAILS");
            bw.newLine();
            bw.write("Reason: " + r.getReferralReason());
            bw.newLine();
            bw.write("Summary: " + r.getClinicalSummary());
            bw.newLine();
            bw.write("Requested investigations: " + r.getRequestedInvestigations());
            bw.newLine();
            bw.newLine();

            bw.write("STATUS / NOTES");
            bw.newLine();
            bw.write("Status: " + r.getStatus());
            bw.newLine();
            bw.write("Appointment ID: " + r.getAppointmentId());
            bw.newLine();
            bw.write("Notes: " + r.getNotes());
            bw.newLine();
        }

        return file;
    }
}
