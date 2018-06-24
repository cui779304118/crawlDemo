package com.cup.cw.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by majingmj on 18-1-23.
 */
public class RegexUtils {
    /**
     * 正则表达式匹配
     * @param regex
     * @param sentence
     * @return
     */
    public static Matcher match(String regex, String sentence){
        Pattern p= Pattern.compile(regex);
        Matcher m=p.matcher(sentence);
        return m;
    }

    /**
     * 返回matcher的第number的group字符串
     * @param regex
     * @param sentence
     * @param number
     * @return
     */
    public static String group(String regex, String sentence, int number) {
        Matcher m = match(regex, sentence);
        if (m.find()) {
            return m.group(number);
        } else {
            return null;
        }
    }

    /**
     * 给定url, 返回保存的路径
     * 比如https://www.csdn.net/nav/ai, urlsave=1,0, 则返回/nav/ai
     * 比如https://www.csdn.net/nav/ai/test.html, urlsave=2,1,0, 则返回/nav/ai/test.html
     * @param url
     * @param urlsave
     * @return
     */
    public static String geturlpath(String url, String urlsave) {
        String result = null;
        if (url == null || urlsave == null) {
            return result;
        }
        String[] urls = url.split("/");
        String[] saves = urlsave.split(",");
        if (urls.length < 1 || saves.length == 0) {
            return result;
        }

        for (int i = 0; i < saves.length; i++) {
            saves[i] = saves[i].trim();
            if (saves[i].length() == 0) {
                continue;
            }
            Integer index = Integer.valueOf(saves[i]);
            if (index >= 0 && index < urls.length) {
                if (result == null) {
                    result = "/" + urls[index];
                } else {
                    result = result + "/" + urls[index];
                }
            }
        }
        return result;
    }
}
