package org.energyfull.blog.redis.redislua;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ApplicationCounterRedisOperation implements CounterRedisOperation{
    private final StringRedisTemplate redisTemplate;
    private static final String KEY = "counter";

    @Override
    public void increment() {
        // 1. redis 값 조회
        String value = redisTemplate.opsForValue().get(KEY);
        int intValue = (value != null) ? Integer.parseInt(value) : 0;

        // 2. 값 증가
        intValue++;

        // 3. 값 저장
        redisTemplate.opsForValue().set(KEY, String.valueOf(intValue));
    }
}
