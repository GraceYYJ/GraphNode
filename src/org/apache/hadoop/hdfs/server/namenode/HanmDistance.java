package org.apache.hadoop.hdfs.server.namenode;

/**
 * Created by Administrator on 2017/6/9.
 */
public class HanmDistance {
    public int hammingDistance(String hashcode1, String hashcode2) {
        long hash1[]=new long[6];
        long hash2[]=new long[6];
        String test=null;

        for(int i=0;i<6;i++){
            test=hashcode1.substring(i*16,i*16+16);
            hash1[i]=Long.parseLong(hashcode1.substring(i*16,i*16+16),2);
        }
        for(int i=0;i<6;i++){
            hash2[i]=Long.parseLong(hashcode2.substring(i*16,i*16+16),2);
        }
        int cnt = 0;
        for(int i=0;i<6;i++){
            hash1[i]=hash1[i]^hash2[i];
            while (hash1[i] != 0) {
                if ((hash1[i]  & 0x01) == 1)
                    cnt++;
                hash1[i]  = hash1[i]  >> 1;
            }
        }
        return cnt;
    }
}

