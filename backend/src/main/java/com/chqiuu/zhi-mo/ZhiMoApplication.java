package com.chqiuu.zhi-mo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chqiuu.zhi-mo.**.mapper")
public class ZhiMoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhiMoApplication.class, args);
    }
}
