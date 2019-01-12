package web.file.mapper;

import org.apache.ibatis.annotations.Select;
import web.file.baseMapper.MyMapper;
import web.file.entity.FileT;

import java.util.List;

public interface FileMapper extends MyMapper<FileT> {


    @Select("SELECT * FROM file_t ORDER BY CREATE_DATE DESC")
    List<FileT> selectAll();
}
