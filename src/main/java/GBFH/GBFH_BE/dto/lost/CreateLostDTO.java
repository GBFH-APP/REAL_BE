package GBFH.GBFH_BE.dto.lost;

import GBFH.GBFH_BE.entity.Board;
import GBFH.GBFH_BE.entity.BoardId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Getter
public class CreateLostDTO {
    private String title;
    private String contents;
    private String status;

    public static Board mapToBoard(CreateLostDTO createLostDTO, Long IDX, Long grp, String username, String userNo, String clientIp) throws UnknownHostException {
        return Board.builder()
                .idx(IDX)
                .grp(grp)
                .seq(1L) // 대부분 1로 저장됨
                .lvl(1L)
                .upIdx(0L)
                .noti(0)
                // noti start, noti end null 값
                .best((byte) 0)
                .sortNo(0L)
                .boardId(BoardId.valueOf("lost"))
                .category("")
                .title(createLostDTO.getTitle())
                .contents(createLostDTO.getContents())
                .writer(username)
                .maskWriter(createLostDTO.makeWriterMask(username))
                .passWD("")
                .read(0L)
                .popupYN('N') // 팝업 사용하지 않음
                .secretYN('N') // 공개글
                .trashYN('N') // 삭제하지 않음
                .editorYN('Y')
                .createLevel("dorm")
                .status(createLostDTO.getStatus())
                .houseNM("")
                .roomNo("")
                .linkUrl("")
                .di("")
                .ci("")
                .createId(userNo)
                .createIP(clientIp)
                .createDT(LocalDateTime.now())
                .build();
    }

    // 유저 이름 마스킹
    private String makeWriterMask(String username) {
        if (username == null || username.length() < 2)
            return username;

        if (username.length() == 2)
            return username.substring(0, 1) + "*";

        return username.substring(0, username.length() - 1) + "*";
    }

    // 현재 기기의 ip 주소 리턴
    private static String getIP() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Res {
        private Long id;
        private String title;
        private String contents;
        private String boardId;
        private String createId;
        private String createIP;
        private LocalDateTime createDT;

        public static Res mapToDTO(Board board) {
            return Res.builder()
                    .id(board.getIdx())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .boardId(board.getBoardId().name())
                    .createId(board.getCreateId())
                    .createIP(board.getCreateIP())
                    .createDT(board.getCreateDT())
                    .build();
        }
    }
}
