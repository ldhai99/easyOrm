package io.github.ldhai99.easyOrm.base;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Tools {

    public static boolean IsNullOrEmpty(String a_value) {
        if (a_value != null && a_value.trim().length() != 0)
            return false;
        else
            return true;
    }

    public static String lastPart(String in) {
        int index = in.lastIndexOf(".");
        if (index == -1) {
            return in;
        } else
            return in.substring(index + 1);
    }

    public static String beforePart(String in) {
        int index = in.lastIndexOf(".");
        if (index == -1) {
            return "";
        } else
            return in.substring(0, index);
    }

    public static int StrToInt(String str) {

        try {
            int a = Integer.parseInt(str);
            return a;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static ArrayList split(String in_str) {
        ArrayList temp = new ArrayList();
        String str;
        StringTokenizer b = new StringTokenizer(in_str, "|");
        while (b.hasMoreTokens()) {
            str = b.nextToken();
            // System.out.println(str);
            temp.add(str);
        }
        return temp;
    }

    public static void main(String[] args) {
        System.out.println(lastPart("药品电子监管系统.a"));
    }
}
