package com.denis.service;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.denis.dao.CatalogInstrumentDAO;
import com.denis.dao.InstrumentDAO;
import com.denis.dto.CatalogInstrument;
import com.denis.dto.Instrument;
import com.denis.utils.DataSaver;

public class ParserSite {
	@Autowired
	private CatalogInstrumentDAO catalogInstrumentDAO;
	@Autowired
	private InstrumentDAO instrumentDAO;

	private String start_URL = "";

	public static final String CATALOG_INSTRUMENT = "d:/instrument/catalogInstrument.txt";
	public static final String ALL_INSTRUMENT = "d:/instrument/allInstrument.txt";

	public ParserSite(String url, AnnotationConfigApplicationContext context) {
		start_URL = url;
		catalogInstrumentDAO = (CatalogInstrumentDAO) context.getBean("catalogInstrumentDAO");
		instrumentDAO = (InstrumentDAO) context.getBean("instrumentDAO");
	}

	public void start() throws IOException {

		DataSaver.saveToDB(catalogInstrumentDAO, getAllCatalogInstruments());
		DataSaver.saveToFile(CATALOG_INSTRUMENT, catalogInstrumentDAO.loadAllCategories());

		DataSaver.saveToDB(instrumentDAO, getAllInstrumentsInfo());
		DataSaver.saveToFile(ALL_INSTRUMENT, instrumentDAO.loadAllInstrument());

	}

	private List<CatalogInstrument> getAllCatalogInstruments() throws IOException {

		List<CatalogInstrument> result = new ArrayList<>();

		Document doc = Jsoup.connect(start_URL).get();

		int countPages = getCountPages(doc);

		// load instruments from first page
		result.addAll(loadInstrument(doc));

		if (countPages > 2) {
			for (int i = 2; i <= countPages; i++) {
				Document docNext = Jsoup.connect(start_URL + "/?PAGEN_1=" + i).get();

				// load instruments from other pages
				result.addAll(loadInstrument(docNext));
			}
		}

		return result;

	}

	private List<CatalogInstrument> loadInstrument(Document doc) {

		List<CatalogInstrument> result = new ArrayList<>();

		Elements h2InstrumentsElemetns = doc.getElementsByAttributeValue("class", "catalog-item-title");

		Elements priceInstrumentsElemetns = doc.getElementsByAttributeValue("class", "item-price");

		for (int i = 0; i < h2InstrumentsElemetns.size(); i++) {
			Element aElement = h2InstrumentsElemetns.get(i).child(0);
			String url = aElement.attr("href");
			String title = aElement.child(0).text();

			String codeItemSummary = h2InstrumentsElemetns.get(i).child(1).text();
			String codeItem = codeItemSummary.replaceAll("Код товара: ", "");

			Element spanPriceElement = priceInstrumentsElemetns.get(i).child(0);
			String price = spanPriceElement.text();
			price = price.replaceAll(" ", "");
			price = price.replaceAll("руб.", "");

			double doublePrice;
			try {
				doublePrice = Double.valueOf(price);
			} catch (Exception e) {
				doublePrice = 0.0;
			}

			result.add(EntityCreator.createCatalogInstrument(url, title, codeItem, doublePrice));

		}

		return result;
	}

