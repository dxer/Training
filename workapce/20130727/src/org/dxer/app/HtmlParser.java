package org.dxer.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * html处理
 * 
 * @author walker
 * 
 */
public class HtmlParser {

	private static String getUrl(Element element) {
		Elements elements = element.getElementsByClass("img");
		String url = null;
		for (Element e : elements) {
			Elements links = e.getElementsByTag("a");
			url = links.attr("href");
		}
		return url;
	}

	/**
	 * 从页面获得链接地址
	 * 
	 * @param pageContent
	 * @return
	 */
	public static ArrayList<String> getUrlFromHtml(String pageContent) {
		ArrayList<String> urlList = new ArrayList<String>();
		try {
			// Document doc = Jsoup.parse(pageContent);
			File input = new File(pageContent);
			Document doc = Jsoup.parse(input, "UTF-8");
			Elements elements = doc.getElementsByTag("tbody");
			String url = null;
			for (Element element : elements) {
				Elements els = element.getElementsByTag("tr");
				for (Element el : els) {
					url = getUrl(el);
					if (url != null) {
						urlList.add(url);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return urlList;
	}

	public static void getInfo(String pageContent) {
		File input = new File(pageContent);
		try {
			Document doc = Jsoup.parse(input, "UTF-8");

			Elements elements = doc.getElementsByClass("mb15");
			for (Element e : elements) {
				// System.out.println(getTitle(e));
				getTime(e);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		// return null;
	}

	private static String getPrice(Element e) {
		Elements es = e.getElementsByClass("mtit_con");
		return "";
	}

	public static String getTime(Element e) {
		Elements es = e.getElementsByClass("mtit_con");
		System.out.println(es);

		return "";
	}

	/**
	 * 获得title
	 * 
	 * @param e
	 * @return
	 */
	private static String getTitle(Element e) {
		Elements es = e.getElementsByClass("bigtitle");
		StringBuffer bigTitle = new StringBuffer();
		for (Element el : es) {
			// System.out.println(el);
			Elements links = el.getElementsByTag("h1");
			bigTitle.append(links.html() + "-");

			Elements ls = el.getElementsByClass("c_666");
			bigTitle.append(ls.html());
		}
		return bigTitle.toString();
	}

	public static void main(String[] args) {
		HtmlParser.getInfo("d:/xx.html");
	}
}
