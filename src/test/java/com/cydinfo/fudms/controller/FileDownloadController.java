package com.cydinfo.fudms.controller;

import com.cydinfo.fudms.dto.FileDownloadDto;
import com.cydinfo.fudms.service.FileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;


@RestController
public class FileDownloadController {

    private final FileDownloadService svc;

    @Autowired
    public FileDownloadController(FileDownloadService svc) {
        this.svc = svc;
    }

    @ResponseBody
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ModelAndView downloadFilePage() throws Exception{
            List<FileDownloadDto> fileDownloadDto = svc.getAllFileInfo();
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("download");
            modelAndView.addObject("files", fileDownloadDto);
            return modelAndView;
    }


    @ResponseBody
    @RequestMapping(value = "/downloader", method = RequestMethod.POST)
    public ResponseEntity<Resource> downloadPage(@RequestBody HashMap<String, Object> files) throws Exception {
        FileDownloadDto dto = new FileDownloadDto();
        dto.setId((Long) files.get("id"));
        dto.setPath((String) files.get("path"));
        Path filePath = Paths.get(dto.getPath()).resolve(dto.getName()).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if(resource.exists()) {
            String contentType = null;
            try {
                contentType = Files.probeContentType(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }else{
           return ResponseEntity.notFound().build();
        }
    }
}