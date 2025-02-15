package org.msa.item.controller;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.msa.item.dto.ItemDTO;
import org.msa.item.dto.ResponseDTO;
import org.msa.item.service.ItemService;
import org.msa.item.valid.ItemTypeValid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@OpenAPIDefinition(info = @Info(title = "물품 처리요청 API", description = "물품 처리요청 API", version = "v1"))
@RestController
@RequestMapping(value="v1/item")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {

  private final ItemService itemService;

  @Operation(summary = "물품등록 요청", description = "물품 등록을 진행할 수 있다.", tags = { "addItem" })
  @ApiResponses({ // 문서에서 응답값에 대한 정의를 추가함
          @ApiResponse(responseCode = "200", description = "SUCCESS"),
          @ApiResponse(responseCode = "501", description = "API EXCEPTION")
  })
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
