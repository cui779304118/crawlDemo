package com.cup.cw.crawl;

import com.cup.cw.crawl.crawler.CrawlDemo;
import com.cup.cw.crawl.crawler.GlosbeCrawler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CrawlMain {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        GlosbeCrawler glosbeCrawler = context.getBean("glosbeCrawler",GlosbeCrawler.class);
        glosbeCrawler.start();
    }
}
