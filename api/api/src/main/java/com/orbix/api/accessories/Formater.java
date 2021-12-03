/**
 * 
 */
package com.orbix.api.accessories;

/**
 * @author GODFREY
 *
 */
public class Formater {
	public static String formatNine(String value) {
		String formattedValue = "";
		
		int tokenLength = 9;
    	int serialLength = value.length();
    	tokenLength = tokenLength - serialLength;
    	String token = "";
    	for(int i = 0; i < tokenLength; i++) {
    		token = token + "0";
    	}
    	value = token + value;
    	StringBuffer sb = new StringBuffer(value);
    	sb.insert(6, "-");
    	sb.insert(3, "-");
    	formattedValue = sb.toString();
		
		return formattedValue;
	}
	public static String formatSix(String value) {
		String formattedValue = "";
		
		int tokenLength = 6;
    	int serialLength = value.length();
    	tokenLength = tokenLength - serialLength;
    	String token = "";
    	for(int i = 0; i < tokenLength; i++) {
    		token = token + "0";
    	}
    	value = token + value;
    	StringBuffer sb = new StringBuffer(value);
    	sb.insert(3, "-");
    	formattedValue = sb.toString();
		
		return formattedValue;
	}
	public static String formatThree(String value) {
		String formattedValue = "";
		
		int tokenLength = 3;
    	int serialLength = value.length();
    	tokenLength = tokenLength - serialLength;
    	String token = "";
    	for(int i = 0; i < tokenLength; i++) {
    		token = token + "0";
    	}
    	value = token + value;
    	StringBuffer sb = new StringBuffer(value);    	
    	formattedValue = sb.toString();
		
		return formattedValue;
	}
	public static String formatFive(String value) {
		String formattedValue = "";
		
		int tokenLength = 5;
    	int serialLength = value.length();
    	tokenLength = tokenLength - serialLength;
    	String token = "";
    	for(int i = 0; i < tokenLength; i++) {
    		token = token + "0";
    	}
    	value = token + value;
    	StringBuffer sb = new StringBuffer(value);
    	sb.insert(3, "-");
    	formattedValue = sb.toString();
		
		return formattedValue;
	}
}
