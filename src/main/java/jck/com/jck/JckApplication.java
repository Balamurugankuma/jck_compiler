package jck.com.jck;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class JckApplication {
	public static void main(String[] args) {
		SpringApplication.run(JckApplication.class, args);
		JckCompiler obj=new JckCompiler();
	}

}
