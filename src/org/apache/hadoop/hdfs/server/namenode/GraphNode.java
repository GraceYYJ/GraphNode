/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.hadoop.hdfs.server.namenode;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.hdfs.protocol.ClientProtocol;
import org.apache.hadoop.hdfs.protocol.DatanodeID;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.hdfs.protocol.FSConstants.DatanodeReportType;
import org.apache.hadoop.hdfs.protocol.FSConstants.SafeModeAction;
import org.apache.hadoop.hdfs.protocol.FSConstants.UpgradeAction;
import org.apache.hadoop.hdfs.protocol.LocatedBlock;
import org.apache.hadoop.hdfs.protocol.LocatedBlocks;
import org.apache.hadoop.hdfs.server.common.UpgradeStatusReport;
import org.apache.hadoop.hdfs.server.protocol.DatanodeCommand;
import org.apache.hadoop.hdfs.server.protocol.DatanodeProtocol;
import org.apache.hadoop.hdfs.server.protocol.DatanodeRegistration;
import org.apache.hadoop.hdfs.server.protocol.NamespaceInfo;
import org.apache.hadoop.hdfs.server.protocol.UpgradeCommand;
import org.apache.hadoop.http.HttpServer;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.Server;
import org.apache.hadoop.net.NetUtils;
import org.eclipse.jdt.internal.compiler.ast.Clinit;

public class GraphNode implements ClientProtocol, DatanodeProtocol, GNProtocol, NewClientProtocol{
	
	private Server server;
	private InetSocketAddress serverAddress = null;
	
	private GNProtocol namenode = null;
	private ClientProtocol namenode1 = null;
	
	private Neo4jOperation neo4jOperation = null;
	private SemanticBuilder semanticBuilder  = null;

