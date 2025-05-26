package GBFH.GBFH_BE.entity.sub;

import GBFH.GBFH_BE.dto.lost.UpdateLostContentDTO;
import GBFH.GBFH_BE.dto.lost.UpdateLostStatusDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LostBoard {
        // 관리 번호
        @Column(name = "IDX", columnDefinition = "BIGINT")
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idx;

        // 카테고리 -> 무조건 ""로 저장함
        @Column(name = "CATEGORY")
        private String category;

        @Column(name="TITLE")
        private String title;

        // 내용
        @Lob
        @Column(name = "CONTENTS", columnDefinition = "MEDIUMTEXT")
        private String contents;

        // 작성자명
        @Column(name = "WRITER")
        private String writer;

        // 마스킹 처리된 작성자 (이규*)
        @Column(name="MASK_WRITER")
        private String maskWriter; // 공지사항에서는 masking을 안 하긴 하는데...

        // 비밀번호 -> "" 값 넣음
        @Column(name = "PASSWD")
        private String passWD; // 필요한감

        // 조회수 - default 0
        @Getter
        @Column(name = "`READ`", columnDefinition = "BIGINT")
        private Long read; //조회수

        // 비밀 여부
        @Column(name = "SECRET_YN")
        private Character secretYN;

        // 에디터 여부
        @Column(name = "EDITOR_YN")
        private Character editorYN;

        // 상태 (접수 > 처리 중 > 처리 완료)
        @Column(name = "STATUS")
        @Enumerated(EnumType.STRING)
        private GBFH.GBFH_BE.entity.sub.Status status; // 값이 있으면 접수라고 되어있음

        // 동
        @Column(name = "HOUSE_NM")
        private String houseNM; // 도 필요할까...?

        // 호실
        @Column(name = "ROOM_NO")
        private String roomNo; // 도 필요할까...?

        // 본인인증 개인식별정보
        @Column(name = "DI")
        private String di;

        // 본인인증 사이트연계정보
        @Column(name = "CI")
        private String ci;

        // 등록자 권한
        @Column(name = "CREATE_LEVEL")
        private String createLevel;

        // 등록자
        @Column(name = "CREATE_ID", length = 60)
        private String createId;

        // 등록일자
        @Column(name="CREATE_DT")
        private LocalDateTime createDT; //작성일 - 시간은 안 보여주더라

        // 등록자 아이피
        @Column(name = "CREATE_IP", length = 40)
        private String createIP;

        // 수정자
        @Column(name = "UPDATE_ID", length = 60)
        private String updateID;

        @Column(name = "FILE_ID", length = 20)
        private String fileId;

        // 등록일자
        @Column(name="UPDATE_DT")
        private LocalDateTime updateDT; //작성일 - 시간은 안 보여주더라

        // 수정자 아이피
        @Column(name = "UPDATE_IP", length = 40)
        private String updateIP;

        // 임시 삭제 여부
        @Column(name = "TRASH_YN")
        private Character trashYN;

        @OneToMany(mappedBy = "lostBoard", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<ImageFile> imageFiles = new ArrayList<>();

        @OneToMany(mappedBy = "lostBoard", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Comment> comments = new ArrayList<>();


        public LostBoard readBoard() {
                this.read = this.getRead() + 1;
                return this;
        }

        // 삭제 처리
        public void delete() {
            this.trashYN = 'Y';
        }

        // status 업데이트
        public void updateStatus(UpdateLostStatusDTO updateLostStatusDTO) {
            this.status = Status.valueOf(updateLostStatusDTO.getStatus());
        }

        // title, content 업데이터
        public void updateContent(UpdateLostContentDTO updateLostContentDTO) {
            this.title = updateLostContentDTO.getTitle() == null ? this.title : updateLostContentDTO.getTitle();
            this.contents = updateLostContentDTO.getContents() == null ? this.contents : updateLostContentDTO.getContents();
            this.status = updateLostContentDTO.getStatus() == null ? this.status : Status.valueOf(updateLostContentDTO.getStatus());
        }

        public void updateTitleImage(String fileId) {
            this.fileId = fileId;
        }

        public String getContent() {
            return this.getContents();
        }

        public String getStatus() {
            return this.status.toString();
    }



}
