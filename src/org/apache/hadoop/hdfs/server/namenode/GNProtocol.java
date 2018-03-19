package org.apache.hadoop.hdfs.server.namenode;

import java.util.Map;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.s3.INode;
import org.apache.hadoop.ipc.VersionedProtocol;

public interface GNProtocol extends VersionedProtocol{
	
	public static final long versionID = 8L;
	//state alive
	final static int alive = 0;
	//state dead
	final static int dead = 1;
	// success
	final static int suc = 2;
	//error
	final static int ero = 3;

	/*
	 * flag : 0  由另一个元数据服务器同步过来
	 * flag : 1 需要同步给另一个元数据服务器
	 */
	public void sendState(int code, int flag);
	public void sendCreateInfo(RpcINodeFile inode, int flag);
	public void sendDeleteInfo(String path, boolean dirflag , int flag);
	public void sendUpdateInfo(String path, Map<String,Object> map, int flag);
	public void sendReadInfo(String path,int flag);
	public void sendACK(int code, int flag);
	//test 
	public void testMethod(String content);
	public FileStatus testMethod1(String path);
}
