package org.apache.hadoop.hdfs.server.namenode;

import java.util.*;

import javax.ws.rs.core.MediaType;

import org.apache.hadoop.hdfs.protocol.Block;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

public class Neo4jOperation {
	
//private static final String SERVER_ROOT_URI = "http://192.168.0.234:7474/db/data/";
	private static final String SERVER_ROOT_URI = "http://127.0.0.1:7474/db/data/";
	public WebResource setUserAndPassword(String nodeEntryPointUri) {
		WebResource resource = Client.create().resource(nodeEntryPointUri);
        resource.addFilter(new HTTPBasicAuthFilter("neo4j","zn326114349"));
		return resource;
	}
	
	private String cypherExecute(String cypher){
		ClientResponse response = null;
		try {
			String cypherOperation="{\"query\":\""+cypher+"\",\"params\":{}}";
			String nodeEntryPointUri = SERVER_ROOT_URI + "cypher";
			WebResource resource = setUserAndPassword(nodeEntryPointUri);
		    response = resource.accept( MediaType.APPLICATION_JSON )
		                					  .type( MediaType.APPLICATION_JSON )
		                					  .post( ClientResponse.class,cypherOperation);
		} catch (Exception e) {
			return null;
		}
	    if(response.getStatus() == 200)
	    	return response.getEntity(String.class);
	    else
	    	return null;
	}
	
	public long addINodeFile(RpcINodeFile inode){
		INodeFile file = inode.getINodeFile();
		String blockInfo = getBlockInfos(file);
		String userName = file.getUserName();
		String filePath=inode.getFilePath();
		String hashcode=new NewSemantic().getHashSemantic(filePath);
		userName = userName.contains("\\") ? userName.replaceAll("\\\\", ":") : userName;
		String cypher = "create (node:INodeFile{name:'" + inode.getName()
	                                        +"',replication:'" + file.getReplication()
	                                        +"',modificationTime:'" + file.getModificationTime()
	                                        +"',accessTime:'" + file.getAccessTime()
	                                        +"',preferredBlockSize:'" + file.getPreferredBlockSize()
	                                        +"',blockInfos:'" + blockInfo
	                                        +"',userName:'" + userName
	                                        +"',groupName:'" + file.getGroupName()
	                                        +"',fp:'" + file.getFsPermissionShort()
	                                        +"',filePath:'" + inode.getFilePath()
											+ "',hashcode:'" + hashcode   //增加哈希码的元数据
	                                        + "'}) return ID(node)";
		String result = cypherExecute(cypher);
		System.out.println("addINodeFile " + result);
		if(result == null)  //create inode point fail
			return -1;
		else
			return getId(result);
	}

	//当前所有节点id
	private List<Long> getAllNodeIds() {
		String cypher = "MATCH (n:INodeFile) RETURN DISTINCT(ID(n))";
		String result = cypherExecute(cypher);
		List<Long> nodelist = getIds(result);
		return nodelist;
	}

	//获取与当前添加节点的哈希值相似的节点列表
	private Map<Long,Integer> getNodeIds_hashSimi2(long fileIDNow,List<Long> nodelist) {
		String cypher = "start a=node(" + fileIDNow + ") return a.hashcode";
		String result = cypherExecute(cypher);
		String hashnow=getHc(result);
		Map<Long,Integer> idAndHash=new HashMap<>();
		if(nodelist!=null&&nodelist.size()>=1){
			for (long l : nodelist) {
				String cypher2 = "start a=node(" + l + ") return a.hashcode";
				String result2 = cypherExecute(cypher2);
				String hashx=getHc(result2);
				HanmDistance hmd=new HanmDistance();
				int hashdf=hmd.hammingDistance(hashx,hashnow);
				if (hashdf < 48) {
					idAndHash.put(l,hashdf);
				}
			}
		}
		return idAndHash;
	}

	//增加节点间的hashsimiliar关系
	public void addHashrelation(long fileId1,List<Long> nodelist){
		Map<Long,Integer> idAndHash =getNodeIds_hashSimi2(fileId1,nodelist);
		if(idAndHash!=null&&idAndHash.size()>=1){
			for (Map.Entry<Long, Integer> entry : idAndHash.entrySet()) {
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				createFilehashRelation(fileId1,entry.getKey(),entry.getValue());
			}
		}
	}

