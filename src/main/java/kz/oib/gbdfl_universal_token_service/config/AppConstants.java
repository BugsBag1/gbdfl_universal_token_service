package kz.oib.gbdfl_universal_token_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "request-property")
@Data
public class AppConstants {
    private String serviceId;
    private String senderCode;
    private String vshepUrl;
    private Short chanelType;
}
