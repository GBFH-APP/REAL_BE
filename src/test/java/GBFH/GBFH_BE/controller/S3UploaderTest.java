package GBFH.GBFH_BE.controller;

import GBFH.GBFH_BE.exception.FileSizeIsNotAllowedException;
import GBFH.GBFH_BE.exception.NotAllowedExtensionException;
import GBFH.GBFH_BE.service.BoardConfigService;
import GBFH.GBFH_BE.service.S3Uploader;
import com.amazonaws.services.s3.AmazonS3;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
@ActiveProfiles("test") // 테스트 환경에서 Security 설정을 비활성화할 수 있는 프로파일
public class S3UploaderTest {
    @Mock
    private BoardConfigService boardConfigService;  // Mock으로 주입할 서비스

    @InjectMocks
    private S3Uploader s3Uploader;  // 테스트할 클래스

    @Mock
    private AmazonS3 amazonS3;  // 의존성 모킹

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    private static final String FOLDER = "lost";

    @Test
    public void testFileUploadWithValidFile() throws IOException {
        // 정상적인 파일 생성 (예: 1MB 크기)
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                new ByteArrayInputStream(new byte[1024 * 1024])  // 1MB 크기
        );

        // Mock 설정: BoardConfigService의 파일 크기 및 MIME 타입 체크
        when(boardConfigService.isFileSizeAllowed(anyString(), anyString())).thenReturn(true);
        when(boardConfigService.isAllowedContentType(anyString(), anyString(), anyString())).thenReturn(true);

        // Mock 설정: S3에 업로드된 파일의 URL 반환
        String uploadedFileName = FOLDER + "/test-uuid";
        String expectedUrl = "https://" + bucket + ".s3.amazonaws.com/" + uploadedFileName;

        // Mock S3 URL
        when(amazonS3.getUrl(eq(bucket), eq(uploadedFileName))).thenReturn(new URL(expectedUrl));

        // S3에 업로드하는 메서드 호출 (예외 없이 정상 실행 확인)
        String resultUrl = s3Uploader.upload(file, FOLDER, "lost");

        // 업로드된 URL이 예상된 URL과 일치하는지 확인 (UUID 때문에 contains 사용)
        assertTrue(resultUrl.contains(expectedUrl));

        // S3에 파일 업로드가 정상적으로 호출되었는지 검증
        verify(amazonS3, times(1)).putObject(eq(bucket), anyString(), any(), any());
    }


    @Test
    public void testFileUploadWithTooLargeFile() throws IOException {
        // 너무 큰 파일 생성 (예: 6MB)
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                new ByteArrayInputStream(new byte[6 * 1024 * 1024])  // 6MB 크기
        );

        // Mock 설정: BoardConfigService의 MIME 타입 체크에서 true 반환
        when(boardConfigService.isAllowedContentType(anyString(), anyString(), anyString())).thenReturn(true);

        // Mock 설정: BoardConfigService의 파일 크기 체크에서 false 반환 (즉, 파일이 너무 큼)
        when(boardConfigService.isFileSizeAllowed(anyString(), anyString())).thenReturn(false);

        // 파일 크기 초과로 예외 발생 검증
        assertThrows(FileSizeIsNotAllowedException.class, () -> s3Uploader.upload(file, FOLDER, "lost"));
    }


    @Test
    public void testFileUploadWithInvalidExtension() throws IOException {
        // 잘못된 확장자 파일 생성
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.exe",
                "application/octet-stream",
                new ByteArrayInputStream(new byte[1024])  // 1KB 크기
        );

        // Mock 설정: BoardConfigService의 확장자 체크에서 false 반환
        when(boardConfigService.isAllowedContentType(anyString(), anyString(), anyString()))
                .thenReturn(false);

        // 허용되지 않은 확장자로 예외 발생 검증 (정확한 예외 타입 확인)
        assertThrows(NotAllowedExtensionException.class, () -> s3Uploader.upload(file, FOLDER, "lost"));
    }

    @Test
    public void testFileUploadWithInvalidMimeType() throws IOException {
        // 잘못된 MIME 타입 파일 생성
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                "application/octet-stream",  // 잘못된 MIME 타입
                new ByteArrayInputStream(new byte[1024])  // 1KB 크기
        );

        // Mock 설정: BoardConfigService의 MIME 타입 체크에서 false 반환
        when(boardConfigService.isAllowedContentType(anyString(), anyString(), anyString()))
                .thenReturn(false);

        // 허용되지 않은 MIME 타입으로 예외 발생 검증
        assertThrows(NotAllowedExtensionException.class, () -> s3Uploader.upload(file, FOLDER, "lost"));
    }
}
