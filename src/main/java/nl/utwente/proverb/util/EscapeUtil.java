package nl.utwente.proverb.util;

public class EscapeUtil {

    private EscapeUtil() { }

    public static String escapeURL(String string){
        string = escapeURLSpecialChar(string);
        string = escapeURLEndWithSpecialChar(string);
        string = escapeDXDOI(string);
        string = escapeHTTP(string);
        return string;
    }

    private static String escapeURLSpecialChar(String string){
        if (string.contains("]")){
            return string.replace("]", "");
        }
        return string;
    }

    private static String escapeURLEndWithSpecialChar(String string){
        if (string.endsWith("/") || string.endsWith("*")){
            return string.substring(0, string.length()-1);
        }
        return string;
    }

    private static String escapeDXDOI(String string){
        if (string.contains("dx.doi")){
            return string.replace("dx.doi", "doi");
        }
        return string;
    }

    private static String escapeHTTP(String string){
        if (string.contains("http:")){
            return string.replace("http:", "https:");
        }
        return string;
    }
}
