package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import model.*;

public class CsvLoader {
    private final DataStore store;

    public CsvLoader(DataStore store) {
        this.store = store;
    }

    public void loadAllFromDataFolder(String dataFolder) {
        loadPatients(new File(dataFolder, "patients.csv"));
        loadClinicians(new File(dataFolder, "clinicians.csv"));
        loadFacilities(new File(dataFolder, "facilities.csv"));
        loadStaff(new File(dataFolder, "staff.csv"));
        loadAppointments(new File(dataFolder, "appointments.csv"));
        loadPrescriptions(new File(dataFolder, "prescriptions.csv"));
        loadReferrals(new File(dataFolder, "referrals.csv"));
    }

    private String[] splitCsvLine(String line) {
        // Simple CSV parser that supports quoted fields and escaped quotes ("")
        java.util.ArrayList<String> out = new java.util.ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (inQuotes) {
                if (ch == '\"') {
                    // Escaped quote?
                    if (i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                        cur.append('\"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(ch);
                }
            } else {
                if (ch == ',') {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else if (ch == '\"') {
                    inQuotes = true;
                } else {
                    cur.append(ch);
                }
            }
        }
        out.add(cur.toString());

        return out.toArray(new String[0]);
    }

    public void loadPatients(File file) {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // header
            String line;
            while ((line = br.readLine()) != null) {
                String[] a = splitCsvLine(line);
                if (a.length < 14) continue;
                store.getPatients().add(new Patient(
                    a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9], a[10], a[11], a[12], a[13]
                ));
            }
        } catch (Exception ignored) {}
    }

    public void loadClinicians(File file) {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] a = splitCsvLine(line);
                if (a.length < 12) continue;
                store.getClinicians().add(new Clinician(
                    a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9], a[10], a[11]
                ));
            }
        } catch (Exception ignored) {}
    }

    public void loadAppointments(File file) {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] a = splitCsvLine(line);
                if (a.length < 13) continue;
                store.getAppointments().add(new Appointment(
                    a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9], a[10], a[11], a[12]
                ));
            }
        } catch (Exception ignored) {}
    }

    public void loadPrescriptions(File file) {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] a = splitCsvLine(line);
                if (a.length < 15) continue;
                store.getPrescriptions().add(new Prescription(
                    a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9], a[10], a[11], a[12], a[13], a[14]
                ));
            }
        } catch (Exception ignored) {}
    }

    public void loadReferrals(File file) {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] a = splitCsvLine(line);
                if (a.length < 16) continue;
                store.getReferrals().add(new Referral(
                    a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9], a[10], a[11], a[12], a[13], a[14], a[15]
                ));
            }
        } catch (Exception ignored) {}
    }

    public void loadFacilities(File file) {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] a = splitCsvLine(line);
                if (a.length < 11) continue;
                store.getFacilities().add(new Facility(
                    a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9], a[10]
                ));
            }
        } catch (Exception ignored) {}
    }

    public void loadStaff(File file) {
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] a = splitCsvLine(line);
                if (a.length < 12) continue;
                store.getStaff().add(new Staff(
                    a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9], a[10], a[11]
                ));
            }
        } catch (Exception ignored) {}
    }
}
