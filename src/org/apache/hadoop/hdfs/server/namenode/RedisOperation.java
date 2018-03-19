package org.apache.hadoop.hdfs.server.namenode;
import redis.clients.jedis.Jedis;

/**
 * Created by Administrator on 2017/6/22.
 */
public class RedisOperation {
    public void operinc(String path){
        Jedis jedis = new Jedis("192.168.0.248");
        //Jedis jedis = RedisUtil.getJedis();
        long count=0;
        if(jedis.exists(path)) {
            count=Long.parseLong(jedis.get(path));
            System.out.println(count);
            count=jedis.incr(path);
            System.out.println(count);
        }
        else {
            jedis.set(path,"1");
            count=Long.parseLong(jedis.get(path));
            System.out.println(count);
        }
        jedis.close();
        //RedisUtil.returnResource(jedis);
    }
}

