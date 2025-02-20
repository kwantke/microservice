package org.msa.history;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

// 넷플릭스에서 개발된 Http Client 기능을 제공하기 위한 라이브러리로
// 보통 Http 통신을 하기 위해서는 Http core 관련 라이브러리를 추가해서
// 하나하나 연결하고 연결 해제하고 데이터 가공하고 이런 로직을 일일이 다 구현해줘야 하는데
// EnableFeignClients 단순하게 JPA 처럼 단순하게 어노테이션을 사용해서 쉽게 HTtp 통신을 할수 있다
@EnableFeignClients
@SpringBootApplication
public class HistoryServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(HistoryServiceApplication.class, args);
  }

}
