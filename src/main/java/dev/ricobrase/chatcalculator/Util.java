package dev.ricobrase.chatcalculator;

public final class Util {
    private Util() {
    }

    public static String convertDoubleToString(double input) {
        if (input == Math.floor(input) && !Double.isInfinite(input) && input <= Integer.MAX_VALUE && input >= Integer.MIN_VALUE) {
            return String.format("%d", (int)Math.floor(input));
        }
        return String.format("%f", input);
    }
}
