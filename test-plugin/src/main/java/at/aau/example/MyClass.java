package at.aau.example;

import at.aau.asserting.AssertLibrary;
import static at.aau.asserting.FOL.ForAll.ForAll;

public class MyClass {


    public void poll(long timeout) {
        // poll for io until the timeout expires
        long now = System.currentTimeMillis();
        long deadline = now + timeout;
        AssertLibrary.assertFormula(ForAll("name"));

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
            AssertLibrary.assertFormula(ForAll("remaining"));
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
