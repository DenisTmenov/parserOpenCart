package com.denis.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.denis.dao.CatalogInstrumentDAO;
import com.denis.dao.InstrumentDAO;
import com.denis.dto.CatalogInstrument;
import com.denis.dto.Instrument;

public class ParserFile {

	private String original;
	private List<String> allLines;
	private List<String> modifyLines = new ArrayList<>();

	private List<Instrument> instrumentList;
	private List<CatalogInstrument> catalogInstrumentList;

	List<String> resultQueryOcProduct = new ArrayList<>();
	List<String> resultQueryOcProductDescription = new ArrayList<>();
	List<String> resultQueryOcProductImage = new ArrayList<>();
	List<String> resultQueryOcProductReward = new ArrayList<>();
	List<String> resultQueryOcProductToCategory = new ArrayList<>();
	List<String> resultQueryOcProductToStore = new ArrayList<>();
	List<String> resultQueryOcUrlAlias = new ArrayList<>();

	public ParserFile(String original, AnnotationConfigApplicationContext context) {
		super();
		this.original = original;
		instrumentList = ((InstrumentDAO) context.getBean("instrumentDAO")).loadAllInstrument();
		catalogInstrumentList = ((CatalogInstrumentDAO) context.getBean("catalogInstrumentDAO")).loadAllCategories();
	}

	public void start() {

		readFromeFile();

		preparationRequests();

		saveToFile();

	}

	private void readFromeFile() {
		try {
			allLines = Files.readAllLines(Paths.get(original));
		} catch (IOException e) {
			new ExceptionParser("No read ORIGINAL file", e);
		}

	}

	private void preparationRequests() {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat hours = new SimpleDateFormat("HH:mm:ss");
		SimpleDateFormat date = new SimpleDateFormat("YYYY-MM-dd");

		Map<String, Integer> mapNumbers = getNumbers();

		int productId = mapNumbers.get("lastProductId") + 1;
		int imageId = mapNumbers.get("lastImageId") + 1;
		int productRewardId = mapNumbers.get("lastProductRewardId") + 1;
		int urlAliasId = mapNumbers.get("lastUrlAliasId") + 1;

		for (CatalogInstrument catalog : catalogInstrumentList) {
			for (Instrument instrument : instrumentList) {
				if (catalog.getId() == instrument.getCatalogId()) {

					String queryOcProduct = getQueryOcProduct(catalog, instrument, productId, cal, hours, date);
					String queryOcProductDescription = getQueryOcProductDescription(catalog, instrument, productId);
					String queryOcProductImage = getQueryOcProductImage(instrument, productId, imageId);
					String queryOcProductReward = getQueryOcProductReward(productId, productRewardId);
					String queryOcProductToCategory = getQueryOcProductToCategory(catalog, productId);
					String queryOcProductToStore = getQueryOcProductToStore(productId);
					String queryOcUrlAlias = getQueryOcUrlAlias(catalog, productId, urlAliasId);

					resultQueryOcProduct.add(queryOcProduct);
					resultQueryOcProductDescription.add(queryOcProductDescription);
					resultQueryOcProductImage.add(queryOcProductImage);
					resultQueryOcProductReward.add(queryOcProductReward);
					resultQueryOcProductToCategory.add(queryOcProductToCategory);
					resultQueryOcProductToStore.add(queryOcProductToStore);
					resultQueryOcUrlAlias.add(queryOcUrlAlias);

					productId++;
					imageId++;
					productRewardId++;
					urlAliasId++;
				}

			}
		}

		String bufferLine = "";

		for (String line : allLines) {

			if (line.contains("INSERT INTO `oc_product`")) {
				bufferLine = line;
			} else if (bufferLine.contains("INSERT INTO `oc_product`")) {
				for (String newLine : resultQueryOcProduct) {
					modifyLines.add(newLine);
				}
				bufferLine = "";
			}

			if (line.contains("INSERT INTO `oc_product_description`")) {
				bufferLine = line;

			} else if (bufferLine.contains("INSERT INTO `oc_product_description`")) {
				for (String newLine : resultQueryOcProductDescription) {
					modifyLines.add(newLine);
				}
				bufferLine = "";
			}

			if (line.contains("INSERT INTO `oc_product_image`")) {
				bufferLine = line;

			} else if (bufferLine.contains("INSERT INTO `oc_product_image`")) {
				for (String newLine : resultQueryOcProductImage) {
					modifyLines.add(newLine);
				}
				bufferLine = "";
			}

			if (line.contains("INSERT INTO `oc_product_reward`")) {
				bufferLine = line;

			} else if (bufferLine.contains("INSERT INTO `oc_product_reward`")) {
				for (String newLine : resultQueryOcProductReward) {
					modifyLines.add(newLine);
				}
				bufferLine = "";
			}

			if (line.contains("INSERT INTO `oc_product_to_category`")) {
				bufferLine = line;

			} else if (bufferLine.contains("INSERT INTO `oc_product_to_category`")) {
				for (String newLine : resultQueryOcProductToCategory) {
					modifyLines.add(newLine);
				}
				bufferLine = "";
			}

			if (line.contains("INSERT INTO `oc_product_to_store`")) {
				bufferLine = line;

			} else if (bufferLine.contains("INSERT INTO `oc_product_to_store`")) {
				for (String newLine : resultQueryOcProductToStore) {
					modifyLines.add(newLine);
				}
				bufferLine = "";
			}

			if (line.contains("INSERT INTO `oc_url_alias`")) {
				bufferLine = line;

			} else if (bufferLine.contains("INSERT INTO `oc_url_alias`")) {
				for (String newLine : resultQueryOcUrlAlias) {
					modifyLines.add(newLine);
				}
				bufferLine = "";
			}

			modifyLines.add(line);
		}

	}

