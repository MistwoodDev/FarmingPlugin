package net.mistwood.FarmingPlugin.Utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessageHandler {

    private Locale locale;
    private Map<String, String> messages;

    public MessageHandler(String language, String country) {
        this.locale = new Locale(language, country);
        this.messages = new HashMap<>();
    }

    // TODO: Take in some sort of config or something (`messages.yml`) and parse it
    public void loadMessages() {

    }

    public String getMessage(String key) {
        if (messages.containsKey(key)) {
            return messages.get(key);
        } else {
            return "$" + key + "$";
        }
    }

    public String getErrorMessage(String key) {
        return Helper.colorfy("&c" + getMessage(key));
    }

    public Locale getLocale() {
        return locale;
    }

}
