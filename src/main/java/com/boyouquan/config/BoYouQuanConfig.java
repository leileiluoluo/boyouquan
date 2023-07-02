package com.boyouquan.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix = "boyouquan")
public class BoYouQuanConfig {

}
