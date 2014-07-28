package io.xixi.sears;

/**
 * A search result item in Sears' search result page.
 *
 * @author xixi.
 *         Created Jul 27, 2014.
 */
public class SearsResultItem {
	private String title;
	private String price;  // not using float because prices can be like "$100-200"
	private String vendor;
	
	/**
	 * Plain constructor.
	 *
	 * @param title
	 * @param price
	 * @param vendor
	 */
	public SearsResultItem(String title, String price, String vendor) {
		this.title = title;
		this.price = price;
		this.vendor = vendor;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Title: " + (this.title == null ? "NA" : this.title) + System.lineSeparator());
		sb.append("Price: " + (this.price == null ? "NA" : this.price) + System.lineSeparator());
		sb.append("Vendor: " + (this.vendor == null ? "NA" : this.vendor) + System.lineSeparator());
		return sb.toString();
	}
}
