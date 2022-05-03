package dev.ricobrase.chatcalculator;

public enum TranslationMessages {
    GLOBAL_CALC("chat.chatcalculator.globalcalcmessage"),
    INVALID_CHARACTERS("chat.chatcalculator.invalidcharacters"),
    NO_PREVIOUS_RESULT("chat.chatcalculator.nopreviousresult");

    private final String translationKey;
    TranslationMessages(String translationKey) {
        this.translationKey = translationKey;
    }

    public String getTranslationKey() {
        return translationKey;
    }
}
