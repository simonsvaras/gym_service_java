package com.gym.gymmanagementsystem.dto.enums;

/**
 * Representuje dostupné kvality profilových fotografií.
 */
public enum PhotoQuality {
    LOW,
    MEDIUM,
    HIGH;

    /**
     * Vrací prefix souboru pro danou kvalitu (např. "low_", "medium_", "high_").
     */
    public String prefix() {
        return this.name().toLowerCase() + "_";
    }
}
