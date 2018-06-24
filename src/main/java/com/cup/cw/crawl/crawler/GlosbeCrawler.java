package com.cup.cw.crawl.crawler;

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.Config;
import com.cup.cw.configration.CrawlConfig;
import com.cup.cw.constant.CrawlConstants;
import com.cup.cw.util.FileUtils;
import com.cup.cw.util.RegexUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 爬取https://ja.glosbe.com/en/ja/，英语翻译日语
 */
@Component
public class GlosbeCrawler extends BreadthCrawler {

    private static CrawlConfig config;
    private String regex1 = "https://ja.glosbe.com/en/ja/(.*)";
    private String rootUrl = "https://ja.glosbe.com/en/ja/";

    static {
        String configFile = CrawlConstants.GLOSBE_CRAWL_CONFIG_FILE;
        try {
            config = new CrawlConfig(configFile);
            System.out.println("配置参数：" + config.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GlosbeCrawler() {
        super(config.getResultpath() + "database", config.isAutoparse());
    }

    public void visit(Page page, CrawlDatums crawlDatums) {
        if (page.matchUrl(regex1)) {
            String filepath = config.getResultpath() + "/html/" +
                    RegexUtils.geturlpath(page.url(), config.getUrlsave());
            FileUtils.writeString(page.html(), filepath, config.isDeletefile());
        }
    }

    public List<String> makeUrls(){
        String filePath = "./data/word";
        List<String > urlList = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while((line = reader.readLine()) != null){
                String keyWord = line.split(";")[0];
                urlList.add(rootUrl + keyWord);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlList;
    }

    /**
     * 启动方法
     *
     * @return
     */
    public boolean start() {
        this.setResumable(config.isResumable());
        for (String seed : makeUrls()) {
            this.addSeed(seed, "main");
        }
        for (String regex : config.getRegexlist()) {
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