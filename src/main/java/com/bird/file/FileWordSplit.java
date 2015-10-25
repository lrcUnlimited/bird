package com.bird.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.bird.module.FileNode;
import com.bird.module.FileNodeHash;
import com.bird.module.FileWordNode;
import com.bird.module.KeyWordNode;
import com.bird.module.ReverseNode;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;

public class FileWordSplit {
	private static JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();
	private static HashSet<String> stopWordSet = StopWord.getStopWordSet();

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
			splitWord = segToken.word.trim();
			if ((!stopWordSet.contains(splitWord)) && splitWord.length() > 0) {
				if (fileWordHash.containsKey(splitWord)) {
					int count = fileWordHash.get(splitWord);
					fileWordHash.put(splitWord, ++count);// 先执行++操作
				} else {
					fileWordHash.put(splitWord, 1);
				}
			}
		}
		fileNodeHash.setWordHash(fileWordHash);

		return fileNodeHash;
	}

	/**
	 * 正向链表 索引->段->文章->词
	 * 
	 * @param rootFile
	 * @return
	 */

	public static List<FileNodeHash> getFilesHashList(File rootFile) {
		FileOperate.readFileContent(rootFile);// 读取文件内容
		List<FileNode> fileNodes = FileOperate.list;
		List<FileNodeHash> fileNodeHashs = new ArrayList<FileNodeHash>();
		for (FileNode fileNode : fileNodes) {
			fileNodeHashs.add(fileWordSplit(fileNode));
		}
		return fileNodeHashs;
	}

	/**
	 * 正向链表查找
	 * 
	 * @param keyWord
	 * @return
	 */

	public static List<String> getSearchResult(String keyWord) {
		long start = System.currentTimeMillis();
		List<FileNodeHash> fileNodeHashs = getFilesHashList(new File("D:\\news"));
		List<String> resultFileNames = new ArrayList<String>();
		for (FileNodeHash fileNodeHash : fileNodeHashs) {
			if (fileNodeHash.getWordHash().containsKey(keyWord)) {
				resultFileNames.add(fileNodeHash.getFileName()
						+ fileNodeHash.getWordHash().get(keyWord));
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("查询时间为:" + (end - start));
		return resultFileNames;
	}

	/**
	 * 添加新的节点
	 * 
	 * @param fileName
	 * @param word
	 * @param count
	 * @return
	 */
	private static ReverseNode addReverseNode(String fileName, String word,
			int count) {
		ReverseNode reverseNode = new ReverseNode();
		FileWordNode fileWordNode = new FileWordNode();
		fileWordNode.setCount(count);
		fileWordNode.setFileName(fileName);
		KeyWordNode keyWordNode = new KeyWordNode();
		keyWordNode.setWord(word);
		keyWordNode.setCount(count);
		List<FileWordNode> fileWordNodes = new ArrayList<FileWordNode>();
		fileWordNodes.add(fileWordNode);
		reverseNode.setFileWordNode(fileWordNodes);
		reverseNode.setKeyWordNode(keyWordNode);
		return reverseNode;
	}

	/**
	 * 构建逆向链表 词->文章
	 * 
	 * @param rootFile
	 * @return
	 */

	public static List<ReverseNode> toReverseNodes(File rootFile) {
		List<FileNodeHash> fileNodeHashs = getFilesHashList(rootFile);
		List<ReverseNode> reverseNodes = new ArrayList<ReverseNode>();
		List<ReverseNode> oneFileReverseNode = new ArrayList<ReverseNode>();
		String fileName = null;
		HashMap<String, Integer> wordHash = null;
		for (FileNodeHash fileNodeHash : fileNodeHashs) {
			fileName = fileNodeHash.getFileName();
			wordHash = fileNodeHash.getWordHash();
			Iterator<Map.Entry<String, Integer>> iterator = wordHash.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Integer> entry = iterator.next();
				String word = entry.getKey();// 单词
				int count = entry.getValue();// 出现次数
				boolean findFlag = false;
				// writeSplitWordToFile(word, fileName, count);
				ReverseNode reverseNode = null;
				if (reverseNodes.size() == 0) {
					reverseNode = addReverseNode(fileName, word, count);
					oneFileReverseNode.add(reverseNode);
				} else {
					for (ReverseNode nodeIn : reverseNodes) {
						if (nodeIn.getKeyWordNode().getWord().equals(word)) {
							FileWordNode fileWordNode = new FileWordNode();
							fileWordNode.setCount(count);
							fileWordNode.setFileName(fileName);
							nodeIn.getFileWordNode().add(fileWordNode);// 向已有单词添加新的节点信息
							int newCount = nodeIn.getKeyWordNode().getCount()
									+ count;
							nodeIn.getKeyWordNode().setCount(newCount);// 更新节点的次数
							findFlag = true;
							break;
						}
					}
					// 新的关键词出现
					if (!findFlag) {
						reverseNode = addReverseNode(fileName, word, count);
						oneFileReverseNode.add(reverseNode);
					}
				}

			}
			wordHash.clear();// 回收内存
			reverseNodes.addAll(oneFileReverseNode);
			oneFileReverseNode.clear();// 回收内存
		}
		return reverseNodes;
	}

	/**
	 * 
	 * @param word
	 * @param fileName
	 * @param count
	 */

	@SuppressWarnings("unused")
	private static void writeSplitWordToFile(String word, String fileName,
			int count) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter("D:\\news\\keyword.txt",
					true));
			writer.write(fileName + " | " + word + " | " + count);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// ignore
				}

			}

		}

	}

	public static void main(String[] args) {
		List<ReverseNode> reverseNodes = toReverseNodes(new File("D:\\news"));
		for (ReverseNode r : reverseNodes) {
			System.out.println(r.toString());
		}
	}
}
