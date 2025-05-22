package cc.wangzijie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private static final int TIMEOUT_TS = 5000;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 设置连接超时
        factory.setConnectTimeout(TIMEOUT_TS);
        // 设置读取超时
        factory.setReadTimeout(TIMEOUT_TS);
        return new RestTemplate(factory);
    }

}
