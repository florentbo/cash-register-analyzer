package be.florentbo.register.controller;

import be.florentbo.register.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static be.florentbo.register.controller.OrderController.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class OrderControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private OrderService orderServiceMock;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testAddForm() throws Exception {
        mockMvc.perform(get("/orders/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(ORDER_VIEW));
    }

    @Test
    public void testOrderList() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_ORDER_LIST));
        verify(orderServiceMock).getDays();
        verifyNoMoreInteractions(orderServiceMock);
    }

    @Test
    public void testOrdersByDayList() throws Exception {
        String dateAsString = "2020-01-01";

        mockMvc.perform(get("/orders/?date="+dateAsString))
                .andExpect(status().isOk())
                .andExpect(view().name(VIEW_ORDER_DAY));
        LocalDate date = LocalDate.of(2020,1,1);
        verify(orderServiceMock).getDay(date);
        verifyNoMoreInteractions(orderServiceMock);
    }

    @Test
    public void test_search_orders_by_day_view() throws Exception {
        mockMvc.perform(get("/orders/search"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("dates", notNullValue()))
                .andExpect(view().name(VIEW_SEARCH_BY_DATES));
    }

    @Test
    public void test_search_orders_by_day_form() throws Exception {
        mockMvc.perform(get("/orders/search").param("startDate","05/08/2015").param("endDate","15/08/2015"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("report", notNullValue()))
                .andExpect(model().attribute("reportQueryParam", notNullValue()))
                .andExpect(view().name(VIEW_SEARCH_BY_DATES));

        LocalDate startDate = LocalDate.of(2015,8,5);
        LocalDate endDate = LocalDate.of(2015,8,15);
        verify(orderServiceMock).find(startDate, endDate);
        verifyNoMoreInteractions(orderServiceMock);
    }

    @Test
    public void test_search_orders_by_day_report() throws Exception {
        //String query = "orderDate >= 05/08/2015 and orderDate <= 15/08/2015";

        mockMvc.perform(get("/orders/report/?query=orderDate+%26gt%3B%3D+05%2F08%2F2015+and+orderDate+%26lt%3B%3D+15%2F08%2F2015"))
                        .andExpect(status().isOk());

        LocalDate startDate = LocalDate.of(2015,8,5);
        LocalDate endDate = LocalDate.of(2015,8,15);
        verify(orderServiceMock).print(startDate, endDate);
        verifyNoMoreInteractions(orderServiceMock);
    }
}