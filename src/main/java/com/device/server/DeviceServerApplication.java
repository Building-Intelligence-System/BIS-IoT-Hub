
package com.device.server;

import com.device.server.server.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableConfigurationProperties
public class DeviceServerApplication {

    public static void main(final String... args) {
        SpringApplication.run(DeviceServerApplication.class, args);

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(DeviceServerApplication::run, 30, TimeUnit.SECONDS);
    }

    private static void run() {
        final ServerService server = Context.getBean(ServerService.class);

        System.out.println("Starting server...");
        try {
            server.startAll();
        } catch (Exception ex) {
            System.out.println("Error on server running up.");
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            try {
                server.stopAll();
            } catch (Exception ex) {
                System.out.println("Error on server shutting down.");
            }
        }));
    }
}
