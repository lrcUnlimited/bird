package com.bird.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class StopWord {
	/**
	 * 读取停词列表
	 * 
	 * @return
	 */
	public static HashSet<String> getStopWordSet() {
		HashSet<String> stopWordSet = new HashSet<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(
					"./stopword.txt")));
			String stopWord = null;
			while ((stopWord = reader.readLine()) != null) {
				stopWordSet.add(stopWord);
			}
			return stopWordSet;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		StopWord.getStopWordSet();
	}

}
