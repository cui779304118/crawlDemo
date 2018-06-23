package com.cup.cw.crawl;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.Config;
import com.cup.cw.configration.CrawlConfig;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * BreadthCrawler demo
 */
public class CrawlDemo extends BreadthCrawler {
    CrawlConfig config;
    public void visit(Page page, CrawlDatums crawlDatums) {
        if (page == null){
            return;
        }

        Elements nextUrls = page.doc().getElementsByAttribute("href");

        long sleeptime;
        if ( (sleeptime = config.getSleeptime()) > 0){
            try{
                Thread.sleep(sleeptime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public CrawlDemo(@Autowired CrawlConfig config) throws IOException{
        super(config.getResultpath() + "database", config.isAutoparse());
        this.config = config;
    }

    /**
     * 启动方法
     * @return
     */
    public boolean start(){
        this.setResumable(config.isResumable());
        for (String seed : config.getSeedlist()){
            this.addSeed(seed);
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
