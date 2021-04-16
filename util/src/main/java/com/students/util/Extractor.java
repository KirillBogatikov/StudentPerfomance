package com.students.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.students.func.UnsafeValueProcessor;

public class Extractor {
	private static ClassLoader loader;
	
	public static void setLoader(ClassLoader loader) {
		Extractor.loader = loader;
	}
	
	private static <T> T open(String path, UnsafeValueProcessor<String, T, IOException> p) {
		try {
			return p.process(path);
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static URL openURL(String path) {
		return open(path, p -> loader.getResource(p));
	}
	
	public static InputStream openStream(String path) {
		return open(path, p -> loader.getResourceAsStream(p));
	}
	
	public static byte[] readBytes(String path) {
		return open(path, p -> {
			var bytes = new ByteArrayOutputStream();
			
			byte[] buffer = new byte[8192];
			int length;
			var stream = openStream(p);
			while((length = stream.read(buffer)) > -1) {
				bytes.write(buffer, 0, length);
			}
			
			return bytes.toByteArray();
		});
	}
	
	public static String readText(String path) {
		return open(path, p -> new String(readBytes(p)));
	}
	
	public static String readText(String dir, String name) {
		return readText("%s/%s".formatted(dir, name));
	}
}
