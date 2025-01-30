package org.energyfull.blog.redis.redislua;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class LuaCounterRedisOperation implements CounterRedisOperation{
    private final StringRedisTemplate redisTemplate;
    private static final String KEY = "counter";
    private static final String LUA_SCRIPT = """
        local current = redis.call('GET', KEYS[1])
        if not current then
            current = 0
        else
            current = tonumber(current)
        end
        current = current + 1
        redis.call('SET', KEYS[1], current)
        return current
    """;

    @Override
    public void increment() {
        redisTemplate.execute(new DefaultRedisScript<>(LUA_SCRIPT, Long.class), Collections.singletonList(KEY));
    }
}
