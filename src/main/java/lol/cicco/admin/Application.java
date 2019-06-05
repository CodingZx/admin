package lol.cicco.admin;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "lol.cicco.admin.common.handler")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		log.info("Admin启动完成..");
	}
}
