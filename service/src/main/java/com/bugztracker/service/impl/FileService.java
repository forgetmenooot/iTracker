package com.bugztracker.service.impl;

import com.bugztracker.service.IFileService;
import com.bugztracker.service.IIssueService;
import com.bugztracker.service.exception.FileServiceException;
import com.bugztracker.service.util.UriBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class FileService implements IFileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private UriBuilder uriBuilder;

    @Autowired
    private IIssueService issueService;

    @Override
    public void save(List<MultipartFile> files, String issueId) {
        for (MultipartFile multipart : files) {
            // save
            try {
                String newName = uriBuilder.buildPathForAttachment(issueId, multipart.getOriginalFilename());
                File file = new File(newName);

                if (!file.mkdirs() && !file.createNewFile() && !file.exists() && file.length() > 0) {
                    log.warn(String.format("Can't create file %s.", newName));
                    continue;
                }
                multipart.transferTo(file);

                if (issueService.checkIfPathExists(issueId, newName)) {
                    log.warn("File already exist.");
                    continue;
                }

                issueService.addAttachment(issueId, newName);
            } catch (IOException e) {
                log.info(String.format("Can't save file %s for issue %s!", multipart.getOriginalFilename(), issueId), e);
                throw new FileServiceException("Can't save file!");
            }
        }
    }

    @Override
    public List<String> listAttachments(String issueId) {
        return issueService.getAttachments(issueId);
    }

    @Override
    public List<File> getAttachments(String issueId) {
        List<File> files = new ArrayList<>();
        List<String> paths = issueService.getAttachments(issueId);
        for (String path : paths) {
            files.add(new File(path));
        }
        return files;
    }

    @Override
    public File get(String issueId, String fileName) {
        String name = uriBuilder.buildPathForAttachment(issueId, fileName);
        return new File(name);
    }

    @Override
    public void remove(String issueId, String fileName) {
        String folder = uriBuilder.buildPathForIssueFolder(issueId);
        Collection<File> files = FileUtils.listFiles(new File(folder),
                CanReadFileFilter.CAN_READ, new PrefixFileFilter(fileName));
        for (File file : files) {
            if (file.getAbsolutePath().contains(fileName)) {
                FileUtils.deleteQuietly(file);
            }
        }

        String name = uriBuilder.buildPathForAttachment(issueId, fileName);
        issueService.deleteAttachment(issueId, name);
    }

}
