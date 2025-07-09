package com.gym.gymmanagementsystem.dto.enums;

import java.util.List;
import java.util.Random;

/**
 * Enum of motivational messages sent to the user when entering the gym.
 */
public enum MotivationalMessage {
    WORKOUT_NICELY("P\u011bkn\u011b si zacvi\u010d!"),
    TODAY_YOU_CAN("Dnes to zvl\u00e1dne\u0161 na jedni\u010dku!"),
    EVERY_SQUAT_COUNTS("Ka\u017ed\u00fd d\u0159ep se po\u010d\u00edt\u00e1!"),
    STRENGTH_GROWS("S\u00edla roste s ka\u017ed\u00fdm vstupem!"),
    FUTURE_YOU_THANKS("Tv\u016fj budouc\u00ed j\u00e1 ti pod\u011bkuje!");

    private final String text;

    MotivationalMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    private static final List<MotivationalMessage> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    /**
     * Returns a random motivational message text.
     */
    public static String randomText() {
        return VALUES.get(RANDOM.nextInt(SIZE)).getText();
    }
}
