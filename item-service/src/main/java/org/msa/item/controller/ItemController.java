package org.msa.item.controller;


import lombok.extern.slf4j.Slf4j;
import org.msa.item.dto.ItemDTO;
import org.msa.item.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="v1/item")
@Slf4j
public class ItemController {

  public ResponseEntity<ResponseDTO> add(@RequestBody ItemDTO itemDTO) {
    ResponseDTO.ResponseDTOBuilder responseDTOBuilder = ResponseDTO.builder();

    log.debug("request add item id = {}", itemDTO.getId());

    responseDTOBuilder.code("200").message("success");
    return ResponseEntity.ok(responseDTOBuilder.build());
  }

}
