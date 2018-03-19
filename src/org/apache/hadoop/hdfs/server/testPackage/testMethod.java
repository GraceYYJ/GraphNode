package org.apache.hadoop.hdfs.server.testPackage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.server.namenode.GNProtocol;
import org.apache.hadoop.hdfs.server.namenode.GraphNode;
import org.apache.hadoop.ipc.RPC;

public class testMethod {
	public static void testMethod1(String content){
		String pathname = "/home/file/test.txt";
		//String pathname = "E:/test_hadoop/my jar/test.txt";
		File file = new File(pathname);
		try {
			FileWriter fileWrite = new FileWriter(file ,true);
			PrintWriter pw = new PrintWriter(fileWrite);
			pw.println(content);
			pw.flush();
			pw.close();
		} catch (IOException e) {
		}
	}
}
