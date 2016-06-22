package com.bugztracker.web.controllers;

import com.bugztracker.service.IFileService;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/api")
public class FileController {

    @Autowired
    private IFileService fileService;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/projects/{pid}/tickets/{iid}/files", method = POST)
    public void save(@RequestParam(value = "files[]") List<MultipartFile> files, @PathVariable String pid,
                     @PathVariable String iid) {

        fileService.save(files, iid);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/projects/{pid}/tickets/{iid}/files", method = GET)
    public List<String> list(@PathVariable String pid, @PathVariable String iid) {
        return fileService.listAttachments(iid);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/projects/{pid}/tickets/{iid}/files/{fileName}.{ext}", method = GET)
    public FileSystemResource downloadFile(@PathVariable String pid, @PathVariable String iid,
                                           @PathVariable String fileName,
                                           @PathVariable("ext") String ext) {
        return new FileSystemResource(fileService.get(iid, fileName + "." + ext));
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/projects/{pid}/tickets/{iid}/files/{fileName}.{ext}", method = DELETE)
    public void removeFile(@PathVariable String pid,
                           @PathVariable String iid,
                           @PathVariable String fileName,
                           @PathVariable("ext") String ext) {
        fileService.remove(iid, fileName+ "." + ext);
    }

    @ExceptionHandler(value ={RuntimeException.class, Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleError(HttpServletRequest req, Exception exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFoundException() {
        return "not-found";
    }


}
