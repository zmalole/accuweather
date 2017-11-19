package com.olehprod.aw.gui.enums;

public enum EmulationMode {

    IPAD_PRO("iPad Pro");

    private final String emulator;

    /**
     * Constructor
     *
     * @param emulator
     */
    EmulationMode(String emulator) {
        this.emulator = emulator;
    }

    /**
     * Getter
     *
     * @return
     */
    public String getEmulator() {
        return emulator;
    }
}
