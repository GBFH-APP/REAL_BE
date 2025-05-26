package GBFH.GBFH_BE.entity.sub;

import GBFH.GBFH_BE.entity.main.BoardFilePk;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="IDX")
    private Long idx;

    @Column(name="FILE_ID")
    private String fileId;

    @Column(name = "SEQ", columnDefinition = "BIGINT")
    private Long seq;


    @Column(name = "CREATE_IP")
    private String createIp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Lost_Board_Idx")
    private LostBoard lostBoard;

    public ImageFile setLostBoard(LostBoard lostBoard) {
        this.lostBoard = lostBoard;
        return this;
    }
}
