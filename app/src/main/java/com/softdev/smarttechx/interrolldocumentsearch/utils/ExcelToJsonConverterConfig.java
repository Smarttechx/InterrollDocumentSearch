package com.softdev.smarttechx.interrolldocumentsearch.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class ExcelToJsonConverterConfig {

	private String sourceFile;
	private boolean pretty = false;
	private boolean omitEmpty = false;
	private boolean fillColumns = false;
	private int numberOfSheets = 0;
	private int rowLimit = 0; // 0 -> no limit
	private int rowOffset = 0;



	public static ExcelToJsonConverterConfig create(String file) {
		ExcelToJsonConverterConfig config = new ExcelToJsonConverterConfig();
			config.sourceFile =file;
		
		return config;
	}
	
	public String valid() {
		if(sourceFile==null) {
			return "Source file may not be empty.";
		}
		File file = new File(sourceFile);
		if(!file.exists()) {
			return "Source file does not exist.";
		}
		if(!file.canRead()) {
			return "Source file is not readable.";
		}

		return null;
	}
	
	// GET/SET

	public boolean isOmitEmpty() {
		return omitEmpty;
	}

	public void setOmitEmpty(boolean omitEmpty) {
		this.omitEmpty = omitEmpty;
	}


	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public boolean isPretty() {
		return pretty;
	}

	public void setPretty(boolean pretty) {
		this.pretty = pretty;
	}
	public int getRowLimit() {
		return rowLimit;
	}

	public void setRowLimit(int rowLimit) {
		this.rowLimit = rowLimit;
	}

	public int getRowOffset() {
		return rowOffset;
	}

	public void setRowOffset(int rowOffset) {
		this.rowOffset = rowOffset;
	}

	public int getNumberOfSheets()
	{
		return numberOfSheets;
	}
	public void setNumberOfSheets(int numberOfSheets)
	{
		this.numberOfSheets = numberOfSheets;

	}
	public boolean isFillColumns() {
		return fillColumns;
	}

	public void setFillColumns(boolean fillColumns) {
		this.fillColumns = fillColumns;
	}

}
