package org.apache.hadoop.hdfs.server.namenode;

import java.util.List;
import java.util.Map;

import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.ipc.VersionedProtocol;

public interface NewClientProtocol extends VersionedProtocol{
	
	public static final long versionID = 18L;
	
	//获取文件元数据
	public FileStatus getFileStatusFromGraph(long pointId);
	public FileStatus[] getSomeFileStatusFromGraph(String[] semantics);
	//删除元数据节点
	public void deleteFile(long pointId);
	public void deleteFile(Map<String, Object> map);
	//获取语义元数据
	public Semantic getSemanticFrom(long pointId);
	public Semantic[] getSomeSemantic(Map<String, Object> map);
	public Semantic[] getFilesSemantic(long filePointIds[]);
	
	public FileStatus[] graphInfluenceAnalysis();
	public FileStatus[] graphRecommend(long FileId);
	public void graphClustering();
	
	//test Method
	public long getFileCount();
	public long getSemanticCount();
	
}
