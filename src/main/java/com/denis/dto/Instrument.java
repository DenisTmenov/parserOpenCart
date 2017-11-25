package com.denis.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "instrument")
public class Instrument implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String img;

	private String brand;
	@Column(name = "vendor_code")
	private String vendorCode;
	@Column(name = "mad_in")
	private String madeIn;
	private String importer;
	private String garante;
	private String weight;

	private String description;

	@Column(name = "fk_catalog_instrument_id")
	private int catalogId;

	public Instrument() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public String getMadeIn() {
		return madeIn;
	}

	public void setMadeIn(String madeIn) {
		this.madeIn = madeIn;
	}

	public String getImporter() {
		return importer;
	}

	public void setImporter(String importer) {
		this.importer = importer;
	}

	public String getGarante() {
		return garante;
	}

	public void setGarante(String garante) {
		this.garante = garante;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Instrument [${id}" + id + "${id}, ${img}" + img + "${img}, ${brand}" + brand + "${brand}, ${vendorCode}"
				+ vendorCode + "${vendorCode}, ${madeIn}" + madeIn + "${madeIn}, ${importer}" + importer
				+ "${importer}, ${garante}" + garante + "${garante}, ${weight}" + weight + "${weight}, ${description}"
				+ description + "${description}" + "${catalogId}" + catalogId + "${catalogId}]";
	}

}
