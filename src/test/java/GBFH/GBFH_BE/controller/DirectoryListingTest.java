package GBFH.GBFH_BE.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // 테스트 환경에서 Security 설정을 비활성화할 수 있는 프로파일
public class DirectoryListingTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testDirectoryListingDisabled() throws Exception {
        // "/static/" 경로에 접근할 때 404 상태 코드가 반환되는지 테스트
        mockMvc.perform(get("/static/"))
                .andExpect(status().isNotFound());  // 404 상태 코드를 기대
    }


    @Test
    public void testResourceNotFound() throws Exception {
        // 존재하지 않는 리소스에 접근할 때 404 상태 코드가 반환되는지 테스트
        mockMvc.perform(get("/resources/"))
                .andExpect(status().isNotFound());  // 404 상태 코드를 기대
    }
}
