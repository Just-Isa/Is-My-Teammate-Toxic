package com.kilic.ismyteammatetoxic.domain;

public enum ToxicityValue {
    TROLLITEMS(30),
    INTER(25),
    EARLYINTER(25),
    AFK(20),
    LOCKINGWRONGLANE(20),
    FEEDING(15),
    TOXICNAME(10),
    TROLL(5),
    BADSCORE(3),
    BOUGHTACCOUNT(3),
    TROLLBUTWIN(2);

    public final float value;

    private ToxicityValue(float value) {
        this.value = value;
    }
}
