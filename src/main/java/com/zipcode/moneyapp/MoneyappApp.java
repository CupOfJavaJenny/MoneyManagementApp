package com.zipcode.moneyapp;

import com.zipcode.moneyapp.config.ApplicationProperties;
import com.zipcode.moneyapp.config.CRLFLogConverter;
import com.zipcode.moneyapp.domain.BankAccount;
import com.zipcode.moneyapp.repository.BankAccountRepository;
import com.zipcode.moneyapp.web.rest.UserProfileResource;
import jakarta.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.jhipster.config.DefaultProfileUtil;
import tech.jhipster.config.JHipsterConstants;

@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationProperties.class })
public class MoneyappApp {

    private static final Logger log = LoggerFactory.getLogger(MoneyappApp.class);

    private final Environment env;
    private final BankAccountRepository bankAccountRepository;

    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedOrigins(
                        "http://localhost:8309",
                        "http://localhost:8310",
                        "http://localhost:8311",
                        "http://127.0.0.1:8309",
                        "http://127.0.0.1:8310",
                        "http://127.0.0.1:8311",
                        "http://morganbank.zipcode.rocks",
                        "https://localhost:8309",
                        "https://localhost:8310",
                        "https://localhost:8311",
                        "https://127.0.0.1:8309",
                        "https://127.0.0.1:8310",
                        "https://127.0.0.1:8311",
                        "https://morganbank.zipcode.rocks"
                    );
            }
        };
    }

    public MoneyappApp(Environment env, BankAccountRepository bankAccountRepository) {
        this.env = env;
        this.bankAccountRepository = bankAccountRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startupTasks() {
        // Binary search for highest used account number, then add 1
        // temporary
        List<BankAccount> list = bankAccountRepository.findAll();
        Long result = 0L;
        for (BankAccount acc : list) {
            if (acc.getAccountNumber() > result) {
                result = acc.getAccountNumber();
            }
        }
        //        BinarySearch.setBankAccountRepository(bankAccountRepository);

        //        Long result = BinarySearch.binarySearch(500000000L, 999999999L, 0L);

        UserProfileResource.setHighestAccountNumber(result + 1);
    }

    /**
     * Initializes moneyapp.
     * <p>
     * Spring profiles can be configured with a program argument --spring.profiles.active=your-active-profile
     * <p>
     * You can find more information on how profiles work with JHipster on <a href="https://www.jhipster.tech/profiles/">https://www.jhipster.tech/profiles/</a>.
     */
    @PostConstruct
    public void initApplication() {
        Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_PRODUCTION)
        ) {
            log.error(
                "You have misconfigured your application! It should not run " + "with both the 'dev' and 'prod' profiles at the same time."
            );
        }
        if (
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT) &&
            activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_CLOUD)
        ) {
            log.error(
                "You have misconfigured your application! It should not " + "run with both the 'dev' and 'cloud' profiles at the same time."
            );
        }
    }

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MoneyappApp.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = Optional.ofNullable(env.getProperty("server.ssl.key-store")).map(key -> "https").orElse("http");
        String applicationName = env.getProperty("spring.application.name");
        String serverPort = env.getProperty("server.port");
        String contextPath = Optional.ofNullable(env.getProperty("server.servlet.context-path"))
            .filter(StringUtils::isNotBlank)
            .orElse("/");
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info(
            CRLFLogConverter.CRLF_SAFE_MARKER,
            """

            ----------------------------------------------------------
            \tApplication '{}' is running! Access URLs:
            \tLocal: \t\t{}://localhost:{}{}
            \tExternal: \t{}://{}:{}{}
            \tProfile(s): \t{}
            ----------------------------------------------------------""",
            applicationName,
            protocol,
            serverPort,
            contextPath,
            protocol,
            hostAddress,
            serverPort,
            contextPath,
            env.getActiveProfiles().length == 0 ? env.getDefaultProfiles() : env.getActiveProfiles()
        );
    }
}
