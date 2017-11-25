package com.denis.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "catalog_instrument")
public class CatalogInstrument implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String url;
	private String title;
	private String codeItem;
	private double price;

	public CatalogInstrument() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
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
		return "CatalogInstrument [" + "${id}" + id + "${id}, " + "${url}" + url + "${url}, " + "${title}" + title
				+ "${title}, " + "${codeItem}" + codeItem + "${codeItem}, " + "${price}" + price + "${price}]";
	}

}
