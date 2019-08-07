package com.os.pg;

public class Util {
    public static String toHump(String str) {
        StringBuffer sbf = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c) && i != 0) {
                sbf.append("_").append(c);
            } else {
                sbf.append(c);
            }
        }
        return sbf.toString();
    }
    public static String toHumpXmlName(String str) {
        StringBuffer sbf = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c) && i != 0) {
                sbf.append("_").append(Character.toLowerCase(c));
            } else {
                sbf.append(Character.toLowerCase(c));
            }
        }
        return sbf.toString();
    }

    public static String toField(String field) {
        return toLowerCaseFirstOne(field);
    }
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
