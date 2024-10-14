package GBFH.GBFH_BE.service;

import GBFH.GBFH_BE.exception.FileSizeIsNotAllowedException;
import GBFH.GBFH_BE.exception.NoExtensionException;
import GBFH.GBFH_BE.exception.NotAllowedExtensionException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.net.URL;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3 amazonS3;
    private final BoardConfigService boardConfigService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;



    public String upload(MultipartFile file, String folderName,String boardId) throws IOException {
        // 1. 파일 확장자 추출 및 허용 여부 검증
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.contains(".")) {
            throw new NoExtensionException("파일 확장자가 없습니다.");
        }
        // 확장자 추출
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();

        // 2. MIME 타입 허용 여부 검증
        String contentType = file.getContentType();

        if (!boardConfigService.isAllowedContentType(fileExtension,boardId, contentType)) {
            throw new NotAllowedExtensionException("파일 확장자가 올바르지 않습니다.");
        }
        if (!boardConfigService.isFileSizeAllowed(String.valueOf(file.getSize()), boardId)) {
            throw new FileSizeIsNotAllowedException("파일이 너무 큽니다.");
        }

        try {
            // UUID를 파일명에 추가 (varchar(20)으로 들어갈 수 있도록)
            String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
            String fileName = folderName + "/" + uuid;

            System.out.println(putS3(file, fileName));
            return uuid;

        } catch (Exception e) {
            throw new RuntimeException("S3에 파일 업로드 중 오류 발생", e);
        }
    }

    // 업로드 시 s3 메타데이터와 파일 크기 검증 절차



/*    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        // 파일 이름에서 공백을 제거한 새로운 파일 이름 생성
        //String originalFileName = multipartFile.getOriginalFilename();

        // UUID를 파일명에 추가 (varchar(20)으로 들어갈 수 있도록)
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 20);

        //String uniqueFileName = uuid + "_" + originalFileName.replaceAll("\\s", "_");

        String fileName = dirName + "/" + uuid;
        log.info("fileName: " + fileName);
        // S3에 파일 업로드
        System.out.println(putS3(multipartFile, fileName));
        return uuid;
    }*/

    private String putS3(MultipartFile multipartFile, String fileName) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        // 업로드된 파일의 URL을 반환
        URL fileUrl = amazonS3.getUrl(bucket, fileName);

        // URL이 null인지 확인
        if (fileUrl == null) {
            throw new RuntimeException("S3에서 URL을 가져올 수 없습니다.");
        }

        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public void deleteFile(String fileName) {
        try {
            // URL 디코딩을 통해 원래의 파일 이름을 가져옴
            String decodedFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("Deleting file from S3: " + decodedFileName);
            amazonS3.deleteObject(bucket, decodedFileName);
        } catch (UnsupportedEncodingException e) {
            log.error("Error while decoding the file name: {}", e.getMessage());
        }
    }
}
