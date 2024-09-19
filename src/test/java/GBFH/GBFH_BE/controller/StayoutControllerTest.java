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
    private StayoutService stayoutService;

}