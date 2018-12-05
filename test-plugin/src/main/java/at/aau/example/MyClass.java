package at.aau.example;

import at.aau.asserting.AssertLibrary;

import static at.aau.asserting.logics.FOL.*;

public class MyClass {


    public void poll(long timeout) {
        long t = 10;
        // poll for io until the timeout expires
        long now = System.currentTimeMillis();
        long deadline = now + timeout;
        AssertLibrary.assertFormula(ForAll("timeout", LTE(now, "deadline")));

        while (now <= deadline) {
            if (valid()) {
                now = System.currentTimeMillis();
            }

            if (valid()) {
                now = System.currentTimeMillis();
            }

            // Note that because the network client is shared with the background heartbeat thread,
            // we do not want to block in poll longer than the time to the next heartbeat.
            long remaining = Math.max(0, deadline - now);
            AssertLibrary.assertFormula(ForAll("remaining", GT("remaining", 0)));
            now = System.currentTimeMillis();

        }
    }

    public double multiply(int i, int i1) {
        return i*i1;
    }

    private boolean valid() {
        return Math.ceil(Math.random()*100) > 30;
    }
}
