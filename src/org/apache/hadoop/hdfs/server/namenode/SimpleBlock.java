package org.apache.hadoop.hdfs.server.namenode;

public class SimpleBlock {
	private long blockId;
	private long numBytes;
	private long generationStamp;
	public SimpleBlock(long blockId, long numBytes, long generationStamp) {
		this.blockId = blockId;
		this.numBytes = numBytes;
		this.generationStamp = generationStamp;
	}
	public long getBlockId() {
		return blockId;
	}
	public void setBlockId(long blockId) {
		this.blockId = blockId;
	}
	public long getNumBytes() {
		return numBytes;
	}
	public void setNumBytes(long numBytes) {
		this.numBytes = numBytes;
	}
	public long getGenerationStamp() {
		return generationStamp;
	}
	public void setGenerationStamp(long generationStamp) {
		this.generationStamp = generationStamp;
	}
	@Override
	public String toString() {
		return "{blockId:" + blockId + ",numBytes:" + numBytes
				+ ",generationStamp:" + generationStamp +"}";
	}
	
}