	//文件哈希相似关系
	private long createFilehashRelation(long fileId1, long fileId2,int hashdf) {
		String createTime = String.valueOf(System.currentTimeMillis());
		String cypher = "start a=node(" + fileId1 + "),b=node(" + fileId2 + ")"
				+ " create (a)-[r:Similiar{createTime:" + createTime +","+"hashsimiliar:"+hashdf+"}]->(b) return ID(r)";
		String result = cypherExecute(cypher);
		System.out.println("createFilehashRelation "+result);
		if(result != null){
			return getId(result);
		}else{
			return -1;
		}
	}

	//删除操作
	public boolean deleteINodeFile(String filePath, boolean dirflag){
		if(dirflag){ //delete all file in the dir
			return deleteDirINodeFile(filePath);
		}else{       //just delete file
			return deleteINodeFile(filePath);
		}
	}
	//删除整个目录下的文件
	private boolean deleteDirINodeFile(String filePath){
		boolean terminalresult=false;
		//filePath = filePath.charAt(filePath.length() - 1) == '/' ? filePath : filePath + '/';
		String cypher = "match (n:INodeFile) where n.filePath =~ '" + filePath + ".*' return n.filePath";
		String result = cypherExecute(cypher);
		if(result == null)
			return false;
		else{
			List<String> pathlist =getPaths(result);
			for(String path :pathlist){
				terminalresult=deleteINodeFile(path);
			}
			return terminalresult;
		}
	}
	//删除某个文件
	private boolean deleteINodeFile(String filePath){
		String cypher1 = "start a=node(*) match a-[r]-b  where a.filePath= '" + filePath + "' return ID(r)";
		String result1 = cypherExecute(cypher1);
		boolean terminalresult=false;
		if(result1 != null){
			System.out.println("deleteINodeFile " + result1);
			String cypher = "start a=node(*) match a-[r]-b  where a.filePath= '" + filePath + "' delete r,a";
			String result = cypherExecute(cypher);
			if(result != null){
				terminalresult= true;
			}else{
				terminalresult= false;
			}
		}
		String cypher2= "start a=node(*)  where a.filePath= '" + filePath + "' return ID(a)";
		String result2 = cypherExecute(cypher1);
		if(result2!=null){
			System.out.println("deleteINodeFile " + result2);
			String cypher = "start a=node(*) where a.filePath= '" + filePath + "' delete a";
			String result = cypherExecute(cypher);
			if(result != null){
				terminalresult= true;
			}else{
				terminalresult= false;
			}
		}
		return terminalresult;
	}


	private long getId(String result){
		JSONObject json = JSONObject.parseObject(result);
		String idStr = json.getString("data").replaceAll("\\]","").replaceAll("\\[","");
		long id = -1;
		try {
			id = Long.parseLong(idStr);
		} catch (Exception e) {
		}
		return id;
	}

	private String getHc(String result){
		JSONObject json = JSONObject.parseObject(result);
		String hcStr = json.getString("data").replaceAll("\\]","").replaceAll("\\[","").replaceAll("\"" ,"");
		return hcStr;
	}

	//get ID set
	private List<Long> getIds(String result){
		List<Long> list = new ArrayList<Long>();
		JSONObject json = JSONObject.parseObject(result);
		String idStr = json.getString("data").replaceAll("\\]","").replaceAll("\\[","").replaceAll(" ","");
		if(idStr.length() <= 0)
			return null;
		try {
			String[] idsStr = idStr.split(",");
			for(String s : idsStr){
				list.add(Long.parseLong(s));
			}
		} catch (Exception e) {
			return null;
		}
		return list;
	}
	private List<String> getHcs(String result){
		List<String> hclists=new ArrayList<String>();
		JSONObject json = JSONObject.parseObject(result);
		String hcStr = json.getString("data").replaceAll("\\]","").replaceAll("\\[","").replaceAll("\"" ,"").replaceAll(" ","");
		if(hcStr.length() <= 0)
			return null;
		try {
			String[] hcsStr = hcStr.split(",");
			for(String s : hcsStr){
				hclists.add(s);
			}
		} catch (Exception e) {
			return null;
		}
		System.out.println("getHcs "+hclists);
		return hclists;
	}
	private List<String> getPaths(String result){
		List<String> pathlists=new ArrayList<String>();
		JSONObject json = JSONObject.parseObject(result);
		String pathStr = json.getString("data").replaceAll("\\]","").replaceAll("\\[","").replaceAll("\"" ,"").replaceAll(" ","");
		if(pathStr.length() <= 0)
			return null;
		try {
			String[] pathsStr = pathStr.split(",");
			for(String s : pathsStr){
				pathlists.add(s);
			}
		} catch (Exception e) {
			return null;
		}
		System.out.println("getHcs "+pathlists);
		return pathlists;
	}

