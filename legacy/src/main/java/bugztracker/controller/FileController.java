package bugztracker.controller;

import bugztracker.exception.service.FileServiceException;
import bugztracker.service.IFileService;
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

/**
 * Created by oleg
 * Date: 31.10.15
 * Time: 13:05
 */
@Controller
@RequestMapping
public class FileController {

    @Autowired
    private IFileService fileService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/save/{issueId}", method = POST)
    public void save(@RequestParam(value = "files[]") List<MultipartFile> files,
                     @PathVariable int issueId) {

        fileService.save(files, issueId);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "issue/{issueId}/file", method = GET)
    public List<String> list(@PathVariable int issueId) {
        return fileService.listAttachments(issueId);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "issue/{issueId}/file/{fileName}", method = GET)
    public FileSystemResource downloadFile(@PathVariable int issueId,
                                           @PathVariable String fileName) {
        return new FileSystemResource(fileService.get(issueId, fileName));
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "issue/{issueId}/file{fileName}/remove", method = POST)
    public void removeFile(@PathVariable int issueId,
                           @PathVariable String fileName) {
        fileService.remove(issueId, fileName);
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
