package com.bugztracker.service.impl;

import com.bugztracker.service.IFileService;
import com.bugztracker.service.IIssueService;
import com.bugztracker.service.util.UriBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.lang3.StringUtils;
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

/**
 * Created by oleg
 * Date: 01.11.15
 * Time: 17:44
 */
@Service
public class FileService implements IFileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private UriBuilder uriBuilder;

    @Autowired
    private IIssueService issueService;

    private void cleanAttachments() {
        String rootPath = uriBuilder.buildRootPathForAttachments();
        List<IssueAttachment> allAttachments = issueService
        for (IssueAttachment attachment : allAttachments) {
            String filePath = attachment.getAttachmentPath();
            if (StringUtils.isNotBlank(filePath) && !filePath.startsWith(rootPath)) {
                FileUtils.deleteQuietly(new File(filePath));
                issueService.delete(attachment);
            }
        }
    }

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

                IssueAttachment result = issueService.getAttachment(issueId, newName);
                if (result != null) {
                    log.info("File already exist.");
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
        return extractNames(issueService.getAttachments(issueId));
    }

    @Override
    public List<File> getAttachments(String issueId) {
        List<File> files = new ArrayList<>();
        List<String> paths = extractNames(issueService.getAttachments(issueId));
        for (String path : paths) {
            files.add(new File(path));
        }
        return files;
    }

    @Override
    public File get(String issueId, String fileName) {
        String name = uriBuilder.buildPathForAttachment(issueId, fileName);
        IssueAttachment attachment = issueService.getAttachment(issueId, name);
        return new File(attachment.getAttachmentPath());
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
        issueService.delete(issueService.getAttachment(issueId, name));
    }

    private List<String> extractNames(List<IssueAttachment> attachments) {
        List<String> paths = new ArrayList<>();
        for (IssueAttachment attachment : attachments) {
            String path = attachment.getAttachmentPath();

            if (isBlank(path)) {
                log.warn(format("Empty path for attachment %s", attachment.getId()));
                continue;
            }

            paths.add(path);
        }
        return paths;
    }
}
