package controller;

import java.util.List;

import data.DataStore;
import model.Patient;

public class PatientController {
    private final DataStore store;

    public PatientController(DataStore store) {
        this.store = store;
    }

    public List<Patient> getAll() {
        return store.getPatients();
    }

    public void add(Patient p) {
        store.getPatients().add(p);
    }

    public void delete(Patient p) {
        store.getPatients().remove(p);
    }

    public Patient findById(String id) {
        return store.findPatientById(id);
    }
}
