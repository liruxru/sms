package com.bonc.util;

public class StringUtil {
	public static String trimString(String string){
		if (string == null || string.length() == 0 || string.toLowerCase().equals("null"))
			return "";
		// 去掉无用字符
		return string.replaceAll("[^\\s\\pP\\pL\\pM\\pZ\\pS\\pN\\pC0-9a-zA-Z\\u4E00-\\u9FA5\\uFF00-\\uFFFF]", "").trim();
	}

	public static boolean isNullOrEmpty(String string) {
		return string == null || string.length() == 0 || string.replace(" ", "").length() == 0;
	}
}
