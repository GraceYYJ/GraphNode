package org.apache.hadoop.hdfs.server.namenode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/*
 * Semantic builder
 * get keyword from file 
 */
public class SemanticBuilder {
	
	public SemanticBuilder(){}
	
	public List<Semantic> getSemantics(FileStatus fiLeStatus){
		FileSystem fileSystem;
		List<Semantic> list = new ArrayList<Semantic>();
		try {
			fileSystem = FileSystemBuild.getFileSystem();
			FSDataInputStream input = fileSystem.open(fiLeStatus.getPath()); 
			String[] keyWords = getKeywords(input);
			long now = System.currentTimeMillis();
			if(keyWords != null && keyWords.length >= 1){
				for(String keyword : keyWords){
					Semantic semantic = new Semantic(keyword, now, now);
					list.add(semantic);
				}
			}
		} catch (Exception e) {
			return null;
		}
		return list;
	}
	
	public String[] getkeyWords(String filePath){
		String[] keywords = null;
		File file = new File(filePath);
		try {
			FileInputStream input = new FileInputStream(file);
			keywords = getKeywords(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return keywords;
	}
		
	private String[] getKeywords(InputStream input){
		String keywords[] = null;
		try {
			RandomAccessBuffer source = new RandomAccessBuffer(input);
			PDFParser parse = new PDFParser(source);
			parse.parse();
			PDDocument document = parse.getPDDocument();
			PDFTextStripper content = new PDFTextStripper();
			String str = content.getText(document);
			String keywordContent = null;
			List<String> list = new ArrayList<String>();
			int start;
			int end;
			start = str.indexOf("Keywords") == -1 ? str.indexOf("Index Terms") : str.indexOf("Keywords") ;
			if(start == -1)
				return null;
			end = str.indexOf("INTRODUCTION") == -1 ? str.indexOf("Introduction") : str.indexOf("INTRODUCTION") ;
			if(end == -1)
				return null;
			if(start < end)
				keywordContent = str.substring(start + 9, end - 3).replaceAll(",", ";").replaceAll("\\r\\n", "").replaceAll("\\n", "");
			if(keywordContent != null){
				for(String s : keywordContent.split(";")){
					list.add(s.trim());
				}
			}
			
			int keyWordCount = 0;
			String lastKeyWord = list.get(list.size() - 1); 
			if(lastKeyWord.endsWith("I")){
				lastKeyWord = lastKeyWord.substring(0, lastKeyWord.length() - 1).trim();
				if(lastKeyWord.equals("")){
					keyWordCount = list.size() - 1;
				}else{
					list.set(list.size() - 1, lastKeyWord);
					keyWordCount = list.size();
				}
			}else{
				keyWordCount = list.size();
			}
			
			keywords = new String[keyWordCount];
			int index = 0;
			for(int i = 0 ; i < keyWordCount ; i ++){
				keywords[i] = list.get(i)
									  .replaceAll("¡ª", " ")
									  .replaceAll("¨C", " ")
									  .replaceAll("\\.", "")
									  .replaceAll(" +", " ")
									  .trim();
			}

		} catch (Exception e) {
			return null;
		}
		return keywords;
	}
}
