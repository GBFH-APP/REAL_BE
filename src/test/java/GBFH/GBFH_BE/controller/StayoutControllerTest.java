package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.service.StayoutService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@WebMvcTest(controllers = StayoutController.class)
public class StayoutControllerTest {

    @MockBean
    public StayoutService stayoutService;

    @Test
    public void shouldReturnAllStayouts_whenUserExists() throws Exception {
        stayoutService.getAllStayout("kiwi3866");

        verify(stayoutService, times(1)).getAllStayout("kiwi3866");
    }
}