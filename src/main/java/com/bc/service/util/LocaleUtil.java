package com.bc.service.util;

import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;

/**
 * @author hp
 */
public final class LocaleUtil {
    
    private LocaleUtil() {}

    public static Locale getLocale(String key, Locale resultIfNone) {
        return key == null ? resultIfNone : 
                validOrDefault(Locale.forLanguageTag(key.replace('_', '-')), resultIfNone);
    }    
    
    private static Locale validOrDefault(Locale locale, Locale resultIfNone) {
        if((locale.getLanguage() == null || locale.getLanguage().isEmpty())
                && (locale.getCountry() == null || locale.getCountry().isEmpty())) {
            return resultIfNone;
        }else{
            return locale;
        }
    }
    
    public static Locale getLocaleForLang(String langKey, Locale outputIfNone) {
        try{
            Locale locale = langKey == null ? null : toLocale(langKey);
            return locale == null ? outputIfNone : locale;
        }catch(ParseException e) {
            return outputIfNone;
        }
    }

    /**
     * <p>Source: http://www.java2s.com/Code/Java/Data-Type/ConvertsaStringtoaLocale.htm</p>
     * 
     * <p>Converts a String to a Locale.</p>
     *
     * <p>This method takes the string format of a locale and creates the
     * locale object from it.</p>
     *
     * <pre>
     *   LocaleUtils.toLocale("en")         = new Locale("en", "")
     *   LocaleUtils.toLocale("en_GB")      = new Locale("en", "GB")
     *   LocaleUtils.toLocale("en_GB_xxx")  = new Locale("en", "GB", "xxx")   (#)
     * </pre>
     *
     * <p>(#) The behaviour of the JDK variant constructor changed between JDK1.3 and JDK1.4.
     * In JDK1.3, the constructor upper cases the variant, in JDK1.4, it doesn't.
     * Thus, the result from getVariant() may vary depending on your JDK.</p>
     *
     * <p>This method validates the input strictly.
     * The language code must be lowercase.
     * The country code must be uppercase.
     * The separator must be an underscore.
     * The length must be correct.
     * </p>
     *
     * @param str  the locale String to convert, null returns null
     * @return a Locale, null if null input
     * @throws ParseException if the string is an invalid format
     */
    private static Locale toLocale(String str) throws ParseException {
        Objects.requireNonNull(str);
        int len = str.length();
        if (len != 2 && len != 5 && len < 7) {
            throw new ParseException("Invalid locale format: " + str, 0);
        }
        char ch0 = str.charAt(0);
        char ch1 = str.charAt(1);
        if (ch0 < 'a' || ch0 > 'z' || ch1 < 'a' || ch1 > 'z') {
            throw new ParseException("Invalid locale format: " + str, 0);
        }
        if (len == 2) {
            return new Locale(str, "");
        } else {
            if (str.charAt(2) != '_') {
                throw new ParseException("Invalid locale format: " + str, 2);
            }
            char ch3 = str.charAt(3);
            if (ch3 == '_') {
                return new Locale(str.substring(0, 2), "", str.substring(4));
            }
            char ch4 = str.charAt(4);
            if (ch3 < 'A' || ch3 > 'Z' || ch4 < 'A' || ch4 > 'Z') {
                throw new ParseException("Invalid locale format: " + str, 4);
            }
            if (len == 5) {
                return new Locale(str.substring(0, 2), str.substring(3, 5));
            } else {
                if (str.charAt(5) != '_') {
                    throw new ParseException("Invalid locale format: " + str, 5);
                }
                return new Locale(str.substring(0, 2), str.substring(3, 5), str.substring(6));
            }
        }
    }
}
