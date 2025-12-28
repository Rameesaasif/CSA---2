package controller;

import java.util.List;

import data.DataStore;
import model.Staff;

public class StaffController {
    private final DataStore store;

    public StaffController(DataStore store) {
        this.store = store;
    }

    public List<Staff> getAll() {
        return store.getStaff();
    }

    public void add(Staff s) {
        store.getStaff().add(s);
    }

    public void delete(Staff s) {
        store.getStaff().remove(s);
    }
}