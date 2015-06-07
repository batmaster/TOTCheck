package com.tot.totcheck;

/**
 * Temporarally used instead of json_encode() from php.
 * @author batmaster
 *
 */
public class Parser {
	
	private Parser() {
		
	}
	
	public static String parse(String result) {
		result = result.replace(" ", "");
		result = result.replace("Array([", "{\"");
		result = result.replace("]=>", "\":\"");
		result = result.replace("[", "\",\"");
		result = result.replace(")", "\"},");
		result = result.split(",\",")[0];
		return "[" + result + "]";
	}
}
