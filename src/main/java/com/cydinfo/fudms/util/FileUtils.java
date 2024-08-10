package com.cydinfo.fudms.util;

import com.cydinfo.fudms.dto.FileDownloadDto;
import com.cydinfo.fudms.vo.FileVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * FileUtils 클래스는 파일 관리 메소드를 제공합니다.
 * 파일업로드, 해시값, zip압축, 경로생성 기능을 포함합니다.
 */
@Component
public class FileUtils {

    @Value("${upload.directory}")
    private String uploadDirectory;

    public FileUtils() throws IOException {
    }

    /**
     * 파일 업로드 폴더 생성하는 메서드
     * @param files
     * @param fileDescription
     * @param attachmentTitle
     * @return List<FileVo>
     */
    public List<FileVo> uploadFile(MultipartFile[] files, List<String> fileDescription, String attachmentTitle){

        // 파일 배열이 비어 있을 경우 예외처리
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("File is null or empty");
        }

        // application.properties에 정의돼 있는 저장 경로
        String root = createDatePath(uploadDirectory) + "/" + attachmentTitle;
        File fileDirectory = new File(root);

        // 해당 디렉토리가 없을 경우 생성
        // ex) /C:/fudms/fudms_dir/2024/06/04
        if(!fileDirectory.exists()) fileDirectory.mkdirs();

        // fileVo를 담기위한 배열 생성
        List<FileVo> fileVos = new ArrayList<>();

        // 파일을 저장하고 fileVo객체를 만들기 위한 반복문
        // forEach는 fileDescription의 리스트에도 접근하기 위한 index가 없기 때문에 기본 반복문 사용
        for(int i = 0; i< files.length ; i++){

            // MultipartFile[]를 분리
            MultipartFile singleFile = files[i];

            // mimeType 추출
            String mimeType = URLConnection.guessContentTypeFromName(singleFile.getOriginalFilename());

            // 파일이름 추출
            String originFileName = singleFile.getOriginalFilename();

            // 파일 확장자 (안쓴다)
            String ext;

            if (originFileName.lastIndexOf(".") == -1) {
                ext = "Null";
            } else {
                ext = originFileName.substring(originFileName.lastIndexOf("."));
            }

            // 파일 생성 목적지
            File fileDestination = new File(root + "/" + singleFile.getOriginalFilename());

            // 파일 해쉬값 초기화
            String hash = "";

            try {
                // 파일 업로드
                singleFile.transferTo(fileDestination);

                // 파일 해시 계산
                hash = getHash(root + "/" + originFileName);
            } catch (IllegalStateException | IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            // filevo 생성
            FileVo fileVo = new FileVo(originFileName, root, singleFile.getSize(), fileDescription.get(i), ext, hash, mimeType);

            // FileVo배열에 fileVo 추가
            fileVos.add(fileVo);
        }
        return fileVos;

    }

    /**
     * 파일의 해시값을 찾는 메서드
     *
     * @param path
     * @return ex) bd68d8db60154ba88681684dc9e81c6c
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getHash(String path) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");

        // try-with-resources
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            byte[] dataBytes = new byte[1024];
            int nRead = 0;
            while ((nRead = fileInputStream.read(dataBytes)) != -1) {
                messageDigest.update(dataBytes, 0, nRead);
            }
        }

        byte[] mdBytes = messageDigest.digest();
        StringBuilder stringBuilder  = new StringBuilder();
        for(byte mdByte : mdBytes) {
            stringBuilder.append(String.format("%02x", mdByte));
        }
        return stringBuilder.toString();
    }

    /**
     * 날짜형식의 폴더 경로 만드는 메소드
     *
     * @param path
     * @return ex) /C:/fudms/fudms_dir/2024/06/04
     */
    public String createDatePath(String path){

        // 현재 날짜 가져오기
        LocalDate currentDate = LocalDate.now();

        // 현재 날짜에서 연도 가져오기
        String year = String.valueOf(Year.from(currentDate).getValue());

        // 현재 날짜에서 월 가져오기
        String month = String.format("%02d", currentDate.getMonthValue());

        // 현재 날짜에서 일 가져오기
        String day = String.format("%02d", currentDate.getDayOfMonth());

        // 파일서버 내부 경로
        return path + "/" + year + "/" + month + "/" + day;

    }


    // 폴더 내의 파일 및 하위 폴더 목록을 반환하는 메서드
    private static List<String> listFiles(String folderPath) throws IOException {
        List<String> fileNames = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
            paths.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .forEach(fileNames::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames;
    }


    /* File을 압축해주는 utils

     */
    // ZIP 파일 생성
    public static ResponseEntity<Resource> selectFiles(List<FileDownloadDto> fileDto, String attName)   {
        List<File> files = new ArrayList<>();
        try {
            for (FileDownloadDto dto : fileDto) {
                Path filePath = Paths.get(dto.getPath()).resolve(dto.getName() + "." + dto.getType()).normalize();
                File file = filePath.toFile();
                if (!file.exists() || !file.isFile()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                files.add(file);
            }
            File zipFile = Files.createTempFile("files", ".zip").toFile();
            try (FileOutputStream fos = new FileOutputStream(zipFile);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {
                for (File file : files) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zos.putNextEntry(zipEntry);
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                    }
                }
            }
            // 파일 리소스 생성
            FileSystemResource resource = new FileSystemResource(zipFile);
            // 파일 이름 설정
            String filename = attName+".zip";
            // HTTP 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(resource.contentLength());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // 파일을 응답으로 반환
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }




    public static ResponseEntity<Resource> selectOneFile(List<FileDownloadDto> fileDto) throws IOException{
        for (FileDownloadDto dto : fileDto) {
            try {
                Path filePath = Paths.get(dto.getPath()).resolve(dto.getName() + "." + dto.getType()).normalize();
                File file = filePath.toFile();
                System.out.println(FileUtils.getHash(filePath.toString()).equals(dto.getHash()));
                if (!file.exists() || !file.isFile()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                // 파일 리소스 생성
                FileSystemResource resource = new FileSystemResource(file);
                // 파일 이름 설정
                String filename = file.getName();
                // HTTP 헤더 설정
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDispositionFormData("attachment", filename);
                headers.setContentLength(resource.contentLength());
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                // 파일을 응답으로 반환
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                        .body(resource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
}
