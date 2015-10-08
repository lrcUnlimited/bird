package com.bird.module;

import java.util.HashMap;

public class FileNodeHash {
	private String fileName;
	private HashMap<String, Integer> wordHash;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public HashMap<String, Integer> getWordHash() {
		return wordHash;
	}

	public void setWordHash(HashMap<String, Integer> wordHash) {
		this.wordHash = wordHash;
	}

}
