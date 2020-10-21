package com.trendyol.stock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trendyol.stock.controllers.StocksController;
import com.trendyol.stock.domain.Stock;
import com.trendyol.stock.domain.Post;
import com.trendyol.stock.repositories.StocksRepository;
import com.trendyol.stock.services.StockService;
import com.trendyol.stock.services.RestService;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class StockApplicationTests {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private StockService stockService;
    @MockBean
    private RestService restService;
    @InjectMocks
    private StocksController stocksController;
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(stocksController).build();
    }

    @Test
    public void createStock_whenStockIsNew_ShouldReturn201WithProperLocationHeader() throws Exception {
        //Given
        Stock mockStock = new Stock();
        mockStock.setId("dummy");
        String productId = "123";
        //When
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:8082/products/"+productId+"/stocks")
                .accept(MediaType.APPLICATION_JSON)
                .content(convertToJson(mockStock))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        //Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_CREATED);
       /* assertThat(response.getHeader(HttpHeaders.LOCATION))
                .isEqualTo(mockStock.getId());*/

    }
    @Test
    public void getStockByProductId_whenStockIsNull_ShouldReturn404() throws Exception {
        //Given
        String productId = "123456";
        //When
        when(stockService.getStockByProductId(productId)).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/stocks/"+ productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        //Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_NOT_FOUND);
    }
    @Test
    public void getStockByProductId_whenEverythingIsOK_ShouldReturn200() throws Exception {
        //Given
        Stock mockStock = new Stock();
        String productId = "123456";
        mockStock.setItemId(productId);
        stockService.createStock(mockStock);

        //When
        when(stockService.getStockByProductId(productId)).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:8082/products/"+productId+"/stocks")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        //Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);


    }
    @Test
    public void changeQuantityByProductId_whenEverythingIsOK_ShouldReturn200() throws Exception {
        //Given
        Stock mockStock = new Stock();
        String productId = "123456";
        mockStock.setItemId(productId);
        stockService.createStock(mockStock);
        int newQuantity = 10;

        //When
        when(stockService.changeQuantityByProductId(productId,newQuantity)).thenReturn(null);
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("http://localhost:8082/products/"+productId+"/stocks?quantity="+newQuantity)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        //Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);

    }
    @Test
    public void deleteItem_whenStockIsExisting_ShouldReturn200() throws Exception {
        //Given
        Stock mockStock = new Stock();
        String productId = "123456";
        mockStock.setItemId(productId);
        stockService.createStock(mockStock);
        //When
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("http://localhost:8082/products/stocks/"+mockStock.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        //Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_NO_CONTENT);

    }
    public static String convertToJson(Object o) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }

}
