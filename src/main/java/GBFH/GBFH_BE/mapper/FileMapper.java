package GBFH.GBFH_BE.mapper;


import GBFH.GBFH_BE.dto.boardFile.FileDTO;
import GBFH.GBFH_BE.entity.main.BoardFile;
import GBFH.GBFH_BE.entity.sub.ImageFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FileMapper {
    @Mapping(source = "idx", target = "id")
    @Mapping(source = "fileId", target = "uri")
    FileDTO toDto(BoardFile file);

    @Mapping(source = "fileId", target = "uri")
    @Mapping(source = "lostBoard.idx", target = "id") // 또는 lostBoard.id
    FileDTO toDto(ImageFile file);
}
