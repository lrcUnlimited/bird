package com.bird;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;

public class FileTest {
	public static void main(String[] args) {
		JiebaSegmenter segmenter = new JiebaSegmenter();
		String[] sentences = new String[] { "这是一个伸手不见五指的黑夜。我叫孙悟空，我爱北京，我爱Python和C++。" };
		for (String sentence : sentences) {
			System.out.println(segmenter.process(sentence, SegMode.SEARCH)
					.toString());
		}
		for (String sentence : sentences) {
			System.out.println(segmenter.process(sentence, SegMode.INDEX)
					.toString());
		}
		for (String sentence : sentences) {
			System.out.println(segmenter.sentenceProcess(sentence));
		}

	}

}
