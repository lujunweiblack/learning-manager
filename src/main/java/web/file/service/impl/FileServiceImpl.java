package web.file.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import web.file.entity.FileT;
import web.file.mapper.FileMapper;
import web.file.service.FileService;

import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileMapper fileMapper;

    @Override
    public void insertFileInfo(FileT fileT) {
        fileMapper.insert(fileT);
    }

    @Override
    public List<FileT> find(FileT fileT) {
        return fileMapper.selectAll();
    }

    @Override
    public FileT findById(String id) {
        Example example = new Example(FileT.class);
        example.createCriteria().andEqualTo("id", id);
        List<FileT> fileTS = fileMapper.selectByExample(example);
        return fileTS.get(0);
    }

    @Override
    public void findDel(FileT fileT) {
        fileMapper.delete(fileT);
    }

    @Override
    public FileT findByFileName(String fileName) {
        Example example = new Example(FileT.class);
        example.createCriteria().andEqualTo("fileName", fileName);
        List<FileT> fileTS = fileMapper.selectByExample(example);
        if (fileTS.size() == 0) {
            return null;
        }
        return fileTS.get(0);
    }
}
