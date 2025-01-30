

package org.energyfull.blog.redis.redislua;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;


@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class RedisConcurrencyTest {
    @Autowired
    private LuaCounterRedisOperation luaCounterRedisOperation;

    @Autowired
    private ApplicationCounterRedisOperation applicationCounterRedisOperation;
    
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void testApplication() throws InterruptedException {
        final int threadCount = 100;
        final int iterationCount = 100;
        int result = testConcurrentIncrement(applicationCounterRedisOperation, 100, 100);
        assertThat(result).isNotEqualTo(threadCount * iterationCount);
    }

    @Test
    void testLua() throws InterruptedException {
        final int threadCount = 100;
        final int iterationCount = 100;
        int result = testConcurrentIncrement(luaCounterRedisOperation, 100, 100);
        assertThat(result).isEqualTo(threadCount * iterationCount);
    }


    private int testConcurrentIncrement(CounterRedisOperation counterRedisOperation, int threadCount, int iterationCount) throws InterruptedException {
        redisTemplate.opsForValue().set("counter", "0"); // 초기값 설정
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < iterationCount; j++) {
                    counterRedisOperation.increment();
                }
            });
        }
        
        executor.shutdown();
        while (!executor.isTerminated()) {
            Thread.sleep(100);
        }
        
        String finalValue = redisTemplate.opsForValue().get("counter");
        System.out.println("최종 결과값: " + finalValue);
        return Integer.parseInt(finalValue);
    }
}