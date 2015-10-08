package com.bird.file;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bird.module.FileNode;
import com.bird.module.FileNodeHash;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;

public class FileWordSplit {
	private static JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();

	/**
	 * 对一篇文章完成分词操作
	 * 
	 * @param fileNode
	 * @return
	 */
	private static FileNodeHash fileWordSplit(FileNode fileNode) {
		HashMap<String, Integer> fileWordHash = new HashMap<String, Integer>();
		FileNodeHash fileNodeHash = new FileNodeHash();
		fileNodeHash.setFileName(fileNode.getFileName());
		List<SegToken> list = jiebaSegmenter.process(fileNode.getFileContent(),
				SegMode.INDEX);
		String splitWord = null;
		for (SegToken segToken : list) {
			splitWord = segToken.word;
			if (fileWordHash.containsKey(splitWord)) {
				int count = fileWordHash.get(splitWord);
				fileWordHash.put(splitWord, count++);
			} else {
				fileWordHash.put(splitWord, 1);
			}
		}
		fileNodeHash.setWordHash(fileWordHash);
		return fileNodeHash;
	}

	public static List<FileNodeHash> getFilesHashList(File rootFile) {
		FileOperate.readFileContent(rootFile);// 读取文件内容
		List<FileNode> fileNodes = FileOperate.list;
		List<FileNodeHash> fileNodeHashs = new ArrayList<FileNodeHash>();
		for (FileNode fileNode : fileNodes) {
			fileNodeHashs.add(fileWordSplit(fileNode));
		}
		return fileNodeHashs;
	}

	public static void main(String[] args) {
		FileWordSplit.getFilesHashList(new File("D:\news"));
	}

}
