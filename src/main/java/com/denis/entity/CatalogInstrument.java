package com.denis.entity;

public class CatalogInstrument {

	private String url;
	private String title;
	private String codeItem;
	private double price;

	public CatalogInstrument(String url, String title, String codeItem, double price) {
		super();
		this.url = url;
		this.title = title;
		this.codeItem = codeItem;
		this.price = price;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCodeItem() {
		return codeItem;
	}

	public void setCodeItem(String codeItem) {
		this.codeItem = codeItem;
	}

	@Override
	public String toString() {
		return "CatalogInstrument [" + "${url}" + url + "${url}, " + "${title}" + title + "${title}, " + "${codeItem}" + codeItem + "${codeItem}, " + "${price}" + price
				+ "${price}]";
	}

}
