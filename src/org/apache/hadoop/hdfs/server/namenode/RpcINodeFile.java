package org.apache.hadoop.hdfs.server.namenode;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.fs.permission.PermissionStatus;
import org.apache.hadoop.hdfs.protocol.Block;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class RpcINodeFile implements Writable{
	
	private String filePath;

	private String name;
	private Short replication;
	private long modificationTime;
	private long accessTime;
	private long preferredBlockSize;
	private Block[] blocks;
	
	private String userName;
	private String groupName;
	private short fp;
	
	public RpcINodeFile(){
		this(null,null,(short)0,0,0,0,null,null,null,(short)0);
	}
	public RpcINodeFile(String filePath,String name, Short replication, long modificationTime,
			long accessTime, long preferredBlockSize, Block[] blocks,
			String userName, String groupName, short fp) {
		this.filePath = filePath;
		this.name = name;
		this.replication = replication;
		this.modificationTime = modificationTime;
		this.accessTime = accessTime;
		this.preferredBlockSize = preferredBlockSize;
		this.blocks = blocks;
		this.userName = userName;
		this.groupName = groupName;
		this.fp = fp;
	}
	
	public RpcINodeFile(INodeFile inode, String filePath) {
		this.filePath = filePath;
		this.name = inode.getLocalName();
		this.replication = inode.getReplication();
		this.modificationTime = inode.getModificationTime();
		this.accessTime = inode.getAccessTime();
		this.preferredBlockSize = inode.getPreferredBlockSize();
		this.blocks = inode.getBlocks();
		this.userName = inode.getUserName();
		this.groupName = inode.getGroupName();
		this.fp = inode.getFsPermissionShort();
	}
	
	public INodeFile getINodeFile(){
		FsPermission fs = new FsPermission((short)0);
		fs.fromShort(this.fp);
		PermissionStatus p = new PermissionStatus(this.userName, this.groupName, fs);
		INodeFile inodeFile = new INodeFile(p, 
											this.blocks.length, 
											this.replication, 
											this.modificationTime, 
											this.accessTime, 
											this.preferredBlockSize);
		for(int i = 0 ; i < this.blocks.length ; i ++){
			inodeFile.setBlock(i, new BlocksMap().addINode(this.blocks[i],inodeFile));
		}
		return inodeFile;
	}
	
	@Override
	public void write(DataOutput out) throws IOException {
		Text.writeString(out, filePath);
		Text.writeString(out, name);
		out.writeShort(replication);
		out.writeLong(modificationTime);
		out.writeLong(accessTime);
		out.writeLong(preferredBlockSize);
		out.writeInt(blocks.length);
		for(Block b : blocks)
			b.write(out);
		out.writeShort(fp);
		Text.writeString(out, userName);
		Text.writeString(out, groupName);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.filePath = Text.readString(in);
		this.name = Text.readString(in);
		this.replication = in.readShort();
		this.modificationTime = in.readLong();
		this.accessTime = in.readLong();
		this.preferredBlockSize = in.readLong();
		int blockCount = in.readInt();
		Block[] bs = new Block[blockCount];
		for(int i = 0 ; i < blockCount ; i ++){
			bs[i]= new Block();
			bs[i].readFields(in);
		}
		this.blocks = bs;
		this.fp = in.readShort();
		this.userName = Text.readString(in);
		this.groupName = Text.readString(in);
	}	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "RpcINodeFile [filePath =" + filePath + ",name=" + name + ", replication=" + replication
				+ ", modificationTime=" + modificationTime + ", accessTime="
				+ accessTime + ", preferredBlockSize=" + preferredBlockSize
				+ ", blocks=" + Arrays.toString(blocks) + ", userName="
				+ userName + ", groupName=" + groupName + ", fp=" + fp + "]";
	}

}
