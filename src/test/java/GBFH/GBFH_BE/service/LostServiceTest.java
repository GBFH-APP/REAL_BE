//package GBFH.GBFH_BE.service;
//
//import GBFH.GBFH_BE.dto.lost.CreateLostDTO;
//import GBFH.GBFH_BE.dto.lost.GetLostDTO;
//import GBFH.GBFH_BE.entity.Applicant;
//import GBFH.GBFH_BE.entity.Board;
//import GBFH.GBFH_BE.entity.BoardId;
//import GBFH.GBFH_BE.repository.ApplicantRepository;
//import GBFH.GBFH_BE.repository.BoardRepository;
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class LostServiceTest {
//    @Autowired
//    LostService lostService;
//
//    @Autowired
//    BoardRepository boardRepository;
//
//    @Autowired
//    ApplicantRepository applicantRepository;
//
//    @Test
//    void 존재하지_않는_username_분실물_생성() {
//        // given
//        CreateLostDTO createLostDTO1 = new CreateLostDTO("title", "content", "분실");
//
//        // when & then
//        assertThrows(UsernameNotFoundException.class, () -> lostService.createLost(createLostDTO1, "non_user", "127.0.0.1", null));
//    }
//
//    @Test
//    void 존재하는_username_분실물_생성() {
//        // given
//        createUser();
//        CreateLostDTO createLostDTO1 = new CreateLostDTO("title", "content", "분실");
//        CreateLostDTO.Res res = lostService.createLost(createLostDTO1, "eunseo", "127.0.0.1", null);
//
//        // when
//        Optional<Board> board = boardRepository.findByIdx(res.getId());
//
//        // theb
//        assertTrue(board.isPresent());
//    }
//
//    @Test
//    void 자신이_작성한_분실물_조회() {
//        // given
//        createUser();
//        CreateLostDTO createLostDTO1 = new CreateLostDTO("title", "content", "분실");
//        CreateLostDTO.Res res = lostService.createLost(createLostDTO1, "eunseo", "127.0.0.1", null);
//
//        // when
//        GetLostDTO.DETAIL detail = lostService.getDetailLost(res.getId(), "eunseo");
//
//        // then
//        assertTrue(detail.getPermission());
//    }
//
//    @Test
//    void 자신이_작성하지_않은_분실물_조회() {
//        // given
//        createUser();
//        CreateLostDTO createLostDTO1 = new CreateLostDTO("title", "content", "분실");
//        CreateLostDTO.Res res = lostService.createLost(createLostDTO1, "eunseo", "127.0.0.1", null);
//
//        // when
//        GetLostDTO.DETAIL detail = lostService.getDetailLost(res.getId(), "gyumin");
//
//        // then
//        assertFalse(detail.getPermission());
//    }
//
//    @Test
//    void 습득으로_필터링하여_분실물_조회() {
//        // given
//        createUser();
//        CreateLostDTO createLostDTO1 = new CreateLostDTO("title", "content", "습득");
//        CreateLostDTO createLostDTO2 = new CreateLostDTO("title", "content", "분실");
//
//        lostService.createLost(createLostDTO1, "eunseo", "127.0.0.1", null);
//        lostService.createLost(createLostDTO2, "eunseo", "127.0.0.1", null);
//
//        // when
//        List<Board> losts = boardRepository.findAllByBoardIdAndStatusOrderByIdxDesc(BoardId.lost, "습득");
//
//        // then
//        assertEquals(3, losts.size(), "리스트의 크기는 3이어야 합니다.");
//    }
//
//    @Test
//    void 조회수_증가() {
//        // given
//        createUser();
//        CreateLostDTO createLostDTO1 = new CreateLostDTO("title", "content", "분실");
//        CreateLostDTO.Res res = lostService.createLost(createLostDTO1, "eunseo", "127.0.0.1", null);
//
//        // when
//        lostService.getDetailLost(res.getId(), "eunseo");
//
//        // then
//        Board board = boardRepository.findByIdx(res.getId()).get();
//        assertEquals(board.getRead(), 1, "조회수 1 증가");
//    }
//
//
//    private void createUser() {
//        Applicant applicant1 = Applicant.builder()
//                .userNo("1")
//                .loginId("eunseo")
//                .loginPwd("sample")
//                .build();
//
//        applicantRepository.save(applicant1);
//
//        Applicant applicant2 = Applicant.builder()
//                .userNo("2")
//                .loginId("gyumin")
//                .loginPwd("sample")
//                .build();
//
//        applicantRepository.save(applicant2);
//
//    }
//
//}