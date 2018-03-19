package org.apache.hadoop.hdfs.server.namenode;

public class Semantic {
	private String content;
	private long createTime;
	private long updateTime;
	
	public Semantic(String content, long createTime, long updateTime) {
		this.content = content;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "Semantic [content=" + content + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + "]";
	}
}
