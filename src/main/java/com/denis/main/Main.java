package com.denis.main;

import java.io.IOException;

import com.denis.parser.Parser;

public class Main {

	public static void main(String[] args) throws IOException {

		System.out.println("start");

		Parser parser = new Parser("http://stroyinstrument.by/catalog/elektroinstrumenty/");

		parser.start();

		System.out.println("end");
	}
}
