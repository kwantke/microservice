package org.msa.item.service;

import lombok.RequiredArgsConstructor;
import org.msa.item.domain.Item;
import org.msa.item.dto.ItemDTO;
import org.msa.item.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ItemService {
  private final ItemRepository itemRepository;

  public void insertItem(ItemDTO itemDTO, String accountId) {
    SimpleDateFormat form = new SimpleDateFormat("yyyyMMddHHmmss");
    String date = form.format(new Date());

    Item item = Item.builder()
            .id(itemDTO.getId())
            .accountId(accountId)
            .name(itemDTO.getName())
            .description(itemDTO.getDescription())
            .itemType(itemDTO.getItemType())
            .count(itemDTO.getCount())
            .regDts(date)
            .updDts(date)
            .build();
    itemRepository.save(item);
  }
}
