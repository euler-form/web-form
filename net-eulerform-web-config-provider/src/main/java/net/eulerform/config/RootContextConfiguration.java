package net.eulerform.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;

import net.eulerform.web.core.annotation.RestEndpoint;
import net.eulerform.web.core.annotation.WebController;

@Configuration
@ImportResource({"classpath*:config/beans.xml"})
@ComponentScan(
        basePackages = {"net.eulerform.web",
                        "com.eulerform.web"},
        excludeFilters = {@ComponentScan.Filter(Controller.class),
                          @ComponentScan.Filter(WebController.class),
                          @ComponentScan.Filter(RestEndpoint.class)}
)

public class RootContextConfiguration {
    
}