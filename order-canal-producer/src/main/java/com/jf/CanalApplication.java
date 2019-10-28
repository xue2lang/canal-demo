package com.jf;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * @ClassName: Application 
 * @Description: 主启动类
 * @author: sunwei
 * @date: 2018年1月23日 下午9:01:41
 */
@SpringBootApplication
@MapperScan({"com.jf.modules.dao"})
public class CanalApplication {
    
    public static void main(String[] args) {

         SpringApplication.run(CanalApplication.class, args);
    }
 
}