	private List<Instrument> getAllInstrumentsInfo() {

		List<Instrument> result = new ArrayList<>();

		List<CatalogInstrument> loadAllCategories = catalogInstrumentDAO.loadAllCategories();

		for (CatalogInstrument entry : loadAllCategories) {
			Map<String, String> map = new HashMap<>();

			map.put("id", String.valueOf(entry.getId()));
			Pattern pattern = Pattern.compile("(^http:\\/\\/.*\\.\\w{2,3})");
			Matcher matcher = pattern.matcher(start_URL);

			String URL = "";

			if (matcher.find()) {
				URL = matcher.group() + entry.getUrl();
			}

			Document doc = null;
			try {
				doc = Jsoup.connect(URL).get();

				Elements characteristicsElemetns = doc.getElementsByAttributeValue("class", "catalog-detail-property");

				for (Element element : characteristicsElemetns) {
					if (element.child(0).text().equals("Производитель")) {
						map.put("brand", element.child(1).text());
					} else if (element.child(0).text().equals("Артикул")) {
						map.put("vendorCode", element.child(1).text());
					} else if (element.child(0).text().equals("Производство")) {
						map.put("madeIn", element.child(1).text());
					} else if (element.child(0).text().equals("Импортер")) {
						map.put("importer", element.child(1).text());
					} else if (element.child(0).text().equals("Гарантия")) {
						map.put("garante", element.child(1).text());
					} else if (element.child(0).text().equals("Вес")) {
						map.put("weight", element.child(1).text());
					}
				}

				Elements descriptionElemetns = doc.getElementsByAttributeValue("class", "description");

				for (Element element : descriptionElemetns) {

					map.put("description", element.html());
				}

				Elements imgElemetns = doc.getElementsByAttributeValue("class", "detail_picture");

				for (Element element : imgElemetns) {

					Elements elementsByTag = element.child(1).getElementsByTag("img");

					String src = elementsByTag.get(0).absUrl("src");

					src = src.replace("resize_cache/", "");
					src = src.replace("390_390_1/", "");

					int indexName = src.lastIndexOf("/");

					String[] masSrc = src.split("/");
					String[] masTypeFile = masSrc[masSrc.length - 1].split("\\.");
					String typeFile = masTypeFile[masTypeFile.length - 1];

					if (indexName == src.length()) {
						src = src.substring(1, indexName);

					}

					String[] masName = URL.split("/");
					String[] masCategory = masName[masName.length - 1].split("-");

					String imgName = masCategory[0] + "/" + masName[masName.length - 1] + "." + typeFile;

					URL url = null;
					try {
						url = new URL(src);
					} catch (MalformedURLException e) {
						System.out.println("line 239");
					}

					InputStream in = null;
					try {
						in = url.openStream();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					// create folders

					String dirPath = "d:/instrument/"
							+ imgName.replace(imgName.substring(0, 1), imgName.substring(0, 1).toUpperCase())
									.replaceFirst("/.*\\..*", "");
					String imgPath = "d:/instrument/" + imgName;

					Path pathDir = Paths.get(dirPath);
					Path pathImg = Paths.get(imgPath);
					Files.createDirectories(pathDir);
					Files.write(pathImg, "".getBytes(), StandardOpenOption.CREATE);

					OutputStream out = null;
					try {
						out = new BufferedOutputStream(new FileOutputStream(imgPath));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					try {
						for (int b; (b = in.read()) != -1;) {
							out.write(b);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					map.put("img", imgPath);
				}

			} catch (IOException e) {
				System.out.println("line 250");
				System.out.println(e);

			}

			if (map.get("brand") == null) {
				String img = map.get("img");
				String[] masImg = img.split("/");
				String[] masLastElementFromMasImg = masImg[masImg.length - 1].split("\\.");
				String[] masFerstElementFromLastElementFromMasImg = masLastElementFromMasImg[0].split("-");
				map.put("brand",
						masFerstElementFromLastElementFromMasImg[masFerstElementFromLastElementFromMasImg.length - 1]
								.replace(
										masFerstElementFromLastElementFromMasImg[masFerstElementFromLastElementFromMasImg.length
												- 1].substring(0, 1),
										masFerstElementFromLastElementFromMasImg[masFerstElementFromLastElementFromMasImg.length
												- 1].substring(0, 1).toUpperCase()));

			}

			result.add(EntityCreator.createInstrument(map));

		}

		return result;

	}

	private int getCountPages(Document doc) {
		Elements paginationsElements = doc.getElementsByAttributeValue("class", "pagination");
		int lastNumber = 0;
		Element numberElement = paginationsElements.get(0).child(0);
		String numbersString = numberElement.text().replaceAll(" Ctrl →", "");
		String[] numbers = numbersString.split(" ");
		try {
			lastNumber = Integer.valueOf(numbers[numbers.length - 1]);
			if (lastNumber < 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			lastNumber = 0;
		}

		return lastNumber;
	}
}
