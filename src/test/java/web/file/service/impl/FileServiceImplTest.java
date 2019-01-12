package web.file.service.impl;
import java.util.Date;

import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import web.ManagerApplication;
import web.file.entity.FileT;
import web.file.service.FileService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ManagerApplication.class)
public class FileServiceImplTest {

    @Autowired
    private FileService fileService;

    @Test
    public void test01(){

        FileT fileT = new FileT();
        fileT.setId(0);
        fileT.setFileName("1");
        fileT.setFilePath("2");
        fileT.setFileType("3");
        fileT.setCreateDate(new Date());


        fileService.insertFileInfo(fileT);
    }

}