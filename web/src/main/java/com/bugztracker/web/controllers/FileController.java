package com.bugztracker.web.controllers;

import com.bugztracker.service.IFileService;
import com.bugztracker.service.exception.FileServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping("/issue")
public class FileController {

    @Autowired
    private IFileService fileService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{issueId}/save", method = POST)
    public void save(@RequestParam(value = "files[]") List<MultipartFile> files,
                     @PathVariable String issueId) {

        fileService.save(files, issueId);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{issueId}/files", method = GET)
    public List<String> list(@PathVariable String issueId) {
        return fileService.listAttachments(issueId);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{issueId}/file/{fileName}.{ext}", method = GET)
    public FileSystemResource downloadFile(@PathVariable String issueId,
                                           @PathVariable String fileName,
                                           @PathVariable("ext") String ext) {
        return new FileSystemResource(fileService.get(issueId, fileName + "." + ext));
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{issueId}/file/{fileName}.{ext}/remove", method = POST)
    public void removeFile(@PathVariable String issueId,
                           @PathVariable String fileName,
                           @PathVariable("ext") String ext) {
        fileService.remove(issueId, fileName+ "." + ext);
    }

    @ExceptionHandler(value = {
            MultipartException.class,
            FileServiceException.class})
    @ResponseBody
    private ResponseEntity uploadErrorHandler(Throwable e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", e.getMessage());

        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }

}
