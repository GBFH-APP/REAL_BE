package GBFH.GBFH_BE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GbfhBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GbfhBeApplication.class, args);
	}

}
