package data;

import java.util.ArrayDeque;
import java.util.Deque;

import model.Referral;

/**
 * Singleton required by the brief:
 * - one instance manages referral processing/queueing
 * - prevents duplicate/parallel referral manager instances
 */
public class ReferralManager {
    private static ReferralManager instance;

    private final Deque<Referral> referralQueue = new ArrayDeque<>();

    private ReferralManager() {}

    public static synchronized ReferralManager getInstance() {
        if (instance == null) instance = new ReferralManager();
        return instance;
    }

    public void enqueue(Referral referral) {
        if (referral != null) referralQueue.addLast(referral);
    }

    public Referral dequeue() {
        return referralQueue.pollFirst();
    }

    public int queueSize() {
        return referralQueue.size();
    }
}
