package org.apache.hadoop.hdfs.server.namenode;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class FileSystemBuild {
	private final static String core_site = "/usr/local/hadoop-0.20.2/conf/core-site.xml";
	private final static String hdfs_site = "/usr/local/hadoop-0.20.2/conf/hdfs-site.xml";
	private static Configuration conf = new Configuration();
	private static FileSystem fileSystem = null;
	
	public static FileSystem getFileSystem() throws Exception{
		if(fileSystem == null){
			conf.addResource(new Path(core_site));
			conf.addResource(new Path(hdfs_site));
			fileSystem = FileSystem.get(conf);
		}
		return fileSystem;
	}
}
