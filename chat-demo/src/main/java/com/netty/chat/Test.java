package com.netty.chat;

import java.util.Scanner;

/**
 * @Author gaolei
 * @Date 2022/4/18 下午8:51
 * @Version 1.0
 */
public class Test {

    public static void main(String[] args) {

            Scanner scanner = new Scanner(System.in);

            boolean b = scanner.hasNextLine();
            while (b) {
                System.out.println(scanner.nextLine());

            }

    }
}
