package io.swagger.client.client;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;
import io.swagger.client.api.LikeApi;
import io.swagger.client.model.AlbumInfo;
import io.swagger.client.model.AlbumsProfile;
import io.swagger.client.model.ImageMetaData;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import javax.sound.midi.SysexMessage;

public class Producer implements Runnable{
  private String IPAddr;
  private String image_file;
  private BlockingQueue<Record> queue;
  private CountDownLatch completed;
  private int success;
  private int failure;
  private AtomicInteger success_global;
  private AtomicInteger failure_global;
  public Producer(String IPAddr,
      String image_file, BlockingQueue queue, CountDownLatch completed, AtomicInteger success, AtomicInteger failure) {
    this.IPAddr = IPAddr;
    this.image_file = image_file;
    this.queue = queue;
    this.completed = completed;
    this.success_global = success;
    this.failure_global = failure;
  }

  public static String sendPost(DefaultApi api, String img) throws ApiException {
    File image = new File(img);
    AlbumsProfile profile = new AlbumsProfile();
    profile.setArtist("Me");
    profile.setTitle("Some Title");
    profile.setYear("2018");
    ImageMetaData result = api.newAlbum(image, profile);
    return result.getAlbumID();
  }

  public static void sendReaction(LikeApi api, String albumID, String reaction) throws ApiException {
    api.review(reaction, albumID);
  }

  private String getRuntime(DefaultApi api, LikeApi likeApi, String albumID, String reaction) throws InterruptedException{
    long start_time = System.currentTimeMillis();
    String id = "-1";
    String type;
    if(likeApi == null) {
      type = "New Album";
      for(int i = 0; i < 5; i++) {
        try {
          id = sendPost(api, this.image_file);
          success++;
          i = 5;
        } catch (ApiException e) {
          failure++;
          System.out.println(e.getMessage());
          System.err.println("Exception when calling DefaultApi#newAlbum");
          e.printStackTrace();
        }
      }
    }else{
      type = "Reaction";
      for(int i = 0; i < 5; i++) {
        try {
          sendReaction(likeApi, albumID, reaction);
          success++;
          i = 5;
        } catch (ApiException e) {
          failure++;
          System.err.println("Exception when calling LikeApi#review");
          e.printStackTrace();
        }
      }
    }
    long end_time = System.currentTimeMillis();
    Record record = new Record(start_time, end_time, type, 200);
    queue.put(record);
    return id;
  }

  @Override
  public void run(){
    ApiClient client = new ApiClient();
    client.setBasePath(IPAddr);
    DefaultApi apiInstance = new DefaultApi(client);
    LikeApi likeApiInstance = new LikeApi(client);
    for(int i = 0; i < 100; i++) {
      try {
        String id = getRuntime(apiInstance, null, null, null);
        if(id == null){
          throw new InterruptedException();
        }
        getRuntime(null, likeApiInstance, id, "like");
        getRuntime(null, likeApiInstance, id, "like");
        getRuntime(null, likeApiInstance, id, "dislike");
      } catch (InterruptedException e) {
        System.out.println(e.getMessage());
      }
    }
    this.success_global.getAndAdd(success);
    this.failure_global.getAndAdd(failure);
    this.completed.countDown();
  }
}
