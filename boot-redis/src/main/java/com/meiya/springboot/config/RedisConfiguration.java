package com.meiya.springboot.config;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


/**
 * Redis配置
 *
 * @author linwb
 * @since 2019/12/20
 **/
@Configuration
public class RedisConfiguration {
    private static final String SEMICOLON = ";";
    private static final String COLON = ":";
    /**
     * redis ip与端口号，多个直接用分号隔开(;)
     */
    @Value("${redis.server-nodes:}")
    public String serverNodes;
    /**
     * redis认证密码
     */
    @Value("${redis.password:}")
    public String password;

    @Value("${redis.database:0}")
    private Integer database;
    /**
     * redis服务端是否集群
     */
    @Value("${redis.is-cluster:true}")
    public boolean isCluster;
    /**
     * 哨兵模式的主服务名
     */
    @Value("${redis.master:masterNodeName}")
    public String master;
    /**
     * 超时时间
     */
    @Value("${redis.timeout:30000}")
    public long redisTimeout;
    /**
     * 最大连接数
     */
    @Value("${redis.jedis-pool.max-total:5}")
    public int redisPoolMaxTotal;
    /**
     * 最大空闲数
     */
    @Value("${redis.jedis-pool.max-idle:5}")
    public int redisPoolMaxIdle;
    /**
     * 最小空闲数
     */
    @Value("${redis.jedis-pool.min-idle:5}")
    public int redisPoolMinIdle;
    /**
     * 获取连接的最大等待毫秒数，小于零:阻塞不确定的时间，默认-1
     */
    @Value("${redis.jedis-pool.max-wait:100000}")
    public long redisPoolMaxWaitMillis;
    /**
     * 在获取连接的时候检查有效性，默认为false
     */
    @Value("${redis.jedis-pool.on-borrow:true}")
    public boolean redisPoolOnBorrow;
    /**
     * 在空闲时检查有效性，默认为false
     */
    @Value("${redis.jedis-pool.test-while-idle:true}")
    public boolean redisPoolTestWhileIdle;
    /**
     * 每次释放连接的最大数目
     */
    @Value("${redis.jedis-pool.test-pre-eviction-run:1024}")
    public int redisPoolNumTestsPerEvictionRun;
    /**
     * 释放连接的扫描间隔（毫秒）
     */
    @Value("${redis.jedis-pool.time-between-eviction-runs:30000}")
    public long redisPoolTimeBetweenEvictionRunsMillis;
    /**
     * 连接最小空闲时间
     */
    @Value("${redis.jedis-pool.min-evictable-idle-time:3000000}")
    public long redisPoolMinEvictableIdleTimeMillis;
    /**
     * 连接空闲多久后释放, 当空闲时间>该值 且 空闲连接>最大空闲连接数 时直接释放
     */
    @Value("${redis.jedis-pool.soft-min-evictable-idle-time:1800000}")
    public long redisPoolSoftMinEvictableIdleTimeMillis;

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisClientConfiguration clientConfiguration = defaultJedisClientConfiguration();

        if (!serverNodes.contains(SEMICOLON)) {
            return new JedisConnectionFactory(getStandaloneConfig(serverNodes, password, database), clientConfiguration);
        }

        List<String> serverNodeList = Lists.newArrayList(StringUtils.split(serverNodes, SEMICOLON));
        if (isCluster) {
            return new JedisConnectionFactory(getClusterConfiguration(serverNodeList, password), clientConfiguration);
        }

        return new JedisConnectionFactory(getSentinelConfig(serverNodeList, password, master, database), clientConfiguration);
    }

    private JedisClientConfiguration defaultJedisClientConfiguration() {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisPoolMaxTotal);
        config.setMaxIdle(redisPoolMaxIdle);
        config.setMinIdle(redisPoolMinIdle);
        config.setMaxWaitMillis(redisPoolMaxWaitMillis);
        config.setTestOnBorrow(redisPoolOnBorrow);
        config.setTestWhileIdle(redisPoolTestWhileIdle);
        config.setNumTestsPerEvictionRun(redisPoolNumTestsPerEvictionRun);
        config.setTimeBetweenEvictionRunsMillis(redisPoolTimeBetweenEvictionRunsMillis);
        config.setMinEvictableIdleTimeMillis(redisPoolMinEvictableIdleTimeMillis);
        config.setSoftMinEvictableIdleTimeMillis(redisPoolSoftMinEvictableIdleTimeMillis);

        builder.readTimeout(Duration.ofMillis(redisTimeout))
                .connectTimeout(Duration.ofMillis(redisTimeout))
                .usePooling()
                .poolConfig(config);
        return builder.build();
    }

    private RedisStandaloneConfiguration getStandaloneConfig(String server, String password, int database) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();

        String[] hostAndPort = StringUtils.split(server, COLON);
        config.setHostName(hostAndPort[0]);
        config.setPort(Integer.valueOf(hostAndPort[1]));
        if (password != null) {
            config.setPassword(RedisPassword.of(password));
        }
        config.setDatabase(database);
        return config;
    }

    private RedisSentinelConfiguration getSentinelConfig(List<String> nodes, String password, String master, int database) {
        RedisSentinelConfiguration config = new RedisSentinelConfiguration();
        config.master(master);
        config.setSentinels(createSentinels(nodes));
        if (password != null) {
            config.setPassword(RedisPassword.of(password));
        }
        config.setDatabase(database);
        return config;
    }

    private RedisClusterConfiguration getClusterConfiguration(List<String> nodes, String password) {
        RedisClusterConfiguration config = new RedisClusterConfiguration(nodes);
        if (password != null) {
            config.setPassword(RedisPassword.of(password));
        }
        config.setMaxRedirects(20);
        return config;
    }

    private List<RedisNode> createSentinels(List<String> nodes) {
        List<RedisNode> redisNodes = new ArrayList<>();
        for (String node : nodes) {
            try {
                String[] parts = StringUtils.split(node, COLON);
                Assert.state(parts.length == 2, "redis节点格式为： 'host:port'");
                redisNodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
            } catch (RuntimeException ex) {
                throw new IllegalStateException(
                        "[哨兵模式]无效的redis属性节点'" + node + "'", ex);
            }
        }
        return redisNodes;
    }
}
