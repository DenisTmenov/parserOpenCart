package com.denis.main;

import java.io.IOException;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.denis.service.ParserFile;
import com.denis.service.ParserSite;

public class Main {

	public static void main(String[] args) throws IOException {

		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.scan("com.denis");
		context.refresh();

		System.out.println("start");

		ParserSite parserSite = new ParserSite("http://stroyinstrument.by/catalog/elektroinstrumenty/", context);

		parserSite.start();

		ParserFile parserFile = new ParserFile("d:/instrument/2017-11-24_16-32-26_backup.sql", context);

		parserFile.start();

		System.out.println("end");
	}
}
