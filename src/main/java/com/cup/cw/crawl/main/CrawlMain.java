package com.cup.cw.crawl.main;

import com.cup.cw.crawl.CrawlDemo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CrawlMain {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        CrawlDemo crawlDemo = context.getBean("crawlDemo",CrawlDemo.class);
        crawlDemo.start();
    }
}
