package GBFH.GBFH_BE.dto.lost;

import GBFH.GBFH_BE.dto.boardFile.FileDTO;
import GBFH.GBFH_BE.entity.main.Board;
import GBFH.GBFH_BE.entity.main.BoardId;
import GBFH.GBFH_BE.entity.sub.LostBoard;
import GBFH.GBFH_BE.entity.sub.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CreateLostDTO {
    @NotEmpty(message = "제목은 필수 입력 값입니다.")
    private String title;
    private String contents;
    @NotNull(message = "상태는 필수 입력 값입니다.")
    private String status;

    public static LostBoard mapToBoard(CreateLostDTO createLostDTO, String username, String userNo, String clientIp, String url) throws UnknownHostException {
        return LostBoard.builder()
                .fileId(url)
                .category("")
                .title(createLostDTO.getTitle())
                .contents(createLostDTO.getContents())
                .writer(username)
                .maskWriter(createLostDTO.makeWriterMask(username))
                .passWD("")
                .read(0L)
                .secretYN('N') // 공개글
                .trashYN('N') // 삭제하지 않음
                .editorYN('Y')
                .createLevel("dorm")
                .status(Status.valueOf(createLostDTO.getStatus()))
                .houseNM("")
                .roomNo("")
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
        private String id;
        private String title;
        private String contents;
        private String status;
        private LocalDateTime createDT;
        private String writer;
        private List<FileDTO> files;

        public static Res mapToDTO(LostBoard board, List<FileDTO> files) {
            return Res.builder()
                    .id(board.getIdx().toString())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .status(board.getStatus())
                    .createDT(board.getCreateDT())
                    .writer(board.getMaskWriter())
                    .files(files)
                    .build();
        }
    }
}
