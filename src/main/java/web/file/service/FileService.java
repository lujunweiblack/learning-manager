package web.file.service;

import web.file.entity.FileT;

import java.util.List;

public interface FileService {
    void insertFileInfo(FileT fileT);

    List<FileT> find(FileT fileT);

    FileT findById(String id);

    void findDel(FileT fileT);

    FileT findByFileName(String fileName);



}
