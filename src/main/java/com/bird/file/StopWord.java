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
		// 读取resource下的停词列表
		ClassLoader classLoader = StopWord.class.getClassLoader();
		File file = new File(classLoader.getResource("stopword.txt").getFile());
		HashSet<String> stopWordSet = new HashSet<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String stopWord = null;
			while ((stopWord = reader.readLine()) != null) {
				if (stopWord.contains(stopWord)) {
					System.out.println(stopWord);
				} else {
					stopWordSet.add(stopWord);
				}
			}
			return stopWordSet;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stopWordSet;
	}
}