	private String getBlockInfos(INodeFile file) {
		Block[] blocks = file.getBlocks();
		String info = null;
		if(blocks != null && blocks.length >= 1){
			info = "[";
			for(Block b : blocks){
				long blockId = b.getBlockId();
				long numBytes = b.getNumBytes();
				long generationStamp = b.getGenerationStamp();
				SimpleBlock sb = new SimpleBlock(blockId, numBytes, generationStamp);
				testMethod(sb.toString());
				info += sb.toString() + ",";
			}
			info = info.substring(0, info.length() - 1);
			info += "]";
		}
		return info;
	}

	//if have same content semantic node return nodeId ,otherwise return -1
	private long ifHaveSameSemantic(Semantic semantic){
		String cypher = "match node where node.keyWord = '" + semantic.getContent().toLowerCase() + "' return DISTINCT(ID(node))";
		String result = cypherExecute(cypher);
		if(result != null)
			return getId(result);
		else
			return -1;
	}
	//update the property in semantic node
	private void updateTime(long nodeId){
		String newUpdateTime = String.valueOf(System.currentTimeMillis());
		String cypher = "start n=node(" + nodeId + ") set n.updateTime = " + newUpdateTime;
		cypherExecute(cypher);
	}

	public List<RpcINodeFile> getINodeFileBySemantic(List<String> list){
		List<RpcINodeFile> inodes = new ArrayList<RpcINodeFile>();
		String match = "";
		String where = "";
		for(int i = 0 ; i < list.size() ; i ++){
			if(i != 0){
				match += ",";
			}
			match += "semantic" + i + "<-[]-file";
		}
		for(int i = 0 ; i < list.size() ; i ++){
			if(i != 0 ){
				where += " and ";
			}
			where += "semantic" + i + ".keyWord=~'.*" + list.get(i) + ".*'";
		}
		String cypher = "match " + match + " where " + where + " return DISTINCT(ID(file))";
		String result = cypherExecute(cypher);
		if(result != null){
			List<Long> longs = getIds(result);
			if(longs != null && longs.size() >=1){
				for(Long l : longs){
					RpcINodeFile inode = getINodeFile(l);
					inodes.add(inode);
				}
				return inodes;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public RpcINodeFile getINodeFile(long id){
		String cypher = "start n=node(" + id + ") return n";
		String result = cypherExecute(cypher);
		if(result == null){
			return null;
		}else{
			JSONObject rpcInode = JSONObject.parseObject(
								    JSONObject.parseObject(
									  JSONObject.parseObject(result).getString("data").replaceAll("\\[", "").replaceAll("\\]", "")).getString("data"));
			String filePath = rpcInode.getString("filePath");
			String name = rpcInode.getString("name");
			short replication = rpcInode.getShort("replication");
			long modificationTime = rpcInode.getLong("modificationTime");
			long accessTime = rpcInode.getLong("accessTime");
			long preferredBlockSize = rpcInode.getLong("preferredBlockSize");
			String userName = rpcInode.getString("userName").contains(":") ? rpcInode.getString("userName").replaceAll(":", "\\\\") : rpcInode.getString("userName"); 
			String groupName = rpcInode.getString("groupName");
			short fp = rpcInode.getShort("fp");
			JSONArray blockInfo = JSONArray.parseArray("[" + rpcInode.getString("blockInfos") + "]");
			Iterator<Object> it = blockInfo.iterator();
			List<Block> list = new ArrayList<Block>();
			while(it.hasNext()){
				JSONObject json = (JSONObject) it.next();
				long blockId = json.getLong("blockId");
				long numbytes = json.getLong("numBytes");
				long generationStamp = json.getLong("generationStamp");
				Block block = new Block(blockId, numbytes, generationStamp);
				list.add(block);
			}
			Block[] blocks = new Block[list.size()];
			list.toArray(blocks);
			RpcINodeFile inode = new RpcINodeFile(filePath, name, replication, modificationTime, accessTime, preferredBlockSize, blocks, userName, groupName, fp);
			return inode;
		}
	}

	private void testMethod(String content) {
		//org.apache.hadoop.hdfs.server.testPackage.testMethod.testMethod1(content);
	}
}
