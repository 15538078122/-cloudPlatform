package com.hd.gateway;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Author: liwei
 * @Description:
 */
public class ReactTests {

    @Test
    public void test() {
        Flux.just("Hello", "World").subscribe(System.out::println);
        Mono.just("HelloWorld").subscribe(System.out::println);
        Mono.create(sink -> sink.success("Hello")).subscribe(System.out::println);
        Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
    }



    @Before
    public void testBefore(){
        System.out.println("before");
    }

    @After
    public void testAfter(){
        System.out.println("after");
    }

}
