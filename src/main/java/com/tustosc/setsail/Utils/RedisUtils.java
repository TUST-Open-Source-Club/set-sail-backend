package com.tustosc.setsail.Utils;

import com.tustosc.setsail.Enums.RedisPrefix;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.HashMap;
import java.util.Map;

@Component
public class RedisUtils {

    private JedisPool jedisPool;

    private static final Logger logger= LoggerFactory.getLogger(RedisUtils.class);

    public RedisUtils() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 配置连接池参数
        jedisPoolConfig.setMaxTotal(8); // 最大链接数
        jedisPoolConfig.setMaxIdle(8); //最大空闲连接数
        jedisPoolConfig.setMinIdle(8); // 最小空闲连接数
        /**
         jedisPool = new JedisPool(jedisPoolConfig,"localhost",6379,
         redisUsername, redisPassword);
         **/
        jedisPool = new JedisPool(jedisPoolConfig,"localhost",6379);
        logger.info("Redis connected successfully.");
    }

    @PreDestroy
    void cleanUp(){
        if (jedisPool != null) {
            jedisPool.close();
        }
    }

    public boolean clean(RedisPrefix prefix){
        boolean ret=true;
        try(Jedis jedis=jedisPool.getResource()){
            ScanParams scanParams=new ScanParams();
            scanParams.match(prefix.toString()+"*");
            scanParams.count(1000);
            ScanResult<String> sr=jedis.scan("0", scanParams);
            for(String key: sr.getResult()){
                jedis.del(key);
            }
        }
        catch (Exception e){
            logger.error("",e);
            ret=false;
        }
        return ret;
    }
    public boolean set(RedisPrefix prefix, String key, String value){
        boolean ret=true;
        try(Jedis jedis=jedisPool.getResource()){
            jedis.set(prefix.toString()+key, value, new SetParams().px(24*60*60*1000L));
        }
        catch (Exception e){
            logger.error("",e);
            ret=false;
        }
        return ret;
    }
    public boolean set(RedisPrefix prefix, String key, String value, Long expireSeconds){
        boolean ret=true;
        try(Jedis jedis=jedisPool.getResource()){
            jedis.set(prefix.toString()+key, value, new SetParams().px(expireSeconds*1000L));
        }
        catch (Exception e){
            logger.error("",e);
            ret=false;
        }
        return ret;
    }

    public boolean set(RedisPrefix prefix, String key, Map<String, String> value){
        boolean ret=true;
        try(Jedis jedis=jedisPool.getResource()){
            Pipeline pipeline = jedis.pipelined();
            for(Map.Entry<String, String> entry:value.entrySet()){
                pipeline.hset(prefix.toString()+key,entry.getKey(),entry.getValue());
                pipeline.hexpire(prefix.toString()+key, 60*60*24, entry.getKey());
            }
            pipeline.sync();
        }
        catch (Exception e){
            logger.error("",e);
            ret=false;
        }
        return ret;
    }
    public boolean set(RedisPrefix prefix, String key, String key2, String value){
        boolean ret=true;
        try(Jedis jedis=jedisPool.getResource()){
            jedis.hset(prefix.toString()+key,key2,value);
        }
        catch (Exception e){
            logger.error("",e);
            ret=false;
        }
        return ret;
    }
    public boolean set(RedisPrefix prefix, String key, Map<String, String> value, Long expireSeconds){
        boolean ret=true;
        try(Jedis jedis=jedisPool.getResource()){
            jedis.hset(prefix.toString()+key, value);
            jedis.expire(prefix.toString()+key, expireSeconds);
        }
        catch (Exception e){
            logger.error("",e);
            ret=false;
        }
        return ret;
    }


    public String get(RedisPrefix prefix, String key){
        String ret="";
        try(Jedis jedis=jedisPool.getResource()){
            ret=jedis.get(prefix.toString()+key);
        }
        catch (Exception e){
            logger.error("",e);
        }
        return ret;
    }

    public Map<String, String> getAll(RedisPrefix prefix, String key){
        Map<String, String> ret=new HashMap<>();
        try(Jedis jedis=jedisPool.getResource()){
            ret=jedis.hgetAll(prefix.toString()+key);
        }
        catch (Exception e){
            logger.error("",e);
        }
        return ret;
    }

    public boolean delete(RedisPrefix prefix, String key){
        long ret=0;
        try(Jedis jedis=jedisPool.getResource()){
            ret=jedis.del(prefix.toString()+key);
        }
        catch (Exception e){
            logger.error("",e);
        }
        return ret>0;
    }
    public boolean deleteAll(RedisPrefix prefix, String key){
        long ret=0;
        try(Jedis jedis=jedisPool.getResource()){
            Map<String, String> map=jedis.hgetAll(prefix.toString()+key);
            Pipeline pipeline=jedis.pipelined();
            for(Map.Entry<String, String> entry:map.entrySet()){
                pipeline.hdel(prefix.toString(),entry.getKey());
            }
            pipeline.sync();
        }
        catch (Exception e){
            logger.error("",e);
            ret=1;
        }
        return ret>0;
    }
}
