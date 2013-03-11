package net.pkhsolutions.idispatch.rest.client;

public enum Urgency {

    A(1), B(2), C(3), D(4);
    private final int beeps;

    private Urgency(int beeps) {
        this.beeps = beeps;
    }

    public int getBeeps() {
        return beeps;
    }
}
