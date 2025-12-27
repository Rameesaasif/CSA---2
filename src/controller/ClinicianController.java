package controller;

import java.util.List;

import data.DataStore;
import model.Clinician;

public class ClinicianController {
    private final DataStore store;

    public ClinicianController(DataStore store) {
        this.store = store;
    }

    public List<Clinician> getAll() {
        return store.getClinicians();
    }

    public void add(Clinician c) {
        store.getClinicians().add(c);
    }

    public void delete(Clinician c) {
        store.getClinicians().remove(c);
    }

    public Clinician findById(String id) {
        return store.findClinicianById(id);
    }
}
