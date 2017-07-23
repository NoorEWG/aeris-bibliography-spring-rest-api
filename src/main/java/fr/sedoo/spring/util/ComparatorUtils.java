package fr.sedoo.spring.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ComparatorUtils {

	public static List<String> getOrdersList(Set<String> list) {

		List<String> aux = new ArrayList<String>();
		// Comparator<String> sortByName = (String o1, String o2) -> o1.compareTo(o2);

		for (String s : list) {
			aux.add(s);
		}

		// Collections.sort(aux, sortByName);
		
		return aux;
	}
}