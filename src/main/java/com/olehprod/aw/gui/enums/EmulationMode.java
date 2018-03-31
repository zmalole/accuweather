package com.olehprod.aw.gui.enums;

import lombok.Getter;

@Getter
public enum EmulationMode {

    IPAD_PRO("iPad Pro");

    private final String emulator;

    EmulationMode(String emulator) {
        this.emulator = emulator;
    }

}
