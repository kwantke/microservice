package org.msa.item.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msa.item.dto.ItemDTO;
import org.msa.item.dto.ResponseDTO;
import org.msa.item.dto.constant.ItemType;
import org.msa.item.exception.ApiException;
import org.msa.item.service.ItemService;
import org.msa.item.valid.ItemTypeValid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value="v1/item")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {

  private final ItemService itemService;

  @RequestMapping(value="/add/{itemType}", method = RequestMethod.POST)
  public ResponseEntity<ResponseDTO> add(@Valid @RequestBody ItemDTO itemDTO,@ItemTypeValid @PathVariable String itemType) {
    ResponseDTO.ResponseDTOBuilder responseBuilder = ResponseDTO.builder();
/*
    log.debug("path.variable itemType = {}",itemType);
    boolean hasItemType = false;
    List<ItemType> itemTypeList = Arrays.asList(ItemType.values());
    hasItemType = itemTypeList.stream()
            .anyMatch(item -> item.hasItemCd(itemType));
    if (!hasItemType) {
      responseBuilder.code("500").message("invalid itemType .["+ itemType + "]");
      return ResponseEntity.ok(responseBuilder.build());
    } else {
      itemDTO.setItemType(itemType);
    }
*/
    /*try {
      Integer.parseInt("test");
    } catch (Exception e) {
      throw new ApiException("test에러");
    }*/
    itemDTO.setItemType(itemType);
    itemService.insertItem(itemDTO);
    log.debug("request add item id = {}", itemDTO.getId());

    responseBuilder.code("200").message("success");
    return ResponseEntity.ok(responseBuilder.build());
  }

}
