package lol.cicco.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;


@Slf4j
@SpringBootApplication
@MapperScan(basePackages = "lol.cicco.admin.mapper")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		log.info("Admin启动完成..");
	}
}
