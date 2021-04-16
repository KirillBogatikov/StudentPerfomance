package com.students.func;

public interface UnsafeValueProcessor<I, O, E extends Throwable> {
	public O process(I input) throws E;
}
