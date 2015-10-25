package com.bird.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bird.module.FileNode;

/**
 * 读取文件内容到fileNode的链表中
 * 
 * @author li
 * 
 */
public class FileOperate {
	public static List<FileNode> list = new ArrayList<FileNode>();

	public static void readFileContent(File rootFile) {

		if (rootFile.isDirectory()) {
			File[] childFiles = rootFile.listFiles();
			for (File f : childFiles) {
				readFileContent(f);
			}
		} else {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(rootFile));
				StringBuffer buffer = new StringBuffer();
				String fileLine = null;
				while ((fileLine = reader.readLine()) != null) {
					fileLine = fileLine.replaceAll("[\\p{C}\\p{Z}]", "");// 去除不可见字符，以及空格
					buffer.append(fileLine);
				}
				FileNode fileNode = new FileNode();
				fileNode.setFileName(rootFile.getName());
				fileNode.setFileContent(buffer.toString());
				list.add(fileNode);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

		}
	}

	public static void main(String[] args) {
		FileOperate.readFileContent(new File("D:\\test"));
		for (FileNode fileNode : FileOperate.list) {
			System.out.println(fileNode.getFileName() + " | "
					+ fileNode.getFileContent());
		}

	}
}
