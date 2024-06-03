package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddItem() throws Exception {
        ItemDto itemIn = new ItemDto(3L, "Testname_in", "Testdescription",true);
        ItemDto itemOut = new ItemDto(3L, "Testname_out", "Testdescription",true);

        when(itemService.addItem(any(), any()))
                .thenReturn(itemOut);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 10)
                        .content(mapper.writeValueAsString(itemIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemOut.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemOut.getName())))
                .andExpect(jsonPath("$.description", is(itemOut.getDescription())))
                .andExpect(jsonPath("$.available", is(itemOut.getAvailable())));

        verify(itemService, times(1)).addItem(10L, itemIn);
    }

    @Test
    void testEditItem() throws Exception {
        ItemDto itemIn = new ItemDto(3L, "Testname_in", "Testdescription",true);
        ItemDto itemOut = new ItemDto(3L, "Updated_name", "Updated_description",false);

        when(itemService.editItem(any(), any(), any()))
                .thenReturn(itemOut);

        mockMvc.perform(patch("/items/3")
                        .header("X-Sharer-User-Id", 10)
                        .content(mapper.writeValueAsString(itemIn))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemOut.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemOut.getName())))
                .andExpect(jsonPath("$.description", is(itemOut.getDescription())))
                .andExpect(jsonPath("$.available", is(itemOut.getAvailable())));

        verify(itemService, times(1)).editItem(3L, 10L, itemIn);
    }

    @Test
    void testGetItemById() throws Exception {
        ItemDto item = new ItemDto(3L, "Testname_in", "Testdescription",true);

        when(itemService.getItemById(any(), any()))
                .thenReturn(item);

        mockMvc.perform(get("/items/3")
                        .header("X-Sharer-User-Id", 10)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.getAvailable())));

        verify(itemService, times(1)).getItemById(3L, 10L);
    }

    @Test
    void testGetAllItemsByOwner() throws Exception {
        ItemDto item = new ItemDto(3L, "Testname_in", "Testdescription",true);

        when(itemService.getAllItemsByOwner(any(), any(), any()))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 10)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].available", is(item.getAvailable())));

        verify(itemService, times(1)).getAllItemsByOwner(10L, 0, 10);
    }

    @Test
    void testSearchItems() throws Exception {
        ItemDto item = new ItemDto(3L, "Testname_in", "Testdescription",true);

        when(itemService.searchItems(any(), any(), any(), any()))
                .thenReturn(List.of(item));

        mockMvc.perform(get("/items/search")
                        .param("text", "Testdescription")
                        .header("X-Sharer-User-Id", 10)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(item.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())))
                .andExpect(jsonPath("$[0].available", is(item.getAvailable())));

        verify(itemService, times(1)).searchItems("Testdescription",
                10L, 0, 10);
    }

    @Test
    void testAddComment() throws Exception {
        CommentDto comment = new CommentDto(9L, "test comment", "author", LocalDateTime.now()
                .withNano(0));

        when(itemService.addComment(any(), any(), any()))
                .thenReturn(comment);

        mockMvc.perform(post("/items/7/comment")
                        .header("X-Sharer-User-Id", 10)
                        .content(mapper.writeValueAsString(comment))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(comment.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(comment.getText())))
                .andExpect(jsonPath("$.authorName", is(comment.getAuthorName())))
                .andExpect(jsonPath("$.created", is(comment.getCreated().toString())));

        verify(itemService, times(1)).addComment(7L,
                10L, comment);
    }
}