package com.denis.service;

import java.util.Map;

import com.denis.dto.CatalogInstrument;
import com.denis.dto.Instrument;

public class EntityCreator {

	public static CatalogInstrument createCatalogInstrument(String url, String title, String codeItem, double price) {
		CatalogInstrument result = new CatalogInstrument();

		result.setUrl(url);
		result.setTitle(title);
		result.setCodeItem(codeItem);
		result.setPrice(price);

		return result;
	}

	public static Instrument createInstrument(Map<String, String> map) {
		Instrument result = new Instrument();

		String img = "";
		String brand = "";
		String vendorCode = "";
		String madeIn = "";
		String importer = "";
		String garante = "";
		String weight = "";
		String description = "";
		int id = 0;

		for (Map.Entry<String, String> entity : map.entrySet()) {

			String key = entity.getKey();
			String value = entity.getValue();

			switch (key) {
			case "img":
				img = value;
				break;
			case "brand":
				brand = value;
				break;
			case "vendorCode":
				vendorCode = value;
				break;
			case "madeIn":
				madeIn = value;
				break;
			case "importer":
				importer = value;
				break;
			case "garante":
				garante = value;
				break;
			case "weight":
				weight = value;
				break;
			case "description":
				description = value;
				break;
			case "id":
				id = Integer.valueOf(value);
				break;
			}

		}

		result.setImg(img);
		result.setBrand(brand);
		result.setVendorCode(vendorCode);
		result.setMadeIn(madeIn);
		result.setImporter(importer);
		result.setGarante(garante);
		result.setWeight(weight);
		result.setDescription(description);
		result.setCatalogId(id);

		return result;
	}

}
