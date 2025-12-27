package controller;

import java.io.File;
import java.util.List;

import data.DataStore;
import data.ReferralManager;
import model.Clinician;
import model.Facility;
import model.Patient;
import model.Referral;
import util.OutputWriter;

public class ReferralController {
    private final DataStore store;
    private final ReferralManager referralManager = ReferralManager.getInstance(); // Singleton

    public ReferralController(DataStore store) {
        this.store = store;
    }

    public List<Referral> getAll() {
        return store.getReferrals();
    }

    public void add(Referral r) {
        store.getReferrals().add(r);
        referralManager.enqueue(r);
    }

    public void delete(Referral r) {
        store.getReferrals().remove(r);
    }

    public int queueSize() {
        return referralManager.queueSize();
    }

    public File generateOutput(Referral r) throws Exception {
        Patient p = store.findPatientById(r.getPatientId());
        if (p == null) throw new IllegalArgumentException("Patient ID not found: " + r.getPatientId());

        Clinician fromClin = store.findClinicianById(r.getReferringClinicianId());
        Clinician toClin = store.findClinicianById(r.getReferredToClinicianId());
        Facility fromFac = store.findFacilityById(r.getReferringFacilityId());
        Facility toFac = store.findFacilityById(r.getReferredToFacilityId());

        return OutputWriter.writeReferral("output/referrals", r, p, fromClin, toClin, fromFac, toFac);
    }
}
