package kr.co.hugetraffic.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final AmazonS3 amazonS3;
    @Value("reservesite-bucket")
    private String bucketName;

    public String upload(MultipartFile multipartFile) throws IOException {
        // NanoId를 통해 파일의 이름을 변경
        final String name = NanoIdUtils.randomNanoId();

        // 파일의 사이즈를 ContentLength로 알려주기 위해 ObjectMetadata 사용
        final ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        // S3에 파일을 올리기 위해 bucketName, 파일이름, inputStream, Object등을 한 객체로 저장
        final PutObjectRequest request = new PutObjectRequest(bucketName, name, multipartFile.getInputStream(), objMeta);

        try {
            final TransferManager tm = TransferManagerBuilder.standard().withS3Client(amazonS3).build();

            //TransferManager는 비동기이므로 즉시 반환됨
            final Upload upload = tm.upload(request);
            log.info("Object 업로드 시작 -> {}({}, {})", name, multipartFile.getContentType(), multipartFile.getSize());

            //upload가 완료되기를 기다림
            upload.waitForUploadResult();
            log.info("Object 업로드 완료 -> {}", name);

        } catch (AmazonServiceException e) {
            // 요청은 성공했지만 s3가 처리하지 못함
            log.error("Error 발생", e);
        } catch (SdkClientException e) {
            // s3에 응답을 달라고 연결할 수 없거나, 클라이언트가 s3의 응답 구문을 분석하지 못함
            log.error("Error 발생", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return name;
    }
}
