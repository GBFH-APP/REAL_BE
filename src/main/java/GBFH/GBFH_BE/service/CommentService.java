package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.dto.lost.CreateCommentDTO;
import GBFH.GBFH_BE.dto.lost.GetCommentDTO;
import GBFH.GBFH_BE.dto.lost.GetReplyDTO;
import GBFH.GBFH_BE.entity.main.Applicant;
import GBFH.GBFH_BE.entity.sub.Comment;
import GBFH.GBFH_BE.entity.sub.LostBoard;
import GBFH.GBFH_BE.exception.CommentNotFoundException;
import GBFH.GBFH_BE.exception.NoPermissionException;
import GBFH.GBFH_BE.exception.PostNotFoundException;
import GBFH.GBFH_BE.repository.main.ApplicantRepository;
import GBFH.GBFH_BE.repository.sub.CommentRepository;
import GBFH.GBFH_BE.repository.sub.LostBoardRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ApplicantRepository applicantRepository;
    private final LostBoardRepository lostBoardRepository;

    public List<GetCommentDTO> getCommentDTOSWithBoardIdx(Long boardIdx, Applicant user) {
        // 한 번의 쿼리로 lvl = 1 및 lvl = 2 댓글 가져오기
        List<Comment> allComments = commentRepository.findByLostBoardIdxAndDelYN(boardIdx, "N");

        // lvl = 2 댓글을 grp 기준으로 Map에 저장 (Key: grp, Value: 대댓글 리스트)
        Map<Long, List<Comment>> replyMap = allComments.stream()
                .filter(comment -> comment.getLvl() == 2)
                .collect(Collectors.groupingBy(Comment::getGrp));

        // lvl = 1 댓글만 필터링 후 대댓글 매핑
        List<GetCommentDTO> commentDTOS = allComments.stream()
                .filter(comment -> comment.getLvl() == 1)
                .map(comment -> {
                    Boolean commentPermission = comment.getCreateId().equals(user.getUserNo());

                    // grp 기준으로 대댓글 리스트 가져오기 (없으면 빈 리스트)
                    List<GetReplyDTO> commentReplyDTOS = replyMap.getOrDefault(comment.getGrp(), Collections.emptyList())
                            .stream()
                            .map(reply -> {
                                Boolean replyPermission = reply.getCreateId().equals(user.getUserNo());
                                return GetReplyDTO.mapToReplyDTO(reply, replyPermission);
                            })
                            .toList();

                    return GetCommentDTO.mapToCommentDTO(comment, commentPermission, commentReplyDTOS);
                }).toList();

        return commentDTOS;
    }


    @Transactional("subTransactionManager")
    public CreateCommentDTO.Res createComment(Long boardId, String username, CreateCommentDTO createCommentDTO, String clientId) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Long grp = commentRepository.findMaxGrp() + 1;

        LostBoard lostBoard = lostBoardRepository.findById(boardId).orElseThrow(()->
                new PostNotFoundException("해당 글을 찾을 수 없습니다."));


        // 댓글 객체 생성
        Comment comment = CreateCommentDTO.mapToComment(createCommentDTO, lostBoard, grp, user.getNameKor(), user.getUserNo(), clientId);
        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);

        return CreateCommentDTO.Res.mapToDTO(savedComment);
    }


    @Transactional("subTransactionManager")
    public CreateCommentDTO.Res createReply(Long boardId, Long commentId, String username, @Valid CreateCommentDTO createCommentDTO, String clientIp) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Comment comment = commentRepository.findByIdxAndDelYN(commentId, "N")
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));

        Long grp = comment.getGrp(); // 댓글과 동일한 그룹을 가짐

        LostBoard lostBoard = lostBoardRepository.findById(boardId).orElseThrow(()->
                new PostNotFoundException("해당 글을 찾을 수 없습니다."));

        Comment createdComment = CreateCommentDTO.mapToCommentReply(createCommentDTO, lostBoard, grp, user.getNameKor(), user.getUserNo(), clientIp);
        Comment savedComment = commentRepository.save(createdComment);

        return CreateCommentDTO.Res.mapToDTO(savedComment);
    }

    @Transactional("subTransactionManager")
    public void deleteCommentReply(Long boardId, Long commentId, String username) {
        Applicant user = applicantRepository.findByLoginId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        Comment comment = commentRepository.findByIdxAndDelYN(commentId, "N")
                .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));


        // 글 작성자인가?
        if(!comment.getCreateId().equals(user.getUserNo())) {
            throw new NoPermissionException("작성자가 아닙니다.");
        }

        comment.delete();
        // 상위 댓글인가? (대댓글이 아닌가?)
        if(comment.getLvl() == 1L) {
            // 대댓글 조회
            List<Comment> commentReplies = commentRepository.findByLostBoardIdxAndDelYNAndLvlAndGrp(boardId, "N", 2L, comment.getGrp());
            commentReplies.forEach(Comment::delete); // 대댓글 모두 휴지통 처리
        }
    }


}
