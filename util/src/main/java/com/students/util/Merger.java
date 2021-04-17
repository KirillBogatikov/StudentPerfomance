package com.students.util;

import com.students.func.Consumer;
import com.students.func.Producer;

public class Merger {
	public static <T> void merge(Consumer<T> set, Producer<T> getNew, Producer<T> getOld) {
		if (getNew.produce() == null) {
			set.consume(getOld.produce());
		}
	}
}
