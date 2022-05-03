package dev.ricobrase.chatcalculator;

public class Util {
    private Util() {

    }

    public static String convertDoubleToString(double input) {
        if (input == Math.floor(input) && !Double.isInfinite(input) && input <= Integer.MAX_VALUE && input >= Integer.MIN_VALUE) {
            input = Math.floor(input);
            return String.format("%d", (int)input);
        }
        return String.format("%f", input);
    }
}
