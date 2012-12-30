package com.travianize.travianize.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс-обертка над стандартными для java средствами работы с регулярными выражениями Вместо классов Pattern, Matcher и
 * циклов используются функции и подход их использования аналогичный php
 */
public class RegexpUtils {

    public static interface Replacer {

        public String onMatch(List<String> matches);
    }
    private static HashMap<String, Pattern> cache = new HashMap<String, Pattern>();

    public void clearCache() {
        cache.clear();
    }

    public static String preg_replace_callback(String pattern, String input, Replacer by) {
        Pattern p = compile(pattern, false);
        Matcher m = p.matcher(input);
        final int gcount = m.groupCount();
        StringBuffer sb = new StringBuffer();
        ArrayList<String> row = new ArrayList<String>();

        while (m.find()) {
            try {
                row.clear();
                for (int i = 0; i <= gcount; i++) {
                    row.add(m.group(i));
                }
                m.appendReplacement(sb, by.onMatch(row));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }//end -- while --
        m.appendTail(sb);
        return sb.toString();
    }

    public static String preg_replace(String pattern, String input, String by) {
        Pattern p = compile(pattern, false);
        Matcher m = p.matcher(input);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            try {
                m.appendReplacement(sb, by);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }//end -- while --
        m.appendTail(sb);
        return sb.toString();
    }

    public static boolean preg_match(String pattern, String input, List<String> rez) {
        Pattern p = compile(pattern, true);
        Matcher m = p.matcher(input);
        final int gcount = m.groupCount();
        if (rez != null) {
            rez.clear();
        }
        if (m.matches()) {
            for (int i = 0; i <= gcount; i++) {
                if (rez != null) {
                    rez.add(m.group(i));
                }
            }
        }
        return rez.size() > 0;
    }

    public static boolean preg_match_all(String pattern, String input, List<List<String>> rez) {
        Pattern p = compile(pattern, true);
        Matcher m = p.matcher(input);
        final int gcount = m.groupCount();
        if (rez != null) {
            rez.clear();
        }
        //  m.find();
        while (m.find()) {
            ArrayList row = new ArrayList();
            for (int i = 0; i <= gcount; i++) {
                if (rez != null) {
                    row.add(m.group(i));
                }
            }
            if (rez != null) {
                rez.add(row);
            }
        }
        return rez.size() > 0;
    }

    public static Pattern compile(String pattern, boolean surroundBy) {
        if (cache.containsKey(pattern)) {
            return cache.get(pattern);
        }
        final String pattern_orig = pattern;

        final char firstChar = pattern.charAt(0);
        char endChar = firstChar;
        if (firstChar == '(') {
            endChar = '}';
        }
        if (firstChar == '[') {
            endChar = ']';
        }
        if (firstChar == '{') {
            endChar = '}';
        }
        if (firstChar == '<') {
            endChar = '>';
        }

        int lastPos = pattern.lastIndexOf(endChar);
        if (lastPos == -1) {
            throw new RuntimeException("Invalid pattern: " + pattern);
        }

        char[] modifiers = pattern.substring(lastPos + 1).toCharArray();
        int mod = 0;
        for (int i = 0; i < modifiers.length; i++) {
            char modifier = modifiers[i];
            switch (modifier) {
                case 'i':
                    mod |= Pattern.CASE_INSENSITIVE;
                    break;
                case 'd':
                    mod |= Pattern.UNIX_LINES;
                    break;
                case 'x':
                    mod |= Pattern.COMMENTS;
                    break;
                case 'm':
                    mod |= Pattern.MULTILINE;
                    break;
                case 's':
                    mod |= Pattern.DOTALL;
                    break;
                case 'u':
                    mod |= Pattern.UNICODE_CASE;
                    break;
            }
        }
        pattern = pattern.substring(1, lastPos);


        final Pattern rezPattern = Pattern.compile(pattern, mod);
        cache.put(pattern_orig, rezPattern);
        return rezPattern;
    }
}
