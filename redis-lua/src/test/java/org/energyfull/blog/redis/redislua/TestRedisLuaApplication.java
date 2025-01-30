package org.energyfull.blog.redis.redislua;

import org.springframework.boot.SpringApplication;

public class TestRedisLuaApplication {

    public static void main(String[] args) {
        SpringApplication.from(RedisLuaApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
