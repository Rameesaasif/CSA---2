package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import model.*;

public class CsvWriter {

    public void saveAllToDataFolder(String dataFolder, DataStore store) throws Exception {
        savePatients(new File(dataFolder, "patients.csv"), store.getPatients());
        saveClinicians(new File(dataFolder, "clinicians.csv"), store.getClinicians());
        saveFacilities(new File(dataFolder, "facilities.csv"), store.getFacilities());
        saveStaff(new File(dataFolder, "staff.csv"), store.getStaff());
        saveAppointments(new File(dataFolder, "appointments.csv"), store.getAppointments());
        savePrescriptions(new File(dataFolder, "prescriptions.csv"), store.getPrescriptions());
        saveReferrals(new File(dataFolder, "referrals.csv"), store.getReferrals());
    }

    // Minimal RFC4180-style escaping: quote fields containing comma/quote/newline.
    private String esc(String s) {
        if (s == null) return "";
        boolean needsQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String v = s.replace("\"", "\"\"");
        return needsQuotes ? ("\"" + v + "\"") : v;
    }

    private void writeHeaderAndRows(File file, String[] header, List<String[]> rows) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < header.length; i++) {
                if (i > 0) bw.write(",");
                bw.write(header[i]);
            }
            bw.newLine();

            for (String[] row : rows) {
                for (int i = 0; i < header.length; i++) {
                    if (i > 0) bw.write(",");
                    String cell = (i < row.length) ? row[i] : "";
                    bw.write(esc(cell));
                }
                bw.newLine();
            }
        }
    }

    public void savePatients(File file, List<Patient> patients) throws Exception {
        String[] header = {
            "patient_id","first_name","last_name","date_of_birth","nhs_number","gender","phone_number","email",
            "address","postcode","emergency_contact_name","emergency_contact_phone","registration_date","gp_surgery_id"
        };
        java.util.ArrayList<String[]> rows = new java.util.ArrayList<>();
        for (Patient p : patients) {
            rows.add(new String[] {
                p.getPatientId(), p.getFirstName(), p.getLastName(), p.getDateOfBirth(), p.getNhsNumber(), p.getGender(),
                p.getPhoneNumber(), p.getEmail(), p.getAddress(), p.getPostcode(), p.getEmergencyContactName(),
                p.getEmergencyContactPhone(), p.getRegistrationDate(), p.getGpSurgeryId()
            });
        }
        writeHeaderAndRows(file, header, rows);
    }

    public void saveClinicians(File file, List<Clinician> clinicians) throws Exception {
        String[] header = {
            "clinician_id","first_name","last_name","title","speciality","gmc_number","phone_number","email",
            "workplace_id","workplace_type","employment_status","start_date"
        };
        java.util.ArrayList<String[]> rows = new java.util.ArrayList<>();
        for (Clinician c : clinicians) {
            rows.add(new String[] {
                c.getClinicianId(), c.getFirstName(), c.getLastName(), c.getTitle(), c.getSpeciality(), c.getGmcNumber(),
                c.getPhoneNumber(), c.getEmail(), c.getWorkplaceId(), c.getWorkplaceType(), c.getEmploymentStatus(), c.getStartDate()
            });
        }
        writeHeaderAndRows(file, header, rows);
    }

    public void saveAppointments(File file, List<Appointment> appts) throws Exception {
        String[] header = {
            "appointment_id","patient_id","clinician_id","facility_id","appointment_date","appointment_time","duration_minutes",
            "appointment_type","status","reason_for_visit","notes","created_date","last_modified"
        };
        java.util.ArrayList<String[]> rows = new java.util.ArrayList<>();
        for (Appointment a : appts) {
            rows.add(new String[] {
                a.getAppointmentId(), a.getPatientId(), a.getClinicianId(), a.getFacilityId(), a.getAppointmentDate(),
                a.getAppointmentTime(), a.getDurationMinutes(), a.getAppointmentType(), a.getStatus(), a.getReasonForVisit(),
                a.getNotes(), a.getCreatedDate(), a.getLastModified()
            });
        }
        writeHeaderAndRows(file, header, rows);
    }

    public void savePrescriptions(File file, List<Prescription> prs) throws Exception {
        String[] header = {
            "prescription_id","patient_id","clinician_id","appointment_id","prescription_date","medication_name","dosage",
            "frequency","duration_days","quantity","instructions","pharmacy_name","status","issue_date","collection_date"
        };
        java.util.ArrayList<String[]> rows = new java.util.ArrayList<>();
        for (Prescription p : prs) {
            rows.add(new String[] {
                p.getPrescriptionId(), p.getPatientId(), p.getClinicianId(), p.getAppointmentId(), p.getPrescriptionDate(),
                p.getMedicationName(), p.getDosage(), p.getFrequency(), p.getDurationDays(), p.getQuantity(), p.getInstructions(),
                p.getPharmacyName(), p.getStatus(), p.getIssueDate(), p.getCollectionDate()
            });
        }
        writeHeaderAndRows(file, header, rows);
    }

    public void saveReferrals(File file, List<Referral> refs) throws Exception {
        String[] header = {
            "referral_id","patient_id","referring_clinician_id","referred_to_clinician_id","referring_facility_id",
            "referred_to_facility_id","referral_date","urgency_level","referral_reason","clinical_summary",
            "requested_investigations","status","appointment_id","notes","created_date","last_updated"
        };
        java.util.ArrayList<String[]> rows = new java.util.ArrayList<>();
        for (Referral r : refs) {
            rows.add(new String[] {
                r.getReferralId(), r.getPatientId(), r.getReferringClinicianId(), r.getReferredToClinicianId(),
                r.getReferringFacilityId(), r.getReferredToFacilityId(), r.getReferralDate(), r.getUrgencyLevel(),
                r.getReferralReason(), r.getClinicalSummary(), r.getRequestedInvestigations(), r.getStatus(),
                r.getAppointmentId(), r.getNotes(), r.getCreatedDate(), r.getLastUpdated()
            });
        }
        writeHeaderAndRows(file, header, rows);
    }

    public void saveFacilities(File file, List<Facility> facilities) throws Exception {
        String[] header = {
            "facility_id","facility_name","facility_type","address","postcode","phone_number","email",
            "opening_hours","manager_name","capacity","specialities_offered"
        };
        java.util.ArrayList<String[]> rows = new java.util.ArrayList<>();
        for (Facility f : facilities) {
            rows.add(new String[] {
                f.getFacilityId(), f.getFacilityName(), f.getFacilityType(), f.getAddress(), f.getPostcode(),
                f.getPhoneNumber(), f.getEmail(), f.getOpeningHours(), f.getManagerName(), f.getCapacity(), f.getSpecialitiesOffered()
            });
        }
        writeHeaderAndRows(file, header, rows);
    }

    public void saveStaff(File file, List<Staff> staff) throws Exception {
        String[] header = {
            "staff_id","first_name","last_name","role","department","facility_id","phone_number","email",
            "employment_status","start_date","line_manager","access_level"
        };
        java.util.ArrayList<String[]> rows = new java.util.ArrayList<>();
        for (Staff s : staff) {
            rows.add(new String[] {
                s.getStaffId(), s.getFirstName(), s.getLastName(), s.getRole(), s.getDepartment(), s.getFacilityId(),
                s.getPhoneNumber(), s.getEmail(), s.getEmploymentStatus(), s.getStartDate(), s.getLineManager(), s.getAccessLevel()
            });
        }
        writeHeaderAndRows(file, header, rows);
    }
}
