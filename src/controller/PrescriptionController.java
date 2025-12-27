package controller;

import java.io.File;
import java.util.List;

import data.DataStore;
import model.Clinician;
import model.Patient;
import model.Prescription;
import util.OutputWriter;

public class PrescriptionController {
    private final DataStore store;

    public PrescriptionController(DataStore store) {
        this.store = store;
    }

    public List<Prescription> getAll() {
        return store.getPrescriptions();
    }

    public void add(Prescription p) {
        store.getPrescriptions().add(p);
    }

    public void delete(Prescription p) {
        store.getPrescriptions().remove(p);
    }

    public File generateOutput(Prescription pr) throws Exception {
        Patient patient = store.findPatientById(pr.getPatientId());
        if (patient == null) throw new IllegalArgumentException("Patient ID not found: " + pr.getPatientId());
        Clinician clinician = store.findClinicianById(pr.getClinicianId());
        return OutputWriter.writePrescription("output/prescriptions", pr, patient, clinician);
    }
}