	private Map<String, Integer> getNumbers() {
		Map<String, Integer> result = new HashMap<>();

		List<Integer> productId = new ArrayList<>();
		List<Integer> imageId = new ArrayList<>();
		List<Integer> productRewardId = new ArrayList<>();
		List<Integer> urlAliasId = new ArrayList<>();

		for (String line : allLines) {

			if (line.contains(") VALUES (")) {

				if (line.contains("INSERT INTO `oc_product_to_category`")) {

					productId.add(getMemberDividedLine(line, 0));

				}

				if (line.contains("INSERT INTO `oc_product_image`")) {

					imageId.add(getMemberDividedLine(line, 0));

				}

				if (line.contains("INSERT INTO `oc_product_reward`")) {

					productRewardId.add(getMemberDividedLine(line, 0));

				}

				if (line.contains("INSERT INTO `oc_url_alias`")) {

					urlAliasId.add(getMemberDividedLine(line, 0));

				}
			}
		}

		Collections.sort(productId, Collections.reverseOrder());
		Collections.sort(imageId, Collections.reverseOrder());
		Collections.sort(productRewardId, Collections.reverseOrder());
		Collections.sort(urlAliasId, Collections.reverseOrder());

		result.put("lastProductId", productId.get(0));
		result.put("lastImageId", imageId.get(0));
		result.put("lastProductRewardId", productRewardId.get(0));
		result.put("lastUrlAliasId", urlAliasId.get(0));

		return result;
	}

	private Integer getMemberDividedLine(String line, int numberMember) {
		Integer result = 0;
		String[] split = line.split("\\) VALUES \\(");
		String[] splitRight = split[1].split(", ");
		result = Integer.valueOf(splitRight[numberMember].replaceAll("'", ""));
		return result;
	}

