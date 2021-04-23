package com.students.service.xlsx;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class XlsxReport {
	private Workbook book;
	private Sheet sheet;
	
	public XlsxReport(String name) {
		book = new XSSFWorkbook();
		sheet = book.createSheet(name);
	}
	
	public abstract void export(Sheet sheet);
	
	public UUID write(String dir) throws IOException {
		var id = UUID.randomUUID();
		var file = new File(dir, "%s.xlsx".formatted(id));
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		System.out.println(file.getAbsolutePath());
		try(OutputStream os = new FileOutputStream(file);
			BufferedOutputStream out = new BufferedOutputStream(os)) {
			
			export(sheet);
			book.write(out);
			book.close();
		}
		
		return id;
	}

}
