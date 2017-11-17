package com.denis.entity;

public class Instrument {

	String img;

	String brand;
	String vendorCode;
	String madeIn;
	String importer;
	String garante;
	String weight;

	String description;

	public Instrument(String img, String brand, String vendorCode, String madeIn, String importer, String garante, String weight, String description) {
		super();
		this.img = img;
		this.brand = brand;
		this.vendorCode = vendorCode;
		this.madeIn = madeIn;
		this.importer = importer;
		this.garante = garante;
		this.weight = weight;
		this.description = description;
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
		return "Instrument [img=" + img + ", brand=" + brand + ", vendorCode=" + vendorCode + ", madeIn=" + madeIn + ", importer=" + importer + ", garante=" + garante + ", weight="
				+ weight + ", description=" + description + "]";
	}

}
