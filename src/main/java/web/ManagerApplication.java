package web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@MapperScan("web.file.mapper")
@Configuration
public class ManagerApplication {
    /**
     * 文件上传配置
     *
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //  单个数据大小
        factory.setMaxFileSize("1000000KB"); // 默认最大1GB (KB,MB)
        /// 总上传数据大小
        factory.setMaxRequestSize("2000000MB");// 默认多文件上传时最大2GB
        return factory.createMultipartConfig();
    }
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }
}
