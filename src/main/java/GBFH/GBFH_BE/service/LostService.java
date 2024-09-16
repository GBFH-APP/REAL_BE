package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.lost.CreateLostDTO;
import GBFH.GBFH_BE.dto.lost.GetLostDTO;
import GBFH.GBFH_BE.entity.Applicant;
import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.entity.BoardId;
import GBFH.GBFH_BE.exception.InvalidHostException;
import GBFH.GBFH_BE.exception.NotLostException;
import GBFH.GBFH_BE.exception.PostNotFoundException;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import GBFH.GBFH_BE.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LostService {
    private final BoardRepository boardRepository;
    private final ApplicantRepository applicantRepository;

    @Transactional
    public CreateLostDTO.Res createLost(CreateLostDTO createLostDTO, String username, String clientIp) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Long IDX = boardRepository.findMaxIdx();
        Long grp = boardRepository.findMaxGrp();

        try {
            Board lost = CreateLostDTO.mapToBoard(createLostDTO, IDX + 1, grp + 1, user.getNameKor(), user.getUserNo(), clientIp);
            Board savedLost = boardRepository.save(lost);
            return CreateLostDTO.Res.mapToDTO(savedLost);

        } catch (UnknownHostException e) {
            throw new InvalidHostException("호스트 연결 실패");
        }
    }

    public List<GetLostDTO.LIST> getAllLosts() {
        List<Board> losts = boardRepository.findAlByBoardIdOrderByIdxDesc(BoardId.lost);

        return losts.stream().map(GetLostDTO.LIST::mapToDTO).collect(Collectors.toList());
    }

    public GetLostDTO.DETAIL getDetailLost(Long id, String username) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Board lost = boardRepository.findByIdx(id)
                .orElseThrow(() -> new PostNotFoundException("찾는 글이 없습니다."));

        if(!lost.getBoardId().toString().equals("lost"))
            throw new NotLostException("분실물 글이 아닙니다.");

        Boolean permission = user.getUserNo().equals(lost.getCreateId());

        return GetLostDTO.DETAIL.mapToDTO(lost, permission);
    }

    public List<GetLostDTO.LIST> getLostsByStatus(String status) {
        List<Board> losts = boardRepository.findAllByBoardIdAndStatusOrderByIdxDesc(BoardId.lost, status);

        return losts.stream().map(GetLostDTO.LIST::mapToDTO).collect(Collectors.toList());
    }
}
