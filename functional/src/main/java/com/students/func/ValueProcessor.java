package com.students.func;

public interface ValueProcessor<I, O> extends UnsafeValueProcessor<I, O, Throwable> {
	public O process(I input);
}
