package com.purduearc.roscc.server;

import java.util.HashMap;

public class QueryUtils {
	public static HashMap<String, String> parseQuery(String query) {
		HashMap<String, String> map = new HashMap<String, String>();
		for (String keyval : query.split("\\&")) {
			String parts[] = keyval.split("\\=");
			if (parts.length == 2) map.put(parts[0], parts[1]);
			else map.put(parts[0], "");
		}
		return map;
	}
}
