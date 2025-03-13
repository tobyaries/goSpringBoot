package core.utils;

public class Utils {
    /**
     * 将字符串的第一个字母转换为小写。
     *
     * @param str 要转换的字符串
     * @return 转换后的字符串，如果输入为空或空字符串，则返回原始字符串
     */
    public static String lowerCaseFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        char firstChar = str.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            return Character.toLowerCase(firstChar) + str.substring(1);
        }
        return str;
    }
}
