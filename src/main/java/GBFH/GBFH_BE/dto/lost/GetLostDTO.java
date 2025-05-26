package GBFH.GBFH_BE.dto.lost;

import GBFH.GBFH_BE.dto.boardFile.FileDTO;
import GBFH.GBFH_BE.entity.sub.LostBoard;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class GetLostDTO {

    // 리스트 안에 들어갈 내용들
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListItem {
        private String id;
        private String title;
        private String contents;
        private String status;
        private LocalDateTime createDT;
        // 이미지 추가해야 함
        private String fildId;

        public static ListItem mapToDTO(LostBoard board) {
            return ListItem.builder()
                    .id(board.getIdx().toString())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .status(board.getStatus())
                    .createDT(board.getCreateDT())
                    .fildId(board.getFileId())
                    .build();
        }
    }
    // 리스트 조회
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LIST {
        private String status;
        private List<ListItem> listItems;
    }

    // category별로 리스트 조회
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryList {
        private String id;
        private String title;
        private String contents;
        private String status;
        private LocalDateTime createDT;
        // 이미지 추가해야 함
        private String fildId;

        public static CategoryList mapToDTO(LostBoard board) {
            return CategoryList.builder()
                    .id(board.getIdx().toString())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .status(board.getStatus())
                    .createDT(board.getCreateDT())
                    .fildId(board.getFileId())
                    .build();
        }
    }

    // 상세 조회
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DETAIL {
        private String id;
        private String title;
        private String contents;
        private String status;
        private String writer;
        private LocalDateTime createDT;
        private Boolean permission;
        // 이미지 추가해야 함
        private List<FileDTO> files;
        // 댓글
        private List<GetCommentDTO> comments;

        public static DETAIL mapToDTO(LostBoard board, Boolean permission, List<FileDTO> files, List<GetCommentDTO> comments) {
            return DETAIL.builder()
                    .id(board.getIdx().toString())
                    .title(board.getTitle())
                    .contents(board.getContents())
                    .status(board.getStatus())
                    .writer(board.getMaskWriter())
                    .createDT(board.getCreateDT())
                    .permission(permission)
                    .files(files)
                    .comments(comments)
                    .build();
        }

    }
}