	private String getQueryOcProduct(CatalogInstrument catalog, Instrument instrument, int product_id, Calendar cal,
			SimpleDateFormat hours, SimpleDateFormat date) {

		String result = "";

		String[] masImage = instrument.getImg().split("/");

		String image = "data/Instrument/"
				+ masImage[masImage.length - 2].replace(masImage[masImage.length - 2].substring(0, 1),
						masImage[masImage.length - 2].substring(0, 1).toUpperCase())
				+ "/" + masImage[masImage.length - 1];
		double price = catalog.getPrice();
		String date_available = date.format(cal.getTime());
		String date_added = date.format(cal.getTime()) + " " + hours.format(cal.getTime());
		String date_modified = date.format(cal.getTime()) + " " + hours.format(cal.getTime());
		String model = "";

		Pattern pENG = Pattern.compile("( [a-zA-Z0-9\\.,\\- ]+)");
		Matcher matcher = pENG.matcher(catalog.getTitle().split("/")[0]);

		if (matcher.find()) {
			model = matcher.group().trim().replace("(", "");
		}

		String manufacturer_id = "";
		String brand = instrument.getBrand();

		switch (brand) {
		case "Bosch":
			manufacturer_id = "29";
			break;
		case "Collomix":
			manufacturer_id = "30";
			break;
		case "Makita":
			manufacturer_id = "31";
			break;
		case "Stromix":
			manufacturer_id = "32";
			break;
		}

		String status = "0";
		if (price > 0) {
			status = "1";
		}

		result = "INSERT INTO `oc_product` (`product_id`, `model`, `sku`, `upc`, `ean`, `jan`, `isbn`, `mpn`, `location`, `quantity`, `stock_status_id`, `image`, `manufacturer_id`, `shipping`, `price`, `points`, `tax_class_id`, `date_available`, `weight`, `weight_class_id`, `length`, `width`, `height`, `length_class_id`, `subtract`, `minimum`, `sort_order`, `status`, `date_added`, `date_modified`, `viewed`) VALUES ("
				+ "'" + product_id + "', '" + model + "', '', '', '', '', '', '', '', '100', '5', '" + image + "', '"
				+ manufacturer_id + "', '1', '" + price + "', '0', '0', '" + date_available
				+ "', '0.00000000', '1', '0.00000000', '0.00000000', '0.00000000', '1', '1', '1', '1', '" + status
				+ "', '" + date_added + "', '" + date_modified + "', '169');";

		return result;
	}

	private String getQueryOcProductDescription(CatalogInstrument catalog, Instrument instrument, int product_id) {
		String result = "";

		String queryUSA = "";
		String queryRUS = "";

		String name = catalog.getTitle();
		String description = instrument.getDescription();

		description = description.replace('\n', ' ');
		description = description.replaceAll("<p>", "");
		description = description.replaceAll("</p>", "");
		description = description.replaceAll("<h2>", "&lt;h2&gt;"); // <h2> &lt;h2&gt;
		description = description.replaceAll("</h2>", "&lt;/h2&gt;");// </h2> &lt;/h2&gt;\r\n
		description = description.replaceAll("<ul>", "&lt;ul&gt;");// <ul> \r\n&lt;ul&gt;\r\n
		description = description.replaceAll("<li>", "&lt;li&gt;"); // <li> \t&lt;li&gt;
		description = description.replaceAll("</li>", "&lt;/li&gt;"); // </li> &lt;/li&gt;\r\n
		description = description.replaceAll("</ul>", "&lt;/ul&gt;"); // </ul> &lt;/ul&gt;\r\n

		queryUSA = "INSERT INTO `oc_product_description` (`product_id`, `language_id`, `name`, `description`, `meta_description`, `meta_keyword`, `tag`, `u_title`, `u_h1`, `u_h2`) VALUES ('"
				+ product_id + "', '1', '" + name + "', '', '', '', '', '', '', '');";

		queryRUS = "INSERT INTO `oc_product_description` (`product_id`, `language_id`, `name`, `description`, `meta_description`, `meta_keyword`, `tag`, `u_title`, `u_h1`, `u_h2`) VALUES ('"
				+ product_id + "', '2', '" + name + "', '" + description + "', '', '', '', '', '', '');";

		result = queryUSA + "\n" + queryRUS;

		return result;
	}

	private String getQueryOcProductImage(Instrument instrument, int product_id, int img_id) {
		String result = "";

		String[] masImage = instrument.getImg().split("/");
		String image = "data/Instrument/"
				+ masImage[masImage.length - 2].replace(masImage[masImage.length - 2].substring(0, 1),
						masImage[masImage.length - 2].substring(0, 1).toUpperCase())
				+ "/" + masImage[masImage.length - 1];

		result = "INSERT INTO `oc_product_image` (`product_image_id`, `product_id`, `image`, `sort_order`) VALUES ('"
				+ img_id + "', '" + product_id + "', '" + image + "', '0');";

		return result;
	}

	private String getQueryOcProductReward(int product_id, int product_reward_id) {
		String result = "";

		result = "INSERT INTO `oc_product_reward` (`product_reward_id`, `product_id`, `customer_group_id`, `points`) VALUES ('"
				+ product_reward_id + "', '" + product_id + "', '1', '0');";

		return result;

	}

