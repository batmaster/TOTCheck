package com.tot.totcheck;

import java.util.HashMap;

public class SharedValues {
	private static HashMap mapList;
	
	
	private SharedValues () {
		
	}
	
	public static HashMap getMapList() {
		if (mapList == null)
			mapList = new HashMap();
		
		return mapList;
	}
}
