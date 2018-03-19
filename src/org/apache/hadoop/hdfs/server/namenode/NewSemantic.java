package org.apache.hadoop.hdfs.server.namenode;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;

/**
 * Created by Administrator on 2017/6/26.
 */
public class NewSemantic {
/*    public static void main(String args[]){
        NewSemantic sem=new NewSemantic();
        System.out.println(sem.getHashSemantic("E:\\input\\1.txt"));
    }*/
    public String getHashSemantic(String filePath){
        try{
            FileSystem fileSystem=FileSystemBuild.getFileSystem();
            FSDataInputStream fsDataInputStream = fileSystem.open(new Path(filePath));
            InputStreamReader reader = new InputStreamReader(fsDataInputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String hashcode=bufferedReader.readLine();
            return hashcode;
        }
        catch (Exception e) {
            return null;
        }
    }
}