	public GraphNode(Configuration conf) {
		try {
			initialize(conf);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void initialize(Configuration conf) throws IOException {
		this.neo4jOperation = new Neo4jOperation();
		this.semanticBuilder = new SemanticBuilder();
		InetSocketAddress socAddr = GraphNode.getAddress();
		int handlerCount = 10;
		this.server = RPC.getServer(this, socAddr.getHostName(), socAddr.getPort(),
                handlerCount, false, conf);
		this.serverAddress = this.server.getListenerAddress(); 
		this.server.start();
	}

	@Override
	public long getProtocolVersion(String arg0, long arg1) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void blockReceived(DatanodeRegistration arg0, Block[] arg1,
			String[] arg2) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DatanodeCommand blockReport(DatanodeRegistration arg0, long[] arg1)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void commitBlockSynchronization(Block arg0, long arg1, long arg2,
			boolean arg3, boolean arg4, DatanodeID[] arg5) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void errorReport(DatanodeRegistration arg0, int arg1, String arg2)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long nextGenerationStamp(Block arg0) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UpgradeCommand processUpgradeCommand(UpgradeCommand arg0)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatanodeRegistration register(DatanodeRegistration arg0)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatanodeCommand[] sendHeartbeat(DatanodeRegistration arg0,
			long arg1, long arg2, long arg3, int arg4, int arg5)
			throws IOException {
		System.out.println(arg1 + " : " + arg2 + " : " + arg3 + " : " + arg4 + " : " + arg5);
		return null;
	}

	@Override
	public NamespaceInfo versionRequest() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abandonBlock(Block arg0, String arg1, String arg2)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LocatedBlock addBlock(String arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocatedBlock append(String arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean complete(String arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void create(String arg0, FsPermission arg1, String arg2,
			boolean arg3, short arg4, long arg5) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean delete(String arg0) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(String arg0, boolean arg1) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UpgradeStatusReport distributedUpgradeProgress(UpgradeAction arg0)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finalizeUpgrade() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fsync(String arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LocatedBlocks getBlockLocations(String arg0, long arg1, long arg2)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentSummary getContentSummary(String arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DatanodeInfo[] getDatanodeReport(DatanodeReportType arg0)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileStatus getFileInfo(String arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileStatus[] getListing(String arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getPreferredBlockSize(String arg0) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long[] getStats() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void metaSave(String arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean mkdirs(String arg0, FsPermission arg1) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refreshNodes() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean rename(String arg0, String arg1) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void renewLease(String arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportBadBlocks(LocatedBlock[] arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveNamespace() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOwner(String arg0, String arg1, String arg2)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPermission(String arg0, FsPermission arg1)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setQuota(String arg0, long arg1, long arg2) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean setReplication(String arg0, short arg1) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setSafeMode(SafeModeAction arg0) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setTimes(String arg0, long arg1, long arg2) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	public void join(){
		try {
			this.server.join();
		} catch (InterruptedException ie) {
		}
	}
	public void stop(){
		this.server.stop();
	}
	/*
	 * add by zounan 
	 */
	public static InetSocketAddress getAddress() {
		//return NetUtils.createSocketAddr("192.168.0.249:9000");
	    //return NetUtils.createSocketAddr("192.168.0.28:9000");
	   return NetUtils.createSocketAddr("127.0.0.1:9000");	  
	}
	
	public static void main(String args[]){
		GraphNode graphnode = createGraphnode();
		if(graphnode != null)
			graphnode.join();
	}

	private static GraphNode createGraphnode() {
		Configuration conf = new Configuration();
		GraphNode graphnode = new GraphNode(conf);
		return graphnode;
	}

	@Override
	public void sendState(int code, int flag) {
		if(flag == 1){
			InetSocketAddress nameNodeAddr = NameNode.getAddress();
			Configuration conf = new Configuration();
			try {
				this.namenode = (GNProtocol) 
					      RPC.waitForProxy(GNProtocol.class,
					                       GNProtocol.versionID,
					                       nameNodeAddr, 
					                       conf);
				namenode.sendState(code, 0);
			} catch (IOException e) {
			}
		}else{
			testMethod("Graphnode sendState");
		}
	}

	@Override
	public void sendCreateInfo(RpcINodeFile inode, int flag) {
		if(flag == 1){
			InetSocketAddress nameNodeAddr = NameNode.getAddress();
			Configuration conf = new Configuration();
			try {
				this.namenode = (GNProtocol) 
					      RPC.waitForProxy(GNProtocol.class,
					                       GNProtocol.versionID,
					                       nameNodeAddr, 
					                       conf);
				namenode.sendCreateInfo(inode, 0);
			} catch (IOException e) {
			}
		}else{
			long fileId = neo4jOperation.addINodeFile(inode);
/*			if(inode.getFilePath().endsWith("pdf")){
				List<Semantic> semantics = semanticBuilder.getSemantics(getFileStatus(inode));
				if(semantics != null)
					neo4jOperation.addSemantic(semantics, fileId);
			}*/
		}		
	}

	@Override
	public void sendDeleteInfo(String path, boolean dirflag, int flag) {
		if(flag == 1){
			InetSocketAddress nameNodeAddr = NameNode.getAddress();
			Configuration conf = new Configuration();
			try {
				this.namenode = (GNProtocol) 
					      RPC.waitForProxy(GNProtocol.class,
					                       GNProtocol.versionID,
					                       nameNodeAddr, 
					                       conf);
				namenode.sendDeleteInfo(path, dirflag, 0);
			} catch (IOException e) {
			}
		}else{
			neo4jOperation.deleteINodeFile(path, dirflag);
			//testMethod("Graphnode sendDeleteInfo");
		}		
	}

	@Override
	public void sendUpdateInfo(String path, Map<String, Object> map, int flag) {
		if(flag == 1){
			InetSocketAddress nameNodeAddr = NameNode.getAddress();
			Configuration conf = new Configuration();
			try {
				this.namenode = (GNProtocol) 
					      RPC.waitForProxy(GNProtocol.class,
					                       GNProtocol.versionID,
					                       nameNodeAddr, 
					                       conf);
				namenode.sendUpdateInfo(path, map, 0);
			} catch (IOException e) {
			}
		}else{
			testMethod("Graphnode sendUpdateInfo");
		}		
	}

	@Override
	public void sendReadInfo(String path, int flag) {
		if(flag == 1){
			InetSocketAddress nameNodeAddr = NameNode.getAddress();
			Configuration conf = new Configuration();
			try {
				this.namenode = (GNProtocol) 
					      RPC.waitForProxy(GNProtocol.class,
					                       GNProtocol.versionID,
					                       nameNodeAddr, 
					                       conf);
				namenode.sendReadInfo(path,0);
			} catch (IOException e) {
			}
		}else{
			//连接redis，调用操作redis方法，该路径的文件访问次数+1
			testMethod("Graphnode sendReadInfo");
		}		
	}
	
	@Override
	public void sendACK(int code, int flag) {
		if(flag == 1){
			InetSocketAddress nameNodeAddr = NameNode.getAddress();
			Configuration conf = new Configuration();
			try {
				this.namenode = (GNProtocol) 
					      RPC.waitForProxy(GNProtocol.class,
					                       GNProtocol.versionID,
					                       nameNodeAddr, 
					                       conf);
				namenode.sendACK(code, 0);
			} catch (IOException e) {
			}
		}else{
			testMethod("Graphnode sendACK");
		}		
	}

	@Override
	public void testMethod(String content) {
		//org.apache.hadoop.hdfs.server.testPackage.testMethod.testMethod1(content);
	}
	
	@Override
	public FileStatus testMethod1(String path) {
		FileStatus fileStatus = null;
		InetSocketAddress nameNodeAddr = NameNode.getAddress();
		Configuration conf = new Configuration();
		try {
			this.namenode1 = (ClientProtocol) 
				      RPC.waitForProxy(ClientProtocol.class,
				                       ClientProtocol.versionID,
				                       nameNodeAddr, 
				                       conf);
			fileStatus = namenode1.getFileInfo(path);
		} catch (IOException e) {
		}
		return fileStatus;
	}

	@Override
	public FileStatus getFileStatusFromGraph(long pointId) {
		// TODO Auto-generated method stub
		//return testInf();
		RpcINodeFile inode = neo4jOperation.getINodeFile(pointId);
		if(inode != null){
			return getFileStatus(inode);
		}else{
			return null;
		}
	}

	@Override
	public FileStatus[] getSomeFileStatusFromGraph(String[] semantics) {
		List<String> list = new ArrayList<String>();
		for(String str : semantics){
			list.add(str);
		}
		List<RpcINodeFile> inodes = neo4jOperation.getINodeFileBySemantic(list);
		List<FileStatus> fileStatusList = new ArrayList<FileStatus>();
		if(inodes != null){
			for(RpcINodeFile inode : inodes){
				FileStatus fileStatus = getFileStatus(inode);
				fileStatusList.add(fileStatus);
			}
			FileStatus[] files = new FileStatus[fileStatusList.size()];
			fileStatusList.toArray(files);
			return files;
		}else{
			return null;
		}
	}

	@Override
	public void deleteFile(long pointId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteFile(Map<String, Object> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Semantic getSemanticFrom(long pointId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Semantic[] getSomeSemantic(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Semantic[] getFilesSemantic(long[] filePointIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileStatus[] graphInfluenceAnalysis() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileStatus[] graphRecommend(long FileId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void graphClustering() {
		// TODO Auto-generated method stub
		
	}
	private FileStatus getFileStatus(RpcINodeFile inode){
		INodeFile file = inode.getINodeFile();
		String path = inode.getFilePath();
		return new FileStatus(file.isDirectory() ? 0 : file.computeContentSummary().getLength(), 
		        file.isDirectory(), 
		        file.isDirectory() ? 0 : ((INodeFile)file).getReplication(), 
		        file.isDirectory() ? 0 : ((INodeFile)file).getPreferredBlockSize(),
		        file.getModificationTime(),
		        file.getAccessTime(),
		        file.getFsPermission(),
		        file.getUserName(),
		        file.getGroupName(),
		        new Path(path)); 
	}
	
	
	
	//test method
	private INodeFile inf;
	public FileStatus testInf(){
		return new FileStatus(inf.isDirectory() ? 0 : inf.computeContentSummary().getLength(), 
		        inf.isDirectory(), 
		        inf.isDirectory() ? 0 : ((INodeFile)inf).getReplication(), 
		        inf.isDirectory() ? 0 : ((INodeFile)inf).getPreferredBlockSize(),
		        inf.getModificationTime(),
		        inf.getAccessTime(),
		        inf.getFsPermission(),
		        inf.getUserName(),
		        inf.getGroupName(),
		        new Path("/input/test.txt"));
	}

	private List<INodeFile> list = new ArrayList<INodeFile>();
	@Override
	public long getFileCount() {
		return list.size();
	}

	@Override
	public long getSemanticCount() {
		return 0;
	}
}
