package org.msa.gatewayserver.filter;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Flow;

// WebFilter 는 Spring에서 제공하는 인터셉터 기능을 대신 제공해주는 Filter이다.
// 다시말해, 요청 처리에 대한 전처리와 후처리 부분을 제공하는 인터페이스이다.
@Component
@Slf4j
public class LogginFilter implements WebFilter {

  // Mono 클래스는 데이터 전송 처리를 하기 위해 사용되는 클래스이다.
  // ServerWebExchange 는 Http 요청과 응답에 대한 액세스를 제공하는 기능을 갖고 있고,
  // WebFilterChain 객체는 다음 필터 항목을 전달하기 위해 사용하는 객체다.
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    // response 와 request 객체를 exchange 객체를 통해 가져오고
    ServerHttpResponse response = exchange.getResponse();
    ServerHttpRequest request = exchange.getRequest();
    DataBufferFactory dataBufferFactory = response.bufferFactory();

    // decoratedRequest 객체는 request 객체에 존재하는 요청 데이터를 스트리밍하기 위해
    // ServerHttpRequestDecorator 라는 클래스를 통해 기능 재정의가 진행된 객체
    // log the request body
    ServerHttpRequest decoratedRequest = getDecoratedRequest(request);

    // ResponseDecorator 객체는 response 객체에 존재하는 응답데이터를 읽어오기위해
    // ServerHttpResponseDecorator 라는 클래스를 통해 기능 재정의가 진행된 객체이다
    // 그래서 chain 객체를 통해 filter 함수를 진해하게 되면 filter 또는 마이크로서비스로 데이터를 전달하게 되는데
    // 여기 안에 파라미터로 들어가는 decoratedRequest, decoratedResponse 객체는 실제로 데이터가 전달되는 시점에 재 정의된 기능을 수행하게된다.
    // 즉, 실제로 요청 데이터를 다음 filter나 마이크로서비스에 전달할때 request로 들어온 json 데이터를 출력하는 부분을 decoratedRequest 가 재정의 된 기능에 개발해야하고,
    // response 들어온 json 데이터를 출력하는 부분을 decoratedResponse 가 재정의 된 기능에 개발해야한다.
    // log the response body
    ServerHttpResponseDecorator decoratedResponse = getDecoratedResponse(response, request, dataBufferFactory);
    return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build());
  }



  private ServerHttpResponseDecorator getDecoratedResponse(ServerHttpResponse response, ServerHttpRequest request, DataBufferFactory dataBufferFactory) {
    return new ServerHttpResponseDecorator(response) {
      @Override
      public Mono<Void> writeWith(final Publisher<? extends DataBuffer> body) {

        if (body instanceof Flux) {

          Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;

          return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                            DefaultDataBuffer joinedBuffers = new DefaultDataBufferFactory().join(dataBuffers);
                            byte[] content = new byte[joinedBuffers.readableByteCount()];
                            joinedBuffers.read(content);
                            String responseBody = new String(content, StandardCharsets.UTF_8);//MODIFY RESPONSE and Return the Modified response
                            log.info("request.id: {}, method: {}, url: {}, \nresponse body :{}", request.getId(), request.getMethod().name(), request.getURI(), responseBody);

                            return dataBufferFactory.wrap(responseBody.getBytes());
                          })
                          .switchIfEmpty(Flux.defer(() -> {

                            System.out.println("If empty");
                            return Flux.just();
                          }))
          ).onErrorResume(err -> {
            log.error("error while decorating Response: {}", err.getMessage());
            return Mono.empty();
          });

        } else {
          System.out.println("Not Flux");
        }
        return super.writeWith(body);
      }
    };
  }

  private ServerHttpRequest getDecoratedRequest(ServerHttpRequest request) {

    return new ServerHttpRequestDecorator(request) {
      // request 데이터를 스트리밍 하기 위해 재정의하는 부분이라고 보면 된다.
      @Override
      public Flux<DataBuffer> getBody() {

        log.info("request.id: {}, method: {} , url: {}", request.getId(), request.getMethod().name(), request.getURI());

        // 1️⃣ 클라이언트 PORT 출력
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        if (remoteAddress != null) {
          log.info("Client PORT: {}", remoteAddress.getPort());
        }

        return super.getBody().publishOn(Schedulers.boundedElastic()).doOnNext(dataBuffer -> {
          try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Channels.newChannel(baos).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
            String body = baos.toString(StandardCharsets.UTF_8);
            //  log.info 부분이 request body를 로그로 출력하는 부분이라고 보면 된다.
            log.info("request.id: {}, request body :{}", request.getId(), body);
          } catch (Exception e) {
            log.error(e.getMessage());
          }
        });
      }
    };
  }
}
// 위 기능은 webFlux 개념이 들어가 부분으로
// WebFlux는 리소스 최적화를 고려해서 API 기반 요청과 응답을 처리하는데 도와주는 라이브러리이다.