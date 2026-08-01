package br.com.daniel.dev.api.aws.api_aws_java_spring_cloud_v2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppAwsLocalstack implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(AppAwsLocalstack.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
