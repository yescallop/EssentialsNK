package cn.yescallop.essentialsnk.lang;

import cn.nukkit.Server;
import cn.nukkit.lang.TextContainer;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class BaseLang {
    public static final String FALLBACK_LANGUAGE = "eng";

    protected String langName;

    protected Map<String, String> lang = new HashMap<>();
    protected Map<String, String> fallbackLang = new HashMap<>();


    public BaseLang(String lang) {
        this(lang, null);
    }

    public BaseLang(String lang, String path) {
        this(lang, path, FALLBACK_LANGUAGE);
    }

    public BaseLang(String lang, String path, String fallback) {
        this.langName = lang.toLowerCase();

        if (path == null) {
            path = "lang/";
            try {
                this.lang = this.loadLang(this.getClass().getClassLoader().getResourceAsStream(path + this.langName + ".ini"));
            } catch (NullPointerException e) {
                this.lang = new HashMap<>();
            }
            this.fallbackLang = this.loadLang(this.getClass().getClassLoader().getResourceAsStream(path + fallback + ".ini"));
        } else {
            try {
                this.lang = this.loadLang(path + this.langName + ".ini");
            } catch (NullPointerException e) {
                this.lang = new HashMap<>();
            }
            this.fallbackLang = this.loadLang(path + fallback + ".ini");
        }


    }

    public String getName() {
        return this.get("language.name");
    }

    public String getLang() {
        return langName;
    }

    protected Map<String, String> loadLang(String path) {
        try {
            String content = Utils.readFile(path);
            Map<String, String> d = new HashMap<>();
            for (String line : content.split("\n")) {
                line = line.trim();
                if (line.equals("") || line.charAt(0) == '#') {
                    continue;
                }
                String[] t = line.split("=");
                if (t.length < 2) {
                    continue;
                }
                String key = t[0];
                String value = "";
                for (int i = 1; i < t.length - 1; i++) {
                    value += t[i] + "=";
                }
                value += t[t.length - 1];
                if (value.equals("")) {
                    continue;
                }
                d.put(key, value);
            }
            return d;
        } catch (IOException e) {
            Server.getInstance().getLogger().logException(e);
            return null;
        }
    }

    protected Map<String, String> loadLang(InputStream stream) {
        try {
            String content = Utils.readFile(stream);
            Map<String, String> d = new HashMap<>();
            for (String line : content.split("\n")) {
                line = line.trim();
                if (line.equals("") || line.charAt(0) == '#') {
                    continue;
                }
                String[] t = line.split("=");
                if (t.length < 2) {
                    continue;
                }
                String key = t[0];
                String value = "";
                for (int i = 1; i < t.length - 1; i++) {
                    value += t[i] + "=";
                }
                value += t[t.length - 1];
                if (value.equals("")) {
                    continue;
                }
                d.put(key, value);
            }
            return d;
        } catch (IOException e) {
            Server.getInstance().getLogger().logException(e);
            return null;
        }
    }

    public String translateString(String str) {
        return this.translateString(str, new String[]{}, null);
    }

    public String translateString(String str, String... params) {
        return this.translateString(str, params, null);
    }

    public String translateString(String str, String param, String onlyPrefix) {
        return this.translateString(str, new String[]{param}, onlyPrefix);
    }

    public String translateString(String str, String[] params, String onlyPrefix) {
        String baseText = this.get(str);
        baseText = this.parseTranslation((baseText != null && (onlyPrefix == null || str.indexOf(onlyPrefix) == 0)) ? baseText : str, onlyPrefix);
        for (int i = 0; i < params.length; i++) {
            baseText = baseText.replace("{%" + i + "}", this.parseTranslation(params[i]));
        }

        return baseText;
    }

    public String translate(TextContainer c) {
        String baseText = this.parseTranslation(c.getText());
        if (c instanceof TranslationContainer) {
            baseText = this.internalGet(c.getText());
            baseText = this.parseTranslation(baseText != null ? baseText : c.getText());
            for (int i = 0; i < ((TranslationContainer) c).getParameters().length; i++) {
                baseText = baseText.replace("{%" + i + "}", this.parseTranslation(((TranslationContainer) c).getParameters()[i]));
            }
        }
        return baseText;
    }

    public String internalGet(String id) {
        if (this.lang.containsKey(id)) {
            return this.lang.get(id);
        } else if (this.fallbackLang.containsKey(id)) {
            return this.fallbackLang.get(id);
        }
        return null;
    }

    public String get(String id) {
        if (this.lang.containsKey(id)) {
            return this.lang.get(id);
        } else if (this.fallbackLang.containsKey(id)) {
            return this.fallbackLang.get(id);
        }
        return id;
    }

    protected String parseTranslation(String text) {
        return this.parseTranslation(text, null);
    }

    protected String parseTranslation(String text, String onlyPrefix) {
        String newString = "";

        String replaceString = null;

        int len = text.length();

        for (int i = 0; i < len; ++i) {
            char c = text.charAt(i);
            if (replaceString != null) {
                if (((int) c >= 0x30 && (int) c <= 0x39) || ((int) c >= 0x41 && (int) c <= 0x5a) || ((int) c >= 0x61 && (int) c <= 0x7a) || c == '.') {
                    replaceString += String.valueOf(c);
                } else {
                    String t = this.internalGet(replaceString.substring(1));
                    if (t != null && (onlyPrefix == null || replaceString.indexOf(onlyPrefix) == 1)) {
                        newString += t;
                    } else {
                        newString += replaceString;
                    }
                    replaceString = null;
                    if (c == '%') {
                        replaceString = String.valueOf(c);
                    } else {
                        newString += String.valueOf(c);
                    }
                }
            } else if (c == '%') {
                replaceString = String.valueOf(c);
            } else {
                newString += String.valueOf(c);
            }
        }

        if (replaceString != null) {
            String t = this.internalGet(replaceString.substring(1));
            if (t != null && (onlyPrefix == null || replaceString.indexOf(onlyPrefix) == 1)) {
                newString += t;
            } else {
                newString += replaceString;
            }
        }
        return newString;
    }
}
