package com.cup.cw.crawl;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.Config;
import com.cup.cw.configration.CrawlConfig;
import com.cup.cw.constant.CrawlConstants;
import com.cup.cw.util.FileUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * BreadthCrawler demo
 */
@Component
public class CrawlDemo extends BreadthCrawler {
    private static  CrawlConfig config;
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
        if (page.matchType("main")){
            Elements nextUrls = page.doc().getElementsByAttribute("href");
            for (Element url : nextUrls){
                if(url.text().matches(config.getRegexlist().get(0))){
                    crawlDatums.add(url.text(),"content");
                }
            }
        }else if(page.matchType("content")){
            String title = page.doc().title();
            String content = page.doc().text();
            FileUtils.writeString(content,config.getResultpath() + File.separator + title ,
                    config.isDeletefile());
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
            this.addSeed(seed,"main");
        }

        for (String regex : config.getRegexlist()){
            this.addRegex(regex);
        }

        this.setThreads(config.getThreadnum());

        Config.MAX_REDIRECT = config.getMaxredirect();

        System.out.println("启动！");
        try {
            this.start(config.getMaxdepth());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
