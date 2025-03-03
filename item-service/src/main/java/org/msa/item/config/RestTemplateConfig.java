package org.msa.item.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

  @Bean
  @LoadBalanced
  public RestTemplate restTemplate() {
    // RestTemplate 의 설정을 추가위한 클래스 HttpComponentsClientHttpRequestFactory
    // setConnectionRequestTimeout 은 요청에 대한 타임 아웃 설정. 3초
    HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    httpRequestFactory.setConnectionRequestTimeout(3000);
    httpRequestFactory.setConnectTimeout(10000); // 10초
    return new RestTemplate(httpRequestFactory);
  }
}

