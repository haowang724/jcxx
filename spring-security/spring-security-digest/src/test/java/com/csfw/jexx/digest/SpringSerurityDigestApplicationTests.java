package com.csfw.jexx.digest;

import com.csfw.jexx.digest.config.RestTempleteConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
class SpringSerurityDigestApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void whenSecuredRestApiIsConsumed_then200OK() throws IOException{
        RestTemplate restTemplate = new RestTempleteConfig().getRestTemplate();
//        String uri = "http://localhost:8080/login";
//        ResponseEntity<String> entity = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
        File file = new File("D:/10144.wav");
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        String url ="http://106.38.42.98:11080/axis-cgi/audio/transmit.cgi";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("audio/basic");
        headers.setContentType(type);
        HttpEntity<String> formEntity = new HttpEntity<>(new String(buffer), headers);
//        ResponseEntity<String> responseEntity =  restTemplate.postForEntity(url,formEntity,String.class);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, formEntity, String.class);
        if(200==responseEntity.getStatusCodeValue()&&"OK".equals(responseEntity.getBody())){
            System.out.println(responseEntity.getStatusCode());
            System.out.println(responseEntity.getBody());
        }

    }

}
