package com.csfw.jcxx.wh.websocket;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @USER wh
 * @DATE 2020/6/8
 * @Description
 */
@ServerEndpoint("/imserver/{userId}")
@Component
public class WebSocketServer {
    public static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static AtomicInteger onlineCount = new AtomicInteger();
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId = "";

    /**
     * 连接建立成功调用方法
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId")String userId){
        this.session = session;
        this.userId = userId;

        if (webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
        } else {
            webSocketMap.put(userId,this);
            onlineCount.incrementAndGet();
        }
        log.info("用户连接：{},当前在线人数：{}",userId,onlineCount.get());
    }

    /**
     * 连接关闭方法
     */
    @OnClose
    public void onClose(){
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            onlineCount.decrementAndGet();
        }
        log.info("用户退出:{},当前在线人数:{}",userId,onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message
     * @param session
     */
    @OnMessage
    public void OnMessage(String message,Session session){
        log.info("用户消息:{},报文:{}",userId,message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromUserId",this.userId);
                String toUserId=jsonObject.getString("toUserId");
                //传送给对应toUserId用户的websocket
                if(StringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)){
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                }else{
                    log.error("请求的userId:"+toUserId+"不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session,Throwable error){
        log.info("用户错误:{},原因:{}",userId,error.getMessage());
        error.printStackTrace();
    }

    /**
     * 服务器主动推送信息
     * @param text
     * @throws IOException
     */
    public void sendMessage(String text) throws IOException {
        this.session.getBasicRemote().sendText(text);
    }

    /**
     * 发送自定义消息
     * @param message
     * @param userId
     * @throws IOException
     */
    public static void sendInfo(String message,@PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:{},报文:{}",userId,message);
        if (StringUtils.isNotBlank(message) && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(message);
        }
    }
}
