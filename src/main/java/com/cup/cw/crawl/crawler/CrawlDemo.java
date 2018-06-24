package com.cup.cw.crawl.crawler;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.Config;
import com.cup.cw.configration.CrawlConfig;
import com.cup.cw.constant.CrawlConstants;
import com.cup.cw.util.FileUtils;
import com.cup.cw.util.RegexUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * BreadthCrawler demo
 */
@Component
public class CrawlDemo extends BreadthCrawler {
    private static  CrawlConfig config;
    private String regex1 = "http://sports.sina.com.cn/nba/(.*)";
    private String regex2 = "http://sports.sina.com.cn/(.*)/nba/2018-06-24/(.*)";

    static{
        String configFile = CrawlConstants.CRAWL_DEMO_CONFIG_FILE;
        try {
            config = new CrawlConfig(configFile);
            System.out.println("配置参数：" + config.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void visit(Page page, CrawlDatums crawlDatums) {
        if (page == null){
            return;
        }
        Elements nextUrls = page.doc().getElementsByAttribute("href");
        if (page.matchType("sport")){
            for (Element ele : nextUrls){
                String url = ele.absUrl("href");

                if (RegexUtils.match(regex1,url).find()){
                    CrawlDatum datum = new CrawlDatum(url).type("nba");
                    crawlDatums.add(datum);
                }
            }
        }else if (page.matchType("nba")){
            System.out.println("进入NBA首页");
            for (Element ele : nextUrls){
                String url = ele.absUrl("href");
                if (RegexUtils.match(regex2,url).find()){
                System.out.println("url:" + url);
                    CrawlDatum datum = new CrawlDatum(url).type("content");
                    crawlDatums.add(datum);
                }
            }
        }else if(page.matchType("content")){
            System.out.println("进入content");
            String content = page.html();
            String filepath = config.getResultpath() + "/html/" +
                    RegexUtils.geturlpath(page.url(), config.getUrlsave());
            FileUtils.writeString(content,filepath , config.isDeletefile());
            System.out.println("输出content");
        }

        long sleeptime;
        if ( (sleeptime = config.getSleeptime()) > 0){
            try{
                Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public CrawlDemo() throws IOException{
        super(config.getResultpath() + "database", config.isAutoparse());
    }

    /**
     * 启动方法
     * @return
     */
    public boolean start(){
        this.setResumable(config.isResumable());
        for (String seed : config.getSeedlist()){
            this.addSeed(seed,"sport");
        }

        for (String regex : config.getRegexlist()){
            this.addRegex(regex);
        }

        this.setThreads(config.getThreadnum());

        Config.MAX_REDIRECT = config.getMaxredirect();

        try {
            this.start(config.getMaxdepth());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
