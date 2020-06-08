package com.csfw.jcxx.wh.controller;

import com.csfw.jcxx.wh.websocket.WebSocketServer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @USER wh
 * @DATE 2020/6/8
 * @Description.
 */
@Controller
public class DemoController {

    @GetMapping("index")
    @ResponseBody
    public ResponseEntity<String> index(){
        return ResponseEntity.ok("请求成功");
    }

    @GetMapping("page")
    public String page(){
        return "websocket";
    }

    @RequestMapping("/push/{toUserId}")
    @ResponseBody
    public ResponseEntity<String> pushToWeb(String message, @PathVariable String toUserId) throws IOException {
        WebSocketServer.sendInfo(message,toUserId);
        return ResponseEntity.ok("MSG SEND SUCCESS");
    }
}
