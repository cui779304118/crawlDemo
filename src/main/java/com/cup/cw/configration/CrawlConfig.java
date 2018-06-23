package com.cup.cw.configration;

import com.alibaba.fastjson.JSONObject;
import com.cup.cw.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class CrawlConfig {
    private boolean autoparse = false;
    private boolean useproxy = false;
    private boolean resumable = false;
    private int maxredirect = 10;
    private int maxdepth = 1;

    private int threadnum = 1;
    private String resultpath = "";
    private boolean deletefile = false;
    private boolean deletepath = false;
    private String urlsave = "";

    private int sleeptime = -1;

    private String requestmethod = "";

    private List<String> seedlist = new ArrayList<String>();

    private List<String> regexlist = new ArrayList<String>();

    public CrawlConfig(String configpath) throws IOException {
        Properties props = new Properties();
        String relativelyPath = System.getProperty("user.dir");
        InputStream in = new FileInputStream(new File(relativelyPath + configpath));
        props.load(in);
        in.close();

        Set keys = props.keySet();
        for (Iterator it = keys.iterator(); it.hasNext();){
            String k = it.next().toString();
            if (k.equals("autoparse")) {
                autoparse = Boolean.valueOf(props.getProperty(k));
            } else if (k.equals("useproxy")) {
                useproxy = Boolean.valueOf(props.getProperty(k));
            } else if (k.equals("resumable")) {
                resumable = Boolean.valueOf(props.getProperty(k));
            } else if (k.equals("maxredirect")) {
                maxredirect = Integer.valueOf(props.getProperty(k));
            } else if (k.equals("maxdepth")) {
                maxdepth = Integer.valueOf(props.getProperty(k));
            } else if (k.equals("deletefile")) {
                deletefile = Boolean.valueOf(props.getProperty(k));
            } else if (k.equals("deletefolder")) {
                deletepath = Boolean.valueOf(props.getProperty(k));
            } else if (k.equals("urlsave")) {
                urlsave = props.getProperty(k);
            } else if (k.equals("seedlist")) {
                String[] seeds = props.getProperty(k).split(",");
                for (int i = 0; i < seeds.length; i++) {
                    if (seeds[i].trim().length() == 0) {
                        continue;
                    }
                    seedlist.add(seeds[i].trim());
                }
            } else if (k.equals("regexlist")) {
                String[] regexs = props.getProperty(k).split(",");
                for (int i = 0; i < regexs.length; i++) {
                    if (regexs[i].trim().length() == 0) {
                        continue;
                    }
                    regexlist.add(regexs[i].trim());
                }
            } else if (k.equals("resultpath")) {
                resultpath = relativelyPath + props.getProperty(k);
            } else if (k.equals("threadnum")) {
                threadnum = Integer.valueOf(props.getProperty(k));
            } else if (k.equals("seedfile")) {
                List<String> seeds = FileUtils.readStrFile(props.getProperty(k));
                seedlist.addAll(seeds);
            } else if (k.equals("requestmethod")) {
                requestmethod = props.getProperty(k);
            } else if (k.equals("sleeptime")) {
                sleeptime = Integer.valueOf(props.getProperty(k));
            }
        }
    }

    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("autoparse", autoparse);
        jsonObject.put("useproxy", useproxy);
        jsonObject.put("resumable", resumable);
        jsonObject.put("maxredirect", maxredirect);
        jsonObject.put("maxdepth", maxdepth);
        jsonObject.put("threadnum", threadnum);
        jsonObject.put("resultpath", resultpath);
        jsonObject.put("deletefile", deletefile);
        jsonObject.put("deletepath", deletepath);
        jsonObject.put("urlsave", urlsave);
        jsonObject.put("requestmethod", requestmethod);
        jsonObject.put("sleeptime", sleeptime);
        return jsonObject.toJSONString();
    }

    public boolean isAutoparse() {
        return autoparse;
    }

    public boolean isUseproxy() {
        return useproxy;
    }

    public boolean isResumable() {
        return resumable;
    }

    public int getMaxredirect() {
        return maxredirect;
    }

    public int getMaxdepth() {
        return maxdepth;
    }

    public String getResultpath() {
        return resultpath;
    }

    public boolean isDeletefile() {
        return deletefile;
    }

    public String getUrlsave() {
        return urlsave;
    }

    public List<String> getSeedlist() {
        return seedlist;
    }

    public List<String> getRegexlist() {
        return regexlist;
    }

    public int getThreadnum() {
        return threadnum;
    }

    public String getRequestmethod() {
        return requestmethod;
    }

    public int getSleeptime() {
        return sleeptime;
    }
}
