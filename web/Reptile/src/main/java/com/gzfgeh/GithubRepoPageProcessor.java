package com.gzfgeh;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class GithubRepoPageProcessor implements PageProcessor {

	private Site site = Site.me().setRetryTimes(3).setSleepTime(100);

	public void process(Page page) {
//		page.addTargetRequests(page.getHtml().links().regex("(https://www.jandan\\.net/\\w+/\\w+)").all());
//		page.putField("author", page.getUrl().regex("https://www.jandan\\.net/(\\w+)/.*").toString());
//		page.putField("name", page.getHtml().xpath("//ol[@class='commentlist']/li//a[@class='view_img_link']/@href").get());
//		if (page.getResultItems().get("name")==null){
//			//skip this page
//			page.setSkip(true);
//		}
//		page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));


		String url = page.getHtml().xpath("//ol[@class='commentlist']/li//a[@class='view_img_link']/@href").get();
		System.out.printf("url: "+url);
	}

	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new GithubRepoPageProcessor())
				.addUrl("http://jandan.net/ooxx/page-0#comments")
				.thread(5)
				.run();
	}

}
