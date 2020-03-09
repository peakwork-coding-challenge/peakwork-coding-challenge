package de.slauth.peakwork.nominatim.updateservice;

import de.slauth.peakwork.nominatim.client.ClientConfiguration;
import de.slauth.peakwork.nominatim.repo.RepoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@Import({ClientConfiguration.class, RepoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
