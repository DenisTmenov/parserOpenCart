package com.denis.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import com.denis.dto.CatalogInstrument;
import com.denis.dto.Instrument;

public class DataSaver {

	private static final String ERROR = "Problem in DataSaver.class";

	public static <E, T> void saveToDB(E dao, List<T> list) {
		Method methodAdd = null;
		String name = list.get(0).getClass().getSimpleName();

		try {
			switch (name) {
			case "CatalogInstrument":
				methodAdd = dao.getClass().getMethod("add", CatalogInstrument.class);
				break;

			case "Instrument":
				methodAdd = dao.getClass().getMethod("add", Instrument.class);
				break;
			}

		} catch (NoSuchMethodException e) {
			throw new ExceptionUtils(ERROR + " in saveToDB method (NoSuchMethodException)!", e);
		} catch (SecurityException e) {
			throw new ExceptionUtils(ERROR + " in saveToDB method (SecurityException)!", e);
		}

		if (list.size() > 0) {
			for (T element : list) {
				try {
					methodAdd.invoke(dao, element);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					System.out.println("(Duplicate entry)! : " + element.toString());

				}
			}
		} else {
			System.out.println("CatalogInstrument is not saved!!!");

		}
	}

	public static <E> void saveToFile(String fileNameAndPath, List<E> list) throws IOException {

		String mas[] = fileNameAndPath.split("/");

		String dirPath = "";

		for (int i = 0; i < mas.length - 1; i++) {

			dirPath += mas[i] + "/";
		}

		Path path = Paths.get(dirPath);

		Files.createDirectories(path);

		Files.write(Paths.get(fileNameAndPath), "".getBytes(), StandardOpenOption.CREATE);
		for (E instrument : list) {
			Method method = null;
			String name = list.get(0).getClass().getSimpleName();

			try {
				switch (name) {
				case "CatalogInstrument":
					method = instrument.getClass().getMethod("getUrl", null);
					break;

				case "Instrument":
					method = instrument.getClass().getMethod("getImg", null);
					break;
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String masInstrument[] = null;
			try {
				masInstrument = method.invoke(instrument, null).toString().split("/");
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String aaa = masInstrument[masInstrument.length - 2].replace(
					masInstrument[masInstrument.length - 2].substring(0, 1),
					masInstrument[masInstrument.length - 2].substring(0, 1).toUpperCase());

			path = Paths.get(dirPath + aaa);
			Files.createDirectories(path);
			Files.write(Paths.get(fileNameAndPath), (instrument.toString() + "\n").getBytes(),
					StandardOpenOption.APPEND);
		}
	}
}
