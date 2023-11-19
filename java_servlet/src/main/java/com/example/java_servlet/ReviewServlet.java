package com.example.java_servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

@WebServlet(urlPatterns = "/review/*")
public class ReviewServlet extends HttpServlet {
  private final static String QUEUE_NAME = "reaction";
  private Gson gson;
  private ConnectionFactory rmqFactory;
  private Connection connection;

  @Override
  public void init() throws ServletException {
    this.rmqFactory = new ConnectionFactory();

    try {
      this.rmqFactory.setUri("amqps://b-193a2bb4-3adb-4fe0-9e8c-b8489b51c326.mq.us-west-2.amazonaws.com:5671");
      this.rmqFactory.setUsername("someusername");
      this.rmqFactory.setPassword("somepassword");
    } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
      System.err.println(e.getMessage());
    }

    /**
    try{
      this.rmqFactory.setHost("localhost");
    }catch (Exception e){
      System.err.println(e.getMessage());
    }
     **/
    this.gson = new Gson();
    try{
      this.connection = rmqFactory.newConnection();
    }catch (IOException | TimeoutException e){
      System.err.println(e.getMessage());
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    try {
      if (this.connection != null && this.connection.isOpen()) {
        this.connection.close();
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }

  private void sendResponse(HttpServletResponse resp, String message) throws IOException{
    PrintWriter out = resp.getWriter();
    out.print(message);
    out.flush();
  }

  private void sendErrorResponse(HttpServletResponse resp, String message) throws IOException {
    ErrorMsg msg = new ErrorMsg();
    msg.setMsg(message);
    sendResponse(resp, gson.toJson(msg));
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("application/json");
    resp.setCharacterEncoding("UTF-8");
    String pathInfo = req.getPathInfo();
    if (pathInfo == null || pathInfo.isEmpty()) {
      sendErrorResponse(resp, "Invalid Request");
      return;
    }
    String[] parts = pathInfo.substring(1).split("/");
    if (parts.length > 2) {
      sendErrorResponse(resp, "Invalid Request");
      return;
    }
    HashMap<String, String> messageMap = new HashMap<>();
    messageMap.put("albumId", parts[1]);
    messageMap.put("reaction", parts[0]);
    publishToQueue(gson.toJson(messageMap));
    sendResponse(resp, "Added reaction");
  }

  private void publishToQueue(String message){
    try(Channel channel = this.connection.createChannel();){
      channel.queueDeclare(QUEUE_NAME, true, false, false, null);
      channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
    } catch (IOException | TimeoutException e) {
      System.out.println(e);
    }
  }

}
