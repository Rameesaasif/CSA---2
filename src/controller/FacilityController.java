package controller;

import java.util.List;

import data.DataStore;
import model.Facility;

public class FacilityController {
    private final DataStore store;

    public FacilityController(DataStore store) {
        this.store = store;
    }

    public List<Facility> getAll() {
        return store.getFacilities();
    }

    public void add(Facility f) {
        store.getFacilities().add(f);
    }

    public void delete(Facility f) {
        store.getFacilities().remove(f);
    }

    public Facility findById(String id) {
        return store.findFacilityById(id);
    }
}
