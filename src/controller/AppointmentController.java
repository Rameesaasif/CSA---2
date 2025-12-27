package controller;

import java.util.List;

import data.DataStore;
import model.Appointment;

public class AppointmentController {
    private final DataStore store;

    public AppointmentController(DataStore store) {
        this.store = store;
    }

    public List<Appointment> getAll() {
        return store.getAppointments();
    }

    public void add(Appointment a) {
        store.getAppointments().add(a);
    }

    public void delete(Appointment a) {
        store.getAppointments().remove(a);
    }
}
