package com.zheng.common.util;


import com.sun.xml.internal.xsom.impl.Ref;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Redis 工具类
 * Created by shuzheng on 2016/11/26.
 */
public class RedisUtil {

	static CacheManager cacheManager = CacheManager.create(new RedisUtil().getClass().getResource("/resources/ehcache-test.xml"));//URL是指配置文件所在路径 的URL，通常使用getClass().getResource("/config/ehcache/ehcache-test.xml")获取
    static Cache cache = cacheManager.getCache("zheng-upms-server-ehcache");
	protected static ReentrantLock lockPool = new ReentrantLock();
	protected static ReentrantLock lockJedis = new ReentrantLock();

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtil.class);

	// Redis服务器IP
	private static String IP = PropertiesFileUtil.getInstance("redis").get("master.redis.ip");

	// Redis的端口号
	private static int PORT = PropertiesFileUtil.getInstance("redis").getInt("master.redis.port");

	// 访问密码
	private static String PASSWORD = AESUtil.aesDecode(PropertiesFileUtil.getInstance("redis").get("master.redis.password"));
	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int MAX_ACTIVE = PropertiesFileUtil.getInstance("redis").getInt("master.redis.max_active");

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = PropertiesFileUtil.getInstance("redis").getInt("master.redis.max_idle");

	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = PropertiesFileUtil.getInstance("redis").getInt("master.redis.max_wait");

	// 超时时间
	private static int TIMEOUT = PropertiesFileUtil.getInstance("redis").getInt("master.redis.timeout");

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = false;



	/**
	 * redis过期时间,以秒为单位
	 */
	// 一小时
	public final static int EXRP_HOUR = 60 * 60;
	// 一天
	public final static int EXRP_DAY = 60 * 60 * 24;
	// 一个月
	public final static int EXRP_MONTH = 60 * 60 * 24 * 30;

	/**
	 * 初始化Redis连接池
	 */
	private static void initialPool() {
		try {
            LOGGER.error("This RedisUtil is backed by ecache, no need to initialize the pool" );
		} catch (Exception e) {
			LOGGER.error("First create JedisPool error : " + e);
		}
	}

	/**
	 * 在多线程环境同步初始化
	 */
	private static synchronized void poolInit() {
		if (null == cache) {
			initialPool();
		}
	}


	/**
	 * 同步获取Jedis实例
	 * @return Jedis
	 */
	public synchronized static Cache getJedis() {
		poolInit();


		return cache;
	}

	/**
	 * 设置 String
	 * @param key
	 * @param value
	 */
	public synchronized static void set(String key, String value) {
		try {
			value = StringUtils.isBlank(value) ? "" : value;

			cache.put(new Element(key, value));

		} catch (Exception e) {
			LOGGER.error("Set key error : " + e);
		}
	}

	/**
	 * 设置 byte[]
	 * @param key
	 * @param value
	 */
	public synchronized static void set(byte[] key, byte[] value) {
		try {
            cache.put(new Element(key, value));

		} catch (Exception e) {
			LOGGER.error("Set key error : " + e);
		}
	}

	/**
	 * 设置 String 过期时间
	 * @param key
	 * @param value
	 * @param seconds 以秒为单位
	 */
	public synchronized static void set(String key, String value, int seconds) {
		try {
            LOGGER.warn("this cache will be expire in {} seconds", seconds);
            Element element = new Element(key, value, false);
            element.setTimeToIdle(seconds);
			cache.put(element);
		} catch (Exception e) {
			LOGGER.error("Set keyex error : " + e);
		}
	}

	/**
	 * 设置 byte[] 过期时间
	 * @param key
	 * @param value
	 * @param seconds 以秒为单位
	 */
	public synchronized static void set(byte[] key, byte[] value, int seconds) {
		try {
            LOGGER.warn("since the backend technology is ehcache, so the time is not used");
            set(key, value);
		} catch (Exception e) {
			LOGGER.error("Set key error : " + e);
		}
	}

	/**
	 * 获取String值
	 * @param key
	 * @return value
	 */
	public synchronized static String get(String key) {
        Element element = cache.get(key);
        if(element == null)
            return null;
        else
		    return (String)element.getObjectValue();
	}

	/**
	 * 获取byte[]值
	 * @param key
	 * @return value
	 */
	public synchronized static byte[] get(byte[] key) {
        Element element = cache.get(key);
        if(element == null)
            return null;
        else
            return (byte[])element.getObjectValue();
	}

	/**
	 * 删除值
	 * @param key
	 */
	public synchronized static void remove(String key) {
		try {
			cache.remove(key);
		} catch (Exception e) {
			LOGGER.error("Remove keyex error : " + e);
		}
	}

	/**
	 * 删除值
	 * @param key
	 */
	public synchronized static void remove(byte[] key) {
		try {
            cache.remove(key);
		} catch (Exception e) {
			LOGGER.error("Remove keyex error : " + e);
		}
	}

	/**
	 * lpush
	 * @param key
	 * @param key
	 */
	public synchronized static void lpush(String key, String... strings) {
		try {
			Element element = cache.get(key);
            if(element == null){
                List<String> list = new ArrayList<String>();
                for (String s:strings
                     ) {
                    list.add(s);
                }
                cache.put(new Element(key, list));
            }else {
                List<String> list = (List<String>) element.getObjectValue();
                for (String s:strings
                        ) {
                    list.add(s);
                }
            }

		} catch (Exception e) {
			LOGGER.error("lpush error : " + e);
		}
	}

	/**
	 * lrem
	 * @param key
	 * @param count
	 * @param value
	 */
	public synchronized static void lrem(String key, long count, String value) {
        List<String> list = (List<String>) cache.get(key).getObjectValue();
		try {
			if(count > 0){
			    long removed = 0;
                for(int i = 0; i< list.size() && removed < count; i++){
                    if(value.equals(list.get(i))){
                        list.remove(i);
                        removed ++;
                    }
                }
            }else if(count < 0){
                long removed = 0;
                for(int i = list.size(); i > 0 && removed < -count; i--){
                    if(value.equals(list.get(i-1))){
                        list.remove(i);
                        removed ++;
                    }
                }
            }else{
                for(int i = 0; i< list.size() ; i++){
                    if(value.equals(list.get(i))){
                        list.remove(i);
                    }
                }
            }
		} catch (Exception e) {
			LOGGER.error("lrem error : " + e);
		}
	}

	/**
	 * sadd
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public synchronized static void sadd(String key, String value, int seconds) {
		try {
            Element element = cache.get(key);
            if(element == null){
                Set<String> set = new HashSet<String>();
                set.add(value);
                cache.put(new Element(key, set));
            }else {
                Set<String> set = (Set<String>) element.getObjectValue();
                set.add(value);
            }

		} catch (Exception e) {
			LOGGER.error("sadd error : " + e);
		}
	}

	/**
	 * incr
	 * @param key
	 * @return value
	 */
	public synchronized static Long incr(String key) {
	    long currentValue = 0;
		Element element = cache.get(key);
		if(null == element){
            cache.put(new Element(key, 1));
            currentValue = 1;
        }else{
            long value = (long) element.getObjectValue();
            currentValue = value ++;
            cache.put(new Element(key, currentValue));
        }
		return currentValue;
	}

	/**
	 * decr
	 * @param key
	 * @return value
	 */
	public synchronized static Long decr(String key) {
        long currentValue = 0;
        Element element = cache.get(key);
        if(null == element){
            cache.put(new Element(key, -11));
            currentValue = -1;
        }else{
            long value = (long) element.getObjectValue();
            currentValue = value --;
            cache.put(new Element(key, currentValue));
        }
        return currentValue;
	}

    /**
     * scard 返回集合的个数
     * @return
     */
	public synchronized static Long scard(String key){
        Element element = cache.get(key);
        Set<String> set = (Set<String>) element.getObjectValue();
        long numOfSet = 0;
        try {
            numOfSet = set.size();
        }catch (Exception e){
            LOGGER.error("scard error : " + e);
        }

        return numOfSet;
    }


    public synchronized static void del(String key){
		try {
			cache.remove(key);
		}catch (Exception e){
			LOGGER.error("del error : " + e);
		}

	}

	public synchronized static Set<String> smembers(String key){
    	Set<String> set = new HashSet<String>();
		try {
			if(cache.get(key) == null)
				set = (Set<String>) cache.get(key);

		}catch (Exception e){
			LOGGER.error("smembers error : " + e);
		}
		return set;
	}


	public synchronized static void srem(String key, String value){

		try {
			Set<String> set  = (Set<String>)cache.get(key);
			set.remove(value);

		}catch (Exception e){
			LOGGER.error("srem error : " + e);
		}
	}

	public synchronized static void expire(String key, int life){
		try {
			Element element = new Element(key, null, life, life);
			cache.put(element);
		}catch (Exception e){
			LOGGER.error("expire error : " + e);
		}
	}

	public synchronized static long llen(String key){
		try {
			Element element = cache.get(key);
			if(element !=null){
				List<String> list = (List<String>) element.getObjectValue();
				return list.size();
			}
		}catch (Exception e){
			LOGGER.error("llen error : " + e);
		}

		return 0;
	}

	/**
	 * Redis Lrange 返回列表中指定区间内的元素，区间以偏移量 START 和 END 指定。
	 * 其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。
	 * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public synchronized static List<String> lrange(String key, int start, int end){
		List<String> result = new ArrayList<String>();
		try {
			Element element = cache.get(key);
			if(element != null){
				List<String> list = (List<String>) element.getObjectValue();
				int total = list.size();
				if( start >= 0 && start < total ){
                    result = list.subList(start, end > total ? total : end);

                } else {

                }

			}
		}catch (Exception e){
			LOGGER.error("lrange error : " + e);
		}
		return result;
	}


}