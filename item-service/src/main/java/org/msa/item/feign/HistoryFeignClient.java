package org.msa.item.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

//eureka에 history-service로 등록되어 있는
// 마이크로서비스로 요청하겠다는 의미
@FeignClient(name = "history-service")
public interface HistoryFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/v1/history/save")
    String saveHistory(Map<String, Object> paramMap);
}
