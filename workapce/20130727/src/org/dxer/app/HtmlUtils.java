package org.dxer.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlUtils {

	private static Document getDocument(String pageContent) {
		File input = new File(pageContent);
		Document doc = null;
		try {
			// doc = Jsoup.parse(input, "UTF-8");
			doc = Jsoup.parse(pageContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public static ArrayList<String> getAllUrlsFromPage(String pagecString) {
		Document doc = getDocument(pagecString);
		Elements elements = doc.select("td.img a");
		ArrayList<String> urlList = new ArrayList<String>();
		for (Element e : elements) {
			urlList.add(e.attr("href"));
		}
		return urlList;
	}

	public static HouseInfo getHouseInfo(String pageContent) throws IOException {
		HouseInfo houseInfo = new HouseInfo();
		Document doc = getDocument(pageContent);
		try {
			houseInfo.setBigTitle(getBigTitle(doc));
			houseInfo.setPrice(getPrice(doc));
			houseInfo.setPaymentType(getPaymentType(doc));
			houseInfo.setHouseType(getHouseType(doc));
			houseInfo.setFloor(getFloor(doc));
			houseInfo.setHouse(getHouse(doc));
			houseInfo.setArea(getArea(doc));
			houseInfo.setAddress(getAddress(doc));
			houseInfo.setContacts(getContact(doc));
			houseInfo.setDescription(getDesc(doc));
		} catch (Exception e) {
			System.err.println("------------------------");
		}
		return houseInfo;
	}

	private static String getBigTitle(Document doc) throws IOException {
		String titlePrefix = doc.select("div.bigtitle h1").text().trim();
		String titleSuffix = doc.select("div.bigtitle .c_666").text().trim();
		String title = titlePrefix + " " + titleSuffix;
		return cleanSpace(title).trim();
	}

	private static String getPrice(Document doc) {
		String price = doc.select("div.su_con .bigpri").text().trim();
		String unit = doc.select("div.su_con").first().text().trim().split(" ")[1];
		return cleanSpace(price + unit);
	}

	private static String getPaymentType(Document doc) {
		String type = doc.select("div.su_con .f12").first().text();
		return type.trim();
	}

	private static String getHouseType(Document doc) {
		String houseType = doc.select("div.su_con").get(1).text();
		return cleanSpace(houseType).trim();
	}

	private static String getFloor(Document doc) {
		String floor = doc.select("div.su_con").get(2).text();
		return cleanSpace(floor).trim();
	}

	private static String getHouse(Document doc) {
		String rent = doc.select("div.su_con").get(3).text();
		return cleanSpace(rent);
	}

	private static String getArea(Document doc) {
		String area = doc.select("div.su_con").get(4).text();
		return cleanSpace(area);
	}

	private static String getAddress(Document doc) {
		String address = doc.select("div.su_con").get(5).text().trim();
		return cleanSpace(address);
	}

	private static String getContact(Document doc) {
		String names = doc.select("ul.suUl li").get(6).text().trim().split("：")[1];
		return cleanSpace(names);
	}

	private static String getDesc(Document doc) {
		String desc = doc.select("article.description_con").text().trim();
		return cleanSpace(desc);
	}

	/**
	 * 去除空格
	 * 
	 * @param str
	 * @return
	 */
	private static String cleanSpace(String str) {
		str = str.replaceAll("   ", " ");
		return str;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// System.out.println(HtmlUtils.getHouseInfo("d:/xx.html").toString());
		// ArrayList<String> list = HtmlUtils.getUrlFromHtml("d:/NewFile.html");
		//
		// for (String str : list) {
		// System.out.println(str);
		// }

		// HtmlUtils.getUrl("d:/NewFile.html");

	}
}
