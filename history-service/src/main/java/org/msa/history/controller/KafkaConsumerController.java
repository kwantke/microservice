package org.msa.history.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


/*
kafka에서 broker는 producer가 요청한 내요을 중계해서
consumer가 가져갈 수 있도록 중계한다는 의미로
kafka broker라고 불린다.

kafka broker를 실행하기 위해서는 반드시 Zookeeper가 필요합니다.
Zookeeper는 Producer와 Consumer에게 어떤 Cluster 및
broker를 사용하게 할 지 알려줍니다
즉 Zookeeper는 Producer와 Consumer간 데이터를 전달하기 위해
어떤 클러스터와 브로커를 사용해야 할 지에 대한 정보를 관리하고
각 클러스터가 정상인지 아닌지에 대한 상태와 같은 정보를 관리하는 프로그램이다
*/
@Component
@Slf4j
public class KafkaConsumerController {

  @KafkaListener(topics = "${topic.name}", groupId = ConsumerConfig.GROUP_ID_CONFIG)
  public void consume(String data) throws IOException {
    System.out.println(String.format("Consumed data : %s", data));
  }
}
