package net.xdevelop.elasticlogdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElasticLogSampleApplication implements CommandLineRunner  {
	private final static Logger logger = LoggerFactory.getLogger(ElasticLogSampleApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(ElasticLogSampleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		for (int i = 0; i < 20; i++) {
			logger.info("Sample info log " + i);
			logger.error("Sample error log " + i);
		}
	}
}
