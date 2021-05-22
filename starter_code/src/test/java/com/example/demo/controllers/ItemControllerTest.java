package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private String item1Name = "itemTest";
    private String item2Name = "itemTest2";
    private String wrongItemName = "itemNameWrong";
    private String item1Description = "Item One Test Description";
    private String item2Description = "Item Two Test Description";
    private long correctItem1Id = 1L;
    private long correctItem2Id = 2L;
    private long incorrectItemId = 20L;

    @Before
    public void setUp() {
        itemController = new ItemController();

        TestUtils.injectObjet(itemController, "itemRepository", itemRepository);

        List<Item> itemList = new ArrayList<>();
        Item itemTest1 = new Item();
        itemTest1.setId(correctItem1Id);
        itemTest1.setName(item1Name);
        itemTest1.setPrice(BigDecimal.valueOf(1.5));
        itemTest1.setDescription(item1Description);
        itemList.add(itemTest1);

        Item itemTest2 = new Item();
        itemTest2.setId(correctItem2Id);
        itemTest2.setName(item2Name);
        itemTest2.setPrice(BigDecimal.valueOf(1.5));
        itemTest2.setDescription(item2Description);
        itemList.add(itemTest2);

        when(itemRepository.findByName(item1Name)).thenReturn(itemList);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(itemTest1));
        when(itemRepository.findAll()).thenReturn(itemList);
    }

    @Test
    public void testGetItemsByNameShouldReturnItemList() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName(item1Name);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        assertEquals(item1Name, response.getBody().get(0).getName());
        assertEquals(item1Description, response.getBody().get(0).getDescription());
    }

    @Test
    public void testGetItemByIdShouldReturnItem() {
        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        assertEquals(item1Name, response.getBody().getName());
        assertEquals(item1Description, response.getBody().getDescription());
    }

    @Test
    public void testGetItemByWrongNameShouldReturnNotFound() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName(wrongItemName);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAllItemsShouldReturnItemList() {
        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> returnedItemList = response.getBody();

        assertNotNull(returnedItemList);
        assertEquals(2, returnedItemList.size());
        assertEquals(item1Name, returnedItemList.get(0).getName());
        assertEquals(item2Name, returnedItemList.get(1).getName());
    }
}
