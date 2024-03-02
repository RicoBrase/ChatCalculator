package dev.ricobrase.chatcalculator;

public final class Util {
    private Util() {
    }

    /**
     * This function will convert a double to a string, stripping the decimals if the input is a whole number.
     * e.g.:
     * 30.0 => 30
     * 30.5 => 30.5
     * @param input The input double to convert to string.
     * @return The input converted to string, removing decimals if input is a whole number.
     */
    public static String convertDoubleToString(double input) {
        if (input == Math.floor(input) && !Double.isInfinite(input) && input <= Integer.MAX_VALUE && input >= Integer.MIN_VALUE) {
            return String.format("%d", (int)Math.floor(input));
        }
        return String.format("%f", input);
    }
}
