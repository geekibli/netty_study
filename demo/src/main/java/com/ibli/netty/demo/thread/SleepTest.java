package com.ibli.netty.demo.thread;

/**
 * @Author gaolei
 * @Date 2022/3/28 下午5:19
 * @Version 1.0
 */
public class SleepTest {

    public static void main(String[] args) {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getId());
        while (true){
            try {
                Thread.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("out");
        }
    }
}
