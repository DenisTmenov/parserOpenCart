package com.denis.parser;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.denis.entity.CatalogInstrument;
import com.denis.entity.Instrument;

public class Parser {

	private String start_URL = "";

	private List<CatalogInstrument> catalogInstrumentList = new ArrayList<>();

	private List<Instrument> instrumentList = new ArrayList<>();

	public Parser(String url) {
		start_URL = url;
	}

	public void start() throws IOException {

		loadAllCatalogInstruments();

		printAllInstrumentToConsole();

		saveToFile("d:/instrument/catalogInstrument.txt", catalogInstrumentList);

		List<String> allInstrumentsURLFromCatalog = getAllInstrumentsURLFromCatalog(
				"d:/instrument/catalogInstrument.txt");

		loadAllInstrumentsInfo(allInstrumentsURLFromCatalog);

		saveToFile("d:/instrument/allInstrument.txt", instrumentList);

	}

	private List<String> getAllInstrumentsURLFromCatalog(String fileNameAndPath) throws IOException {

		List<String> allURL = new ArrayList<>();

		List<String> readAllLines = Files.readAllLines(Paths.get(fileNameAndPath));

		Pattern pattern = Pattern.compile("(\\$\\{url\\}.*\\$\\{url\\})");

		for (String line : readAllLines) {

			Matcher matcher = pattern.matcher(line);

			if (matcher.find()) {
				String fullURL = matcher.group(1).replace("${url}/catalog/elektroinstrumenty/", start_URL)
						.replace("${url}", "");
				allURL.add(fullURL);
			}

		}
		return allURL;

	}

	private <E> void saveToFile(String fileNameAndPath, List<E> list) throws IOException {
		Files.write(Paths.get(fileNameAndPath), "".getBytes(), StandardOpenOption.CREATE);
		for (E instrument : list) {
			Files.write(Paths.get(fileNameAndPath), (instrument.toString() + "\n").getBytes(),
					StandardOpenOption.APPEND);
		}
	}

	private void loadAllInstrumentsInfo(List<String> catalog) throws IOException {

		for (String URL : catalog) {
			Document doc = Jsoup.connect(URL).get();

			Elements characteristicsElemetns = doc.getElementsByAttributeValue("class", "catalog-detail-property");

			String brand = "";
			String vendorCode = "";
			String madeIn = "";
			String importer = "";
			String garante = "";
			String weight = "";

			for (Element element : characteristicsElemetns) {
				if (element.child(0).text().equals("Производитель")) {
					brand = element.child(1).text();
				} else if (element.child(0).text().equals("Артикул")) {
					vendorCode = element.child(1).text();
				} else if (element.child(0).text().equals("Производство")) {
					madeIn = element.child(1).text();
				} else if (element.child(0).text().equals("Импортер")) {
					importer = element.child(1).text();
				} else if (element.child(0).text().equals("Гарантия")) {
					garante = element.child(1).text();
				} else if (element.child(0).text().equals("Вес")) {
					weight = element.child(1).text();
				}
			}

			Elements descriptionElemetns = doc.getElementsByAttributeValue("class", "description");
			String description = "";
			for (Element element : descriptionElemetns) {
				description = element.text();
			}

			Elements imgElemetns = doc.getElementsByAttributeValue("class", "detail_picture");
			String img = "";
			for (Element element : imgElemetns) {

				Elements elementsByTag = element.child(1).getElementsByTag("img");

				String src = elementsByTag.get(0).absUrl("src");

				int indexName = src.lastIndexOf("/");

				if (indexName == src.length()) {
					src = src.substring(1, indexName);

				}

				String[] masName = URL.split("/");

				String name = masName[masName.length - 1] + ".jpg";

				URL url = new URL(src);

				InputStream in = url.openStream();

				OutputStream out = new BufferedOutputStream(new FileOutputStream("d:/instrument/" + name));

				for (int b; (b = in.read()) != -1;) {
					out.write(b);
				}
				out.close();
				in.close();
				img = "d:/instrument/" + name;
			}

			instrumentList.add(new Instrument(img, brand, vendorCode, madeIn, importer, garante, weight, description));

		}

	}

	private void loadAllCatalogInstruments() throws IOException {

		Document doc = Jsoup.connect(start_URL).get();

		int countPages = getCountPages(doc);

		// load instruments from first page
		loadInstruments(doc);

		if (countPages > 2) {
			for (int i = 2; i <= countPages; i++) {
				Document docNext = Jsoup.connect(start_URL + "/?PAGEN_1=" + i).get();

				// load instruments from other pages
				loadInstruments(docNext);
			}
		}

	}

	private void loadInstruments(Document doc) {

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

			catalogInstrumentList.add(new CatalogInstrument(url, title, codeItem, doublePrice));

		}
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

	private void printAllInstrumentToConsole() {

		catalogInstrumentList.forEach(System.out::println);

		System.out.println("end");

	}
}
