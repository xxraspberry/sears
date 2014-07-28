package io.xixi.sears;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * A simple parser for Sears.com search results.
 * 
 * @author xixi. Created Jul 26, 2014.
 */
public class SearsParser {

	private final static String URL_PREFIX = "http://www.sears.com/search=";
	private final static int FETCH_TIMEOUT_MILLIS = 20000;  // 20sec
	private final static int ITEMS_PER_PAGE = 50;  // seems to be Sears' default
	private Document doc = null;


	/**
	 * Fetch a search result page.
	 * 
	 * @param keyword
	 * @param pageNumber
	 * @throws IOException
	 * 
	 */
	void fetchResultPage(String keyword, int pageNumber) throws IOException {
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
		} catch (UnsupportedEncodingException exception) {
			// try using un-escaped keyword
		}
		String url = URL_PREFIX + keyword;

		if (pageNumber > 1) {
			url += "?pageNum=" + pageNumber + "&viewItems=" + ITEMS_PER_PAGE;
		}

		Connection request = Jsoup.connect(url);
		request.timeout(FETCH_TIMEOUT_MILLIS);
		this.doc = request.get();
	}

	
	/**
	 * Parse the page to get the total number of result.
	 *
	 * @return the total number as a String (as it appears in the page)
	 * @throws Exception 
	 */
	public String getTotalResultCount() throws Exception {
		if (this.doc == null) {
			throw new Exception("Result page is not ready.");
		}
		if (this.doc.select("#noResults") != null) {
			return "0";
		}
		Element total = this.doc.select(".tab-filters-count").first();
		if (total != null) {
			return total.text().replace("(", "").replace(")", "");
		}
		throw new Exception("[Failed to parse result count; missing .tab-filters-count in DOM.]");
	}

	
	/**
	 * Parse the page to get all result items.
	 * @return null if no item found; a non-empty list otherwise
	 */
	public List<SearsResultItem> getResultItems() {
		ArrayList<SearsResultItem> items = new ArrayList<SearsResultItem>();
		Element cards = this.doc.select("#cardsHolder").first();
		if (cards == null) {
			return null;
		}
		Elements results = cards.select(".cardContainer");
		if (results == null) {
			return null;
		}

		for (Element e : results) {
			String title = e.select(".cardProdTitle").first().text();
			// price may be presented in two kinds of tags
			String price = null;
			Element priceNode = e.select(".gridPrice").first();
			if (priceNode != null) {
				price = priceNode.text();
			} else {
				priceNode = e.select(".price_v2").first();
				if (priceNode != null) {
					price = priceNode.text();
				}
			}
			String vendor = null;
			Elements vendorLines = e.select("#mrkplc p");
			for (Element line : vendorLines) {
				String text = line.text().trim();
				if (text.startsWith("Sold by ")) {
					vendor = text.replaceFirst("Sold by ", "");
				}
			}
			SearsResultItem item = new SearsResultItem(title, price, vendor);
			items.add(item);
		}
		return items;
	}

	/**
	 * Main entry point.
	 * @param args
	 */
	public static void main(String[] args) {

		if (args.length != 1 && args.length != 2) {
			System.out.println("Syntax for arguments:");
			System.out.println("  1. keyword   [Gets total result count]");
			System.out.println("  2. keyword K [List all results on page K]");
			return;
		}

		SearsParser parser = new SearsParser();
		String keyword = args[0];
		int pageNumber = 1;
		boolean showResults = false;
		if (args.length > 1) {
			try {
				pageNumber = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				System.err.println("Error: second argument must be a valid integer.");
				return;
			}
			if (pageNumber <= 0) {
				System.err.println("Page number must be a positive interger.");
				return;
			}
			showResults = true;
		}
		
		try {
			parser.fetchResultPage(keyword, pageNumber);
		} catch (IOException e) {
			System.err.println("Failed to fetch search result page: " + e.getMessage());
			return;
		}
		
		if (!showResults) {
			try {
				System.out.println("Total result count: " + parser.getTotalResultCount());
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		} else {
			List<SearsResultItem> results = parser.getResultItems();
			if (results == null) {
				System.out.println("Could not find any result on page " + pageNumber);
			} else {
				for (int i = 0; i < results.size(); i++) {
					System.out.println("*** Result #" + (i + 1));
					System.out.println(results.get(i));
				}
			}
		}
	}
}