	private String getQueryOcProductToCategory(CatalogInstrument catalog, int product_id) {
		String result = "";

		String category_id = "";

		String startNameFromTitle = catalog.getTitle().split(" ")[0];

		switch (startNameFromTitle) {

		case "Бетонолом":
			category_id = "144";
			break;
		case "Бороздодел":
			category_id = "145";
			break;
		case "Виброшлифмашина":
			category_id = "167";
			break;
		case "Гайковерт":
			category_id = "147";
			break;
		case "Гайковёрт":
			category_id = "147";
			break;
		case "Дрель":
			category_id = "143";
			break;
		case "Дрель-шуруповерт":
			category_id = "143";
			break;
		case "Машина":
			category_id = "151";
			break;
		case "Миксер":
			category_id = "152";
			break;
		case "Молоток":
			category_id = "153";
			break;
		case "Ножницы":
			category_id = "154";
			break;
		case "Ножовка":
			category_id = "156";
			break;
		case "Очиститель":
			category_id = "157";
			break;
		case "Панельная":
			category_id = "158";
			break;
		case "Перфоратор":
			category_id = "159";
			break;
		case "Пила":
			category_id = "158";
			break;
		case "Пистолет":
			category_id = "160";
			break;
		case "Пылесос":
			category_id = "161";
			break;
		case "Резак":
			category_id = "162";
			break;
		case "Рубанок":
			category_id = "163";
			break;
		case "Стойка":
			category_id = "164";
			break;
		case "Стол":
			category_id = "165";
			break;
		case "Термовоздуходувка":
			category_id = "166";
			break;
		case "Угловая":
			category_id = "167";
			break;
		case "Фрезер":
			category_id = "168";
			break;
		case "Шлифмашина":
			category_id = "167";
			break;
		case "Шлифователь":
			category_id = "167";
			break;
		case "Шуруповерт":
			category_id = "169";
			break;
		case "Эксцентриковая":
			category_id = "167";
			break;
		}

		result = "INSERT INTO `oc_product_to_category` (`product_id`, `category_id`) VALUES ('" + product_id
				+ "', '138');" + "\n" + "INSERT INTO `oc_product_to_category` (`product_id`, `category_id`) VALUES ('"
				+ product_id + "', '142');" + "\n"
				+ "INSERT INTO `oc_product_to_category` (`product_id`, `category_id`) VALUES ('" + product_id + "', '"
				+ category_id + "');";

		return result;

	}

	private String getQueryOcProductToStore(int product_id) {
		String result = "";

		result = "INSERT INTO `oc_product_to_store` (`product_id`, `store_id`) VALUES ('" + product_id + "', '0');";

		return result;
	}

	private String getQueryOcUrlAlias(CatalogInstrument catalog, int product_id, int url_id) {
		String result = "";

		Pattern patternTitle = Pattern.compile("\\(.*\\)");

		String title = catalog.getTitle();
		Matcher matcherTitle = patternTitle.matcher(title);

		String symbolsInParenthesesFromTitle = "";

		if (matcherTitle.find()) {
			symbolsInParenthesesFromTitle = matcherTitle.group().toLowerCase();
		}

		String lineWithDash = symbolsInParenthesesFromTitle.replaceAll(" ", "-");
		String lineWithoutParentheses = lineWithDash.replaceAll("\\(", "").replaceAll("\\)", "");

		String url = catalog.getUrl();
		String[] masUrl = url.split("/");

		String keywordWithDash = masUrl[masUrl.length - 1].replace(lineWithoutParentheses, lineWithDash);
		String keywordWithUnderscore = keywordWithDash.replaceAll("-", "_");
		String keyword = keywordWithUnderscore.replace("_(", "__(");

		result = "INSERT INTO `oc_url_alias` (`url_alias_id`, `query`, `keyword`) VALUES ('" + url_id
				+ "', 'product_id=" + product_id + "', '" + keyword + "');";

		return result;
	}

	private void saveToFile() {
		String[] masName = original.split("\\.");

		String newFileName = masName[0] + "_modify." + masName[1];

		try {
			Files.write(Paths.get(newFileName), "".getBytes(), StandardOpenOption.CREATE);
			for (String line : modifyLines) {
				Files.write(Paths.get(newFileName), (line + "\n").getBytes(), StandardOpenOption.APPEND);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
