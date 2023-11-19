/*
 * Album Store API
 * CS6650 Fall 2023
 *
 * OpenAPI spec version: 1.1
 * Contact: i.gorton@northeasern.edu
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.client.model.AlbumsProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.File;
import java.io.IOException;
/**
 * AlbumsBody
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2023-11-16T01:54:36.218172485Z[GMT]")

public class AlbumsBody {
  @SerializedName("image")
  private File image = null;

  @SerializedName("profile")
  private AlbumsProfile profile = null;

  public AlbumsBody image(File image) {
    this.image = image;
    return this;
  }

   /**
   * Get image
   * @return image
  **/
  @Schema(description = "")
  public File getImage() {
    return image;
  }

  public void setImage(File image) {
    this.image = image;
  }

  public AlbumsBody profile(AlbumsProfile profile) {
    this.profile = profile;
    return this;
  }

   /**
   * Get profile
   * @return profile
  **/
  @Schema(description = "")
  public AlbumsProfile getProfile() {
    return profile;
  }

  public void setProfile(AlbumsProfile profile) {
    this.profile = profile;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AlbumsBody albumsBody = (AlbumsBody) o;
    return Objects.equals(this.image, albumsBody.image) &&
        Objects.equals(this.profile, albumsBody.profile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(Objects.hashCode(image), profile);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AlbumsBody {\n");
    
    sb.append("    image: ").append(toIndentedString(image)).append("\n");
    sb.append("    profile: ").append(toIndentedString(profile)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
