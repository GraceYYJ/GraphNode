package org.apache.hadoop.hdfs.server.namenode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;

/*
 * Java run Python.exe 
 * get hashCode 
 */
public class Java2Python {
	
	private String runCode = "python";
//	private String runCodePath = "/home/deeplearn/lightyear/yifeiliu/extract_stage3_testset.py";
	private String runCodePath = "/home/deeplearn/lightyear/yifeiliu/call_extract3.py";
//	private String runCodePath = "/home/zounan/JavaPython3.py";
	private String picPath = "/home/zounan/pic";
	
	public Java2Python(){}
	/*
	 * 构建学习模型
	 * 当文件数量第一次达到10000时 
	 */
	private void buildModel(){
		
	}
	
	/*
	 * 生成原始文件
	 * 将HDFS中的数据块文件转换成可以使用的文件
	 * 返回文件名 
	 */
	private String createFile(FileStatus fileStatus, long fileId){
		try {
	
			FileSystem fileSystem = FileSystemBuild.getFileSystem();
			FSDataInputStream in = fileSystem.open(fileStatus.getPath()); 
			byte[] bs = new byte[(int) fileStatus.getLen()];
			in.read(bs);
			
			File file = new File(picPath + "/" + fileId + ".jpg");
			FileOutputStream out = new FileOutputStream(file);
			out.write(bs);
			out.close();
			in.close();
		} catch (Exception e) {
		}
		return  fileId+ ".jpg";
	}
	
	/*
	 *  
	 */
	public String getHashCode(FileStatus fileStatus, long fileId){
		String fileName = createFile(fileStatus, fileId);
		//String fileName = "52.jpg";
		String fileInfo = picPath + "/" + fileName;
		String hasdCode = null;
		String args[] = new String[]{runCode, runCodePath, fileInfo};
		//FileWriteTool.writeFuc("getHashCode " + 1, runCode + " " + runCodePath + " " + fileInfo);
		try {
			//FileWriteTool.writeFuc("line : " + 1 , "flag1");
			Process process = Runtime.getRuntime().exec(args);
//			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//			hasdCode = in.readLine();
//			in.close();
//			process.waitFor();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			//FileWriteTool.writeFuc("line : " + 2 , "flag2");
			String line;
			line = in.readLine();
			//FileWriteTool.writeFuc("line : " , line);
			while ((line = in.readLine()) != null) {
//				System.out.println("HashCode : " + line);
				//FileWriteTool.writeFuc("line : " , line);
			}
			in.close();
			process.waitFor();
			//FileWriteTool.writeFuc("line : " + 3 , "flag3");
			
		} catch (Exception e) {
			//FileWriteTool.writeFuc("line : " + 4 , "flag4");
		}
		//FileWriteTool.writeFuc("getHashCode " + 2, "hashCode : " + hasdCode);
		return hasdCode;
	}
	
}
