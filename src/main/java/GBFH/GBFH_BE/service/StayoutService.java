package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.stayout.StayoutRequestDTO;
import GBFH.GBFH_BE.dto.stayout.StayoutResponseDTO;
import GBFH.GBFH_BE.entity.Applicant;
import GBFH.GBFH_BE.entity.DormEnterSubmit;
import GBFH.GBFH_BE.entity.Stayout;
import GBFH.GBFH_BE.exception.EmptyPostException;
import GBFH.GBFH_BE.repository.ApplicantRepository;
import GBFH.GBFH_BE.repository.DormEnterSubmitRepository;
import GBFH.GBFH_BE.repository.StayoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StayoutService {
    private final StayoutRepository stayoutRepository;
    private final ApplicantRepository applicantRepository;
    private final DormEnterSubmitRepository dormEnterSubmitRepository;
    private final PaginateService paginateService;

    public StayoutResponseDTO createStayOut(String username, StayoutRequestDTO stayoutRequestDTO, String clientIp) {
        Applicant applicant = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        DormEnterSubmit dormEnterSubmit = dormEnterSubmitRepository.findTopByCreateIdOrderByTrackNoDesc(applicant.getUserNo());

        LocalDateTime createDT = LocalDateTime.now();
        Stayout stayout = Stayout.builder()
                .regiNo(dormEnterSubmit.getRegiNo())
                .seq(stayoutRepository.findMaxSeq(dormEnterSubmit.getRegiNo())+1)
                .createDT(createDT)
                .createId(applicant.getUserNo())
                .startDT(checkStartDT(stayoutRequestDTO.getStartDT(), createDT))
                .endDT(stayoutRequestDTO.getEndDT().atTime(23,0,0,0))
                .returnDT(null)
                .reason(stayoutRequestDTO.getReason())
                .approveType('N')
                .createIP(clientIp)
                .build();

        stayoutRepository.save(stayout);
        return StayoutResponseDTO.toDTO(stayout);
    }

    public LocalDateTime checkStartDT (LocalDate startDT, LocalDateTime createDT) {
        if (startDT.isAfter(LocalDate.now())) {
            return startDT.atTime(0,0,0,0);
        }
        else {
            return createDT;
        }
    }

    public Page<StayoutResponseDTO> getAllStayout(String username, int page, int size) {
        Applicant applicant = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        DormEnterSubmit dormEnterSubmit = dormEnterSubmitRepository.findTopByCreateIdOrderByTrackNoDesc(applicant.getUserNo());

        if (stayoutRepository.existsByRegiNo(dormEnterSubmit.getRegiNo())) {
            List<Stayout> stayouts = stayoutRepository.findAllByRegiNoOrderBySeqDesc(dormEnterSubmit.getRegiNo());
            List<StayoutResponseDTO> stayoutResponseDTOS = stayouts.stream().map((StayoutResponseDTO::toDTO)).collect(Collectors.toList());
            return paginateService.paginateList(stayoutResponseDTOS, page, size);
        }
        else {
            throw new EmptyPostException("외박 신청 글이 존재하지 않습니다.");
        }
    }
}
