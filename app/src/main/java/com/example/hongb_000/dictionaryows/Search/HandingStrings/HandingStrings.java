package com.example.hongb_000.dictionaryows.Search.HandingStrings;

/**
 * Created by hongb_000 on 7/27/2015.
 */
public class HandingStrings {

    public static String newString(String word){
        String string = "";
        char a[] = word.toCharArray();
        for(int i = 0; i < word.length(); i++){
            if((a[i] == '∴')||(a[i] == ':')){
                a[i] = '\n';
            }
            else if(a[i] == '☆'){
                a[i-1] = '\n';
            }
            else if((a[i] == '◆')||(a[i] == '※')){
                a[i-1] = '\n';
                a[i+1] = '\t';
            }
            else if((a[i] == '\\')&& (a[i+1] == 'n')){
                a[i] = '\n';
                a[i+1] = '\0';
            }
            else if((a[i] == '\\')&& (a[i+1] == 't')){
                a[i] = '\t';
                a[i+1] = '\0';
            }
        }
        for(int i = 0; i < word.length(); i++){
            string += a[i];
        }
        return string;
    }

    public static String newStringToSearchKanji (String word) {

        StringBuilder builder = new StringBuilder("(");
        char[] arrayChar = word.toCharArray();
        for (int i = 0; i < arrayChar.length; i++){
            if (i != 0) {
                builder.append(",");
            }
            builder.append("'");
            builder.append(arrayChar[i]);
            builder.append("'");
        }
        builder.append(")");
        return builder.toString();
    }

    public static String convertStringtoKanji(String string){
        StringBuilder stringBuilder =  new StringBuilder();
        char[] arrayChar = string.toCharArray();
        for (int i = 0; i < arrayChar.length; i++){
            if (arrayChar[i] == '\u300c'){
                while (arrayChar[i] != '\u300d'){
                    if (arrayChar[i] >= '\u4e00' && arrayChar[i] <= '\u9faf'){
                        if(stringBuilder.indexOf(arrayChar[i]+"") == -1){
                            stringBuilder.append(arrayChar[i]);
                        }
                    }
                    i++;
                }
            }
        }
        return stringBuilder.toString();
    }

    public static CharSequence trim(CharSequence text) {
        int len = text.length();
        if (text == null) {
            return "";
        }

        while (--len >= 0&& Character.isWhitespace(text.charAt(len))) {}

        return  text.subSequence(0, len+1);
    }

    public static String setHTMLforText (String content) {
        StringBuilder builder = new StringBuilder("");
        content = content.trim();
        char[] arrayChar = content.toCharArray();

        if (arrayChar[0] == '∴') {
            builder.append("<div style=\"margin-left:0px\"> <font color = 'red' >");
        } else if (arrayChar[0] == '☆') {
            builder.append("<div style=\"margin-left:0px\" > <font color = 'blue' >");
        } else if (arrayChar[0] == '◆') {
            builder.append("<div style=\"margin-left:0px\" > <font color = 'green' >");
        }

        builder.append(arrayChar[0]);

        for (int i = 1; i < arrayChar.length; i++) {
            if (arrayChar[i] == '∴') {
                if (arrayChar[i-1] != 0) {
                    builder.append("</font></div>");
                }
                builder.append("<div style=\"margin-left:0px\" > <font color = 'red' >");

            }else if (arrayChar[i] == '☆') {
                if ((int) arrayChar[i-1] != 0) {
                    builder.append("</font></div>");
                }
                builder.append("<div style=\"margin-left:0px\"> <font color = 'blue' >");

            } else if (arrayChar[i] == '◆') {
                if (arrayChar[i-1] != 0) {
                    builder.append("</font></div>");
                }
                builder.append("<div style=\"margin-left:0px\"> <font color = 'green' >");

            }else if (arrayChar[i] == '※') {
                builder.append("</font></div>");
                int temp = i;

                while (true) {

                    if ('\u3040' <= arrayChar[i+1] && arrayChar[i+1] <= '\u9faf' ) {
                        builder.append("<div style=\"margin-left:15px\" > <font color = 'black' >");
                        while ((i - temp) > 0) {
                            builder.append(arrayChar[temp]);
                            temp++;
                        }
                        break;
                    } else if ('\u022f' <= arrayChar[i+1] && arrayChar[i+1] <= '\u1EF4'){
                        builder.append("<div style=\"margin-left:15px\" ><font color = 'yellow' >");
                        while ((i - temp) >= 0) {
                            builder.append(arrayChar[temp]);
                            temp++;
                        }
                        break;
                    }
                    i++;
                }
            }else if (arrayChar[i] == ':') {
                builder.append("</font>");
                while (true) {
                    if ('\u3040' <= arrayChar[i+1] && arrayChar[i+1] <= '\u9faf' ) {
                        builder.append("<font color = 'black' >");
                        break;
                    } else if ('\u0000' <= arrayChar[i+1] && arrayChar[i+1] <= '\u1ef9'){
                        builder.append("<font color = 'yellow' >");
                        break;
                    }
                    i++;
                }
            }

            builder.append(arrayChar[i]);
        }

        builder.append("</font></div>");

        return builder.toString();
    }
}
