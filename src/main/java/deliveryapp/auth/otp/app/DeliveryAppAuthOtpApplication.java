package deliveryapp.auth.otp.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class DeliveryAppAuthOtpApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(DeliveryAppAuthOtpApplication.class, args);
	}

}
