package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.BelongsTo;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks")
public final class Task implements Model {
  public static final QueryField ID = field("id");
  public static final QueryField TITLE = field("title");
  public static final QueryField DESCRIPTION = field("description");
  public static final QueryField STATUS = field("status");
  public static final QueryField ADDRESS = field("address");
  public static final QueryField LATTITUDE = field("lattitude");
  public static final QueryField LONGITUDE = field("longitude");
  public static final QueryField BUCKETKEY = field("bucketkey");
  public static final QueryField TASK_FOR_TEAM = field("taskTaskForTeamId");
  public @ModelField(targetType="ID", isRequired = true) String id;
  public @ModelField(targetType="String", isRequired = true) String title;
  public @ModelField(targetType="String") String description;
  public @ModelField(targetType="String") String status;
  public @ModelField(targetType="String") String address;
  public @ModelField(targetType="Float") Float lattitude;
  public @ModelField(targetType="Float") Float longitude;
  public @ModelField(targetType="String") String bucketkey;
  public @ModelField(targetType="Team") @BelongsTo(targetName = "taskTaskForTeamId", type = Team.class) Team taskForTeam;
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getDescription() {
      return description;
  }
  
  public String getStatus() {
      return status;
  }
  
  public String getAddress() {
      return address;
  }
  
  public Float getLattitude() {
      return lattitude;
  }
  
  public Float getLongitude() {
      return longitude;
  }
  
  public String getBucketkey() {
      return bucketkey;
  }
  
  public Team getTaskForTeam() {
      return taskForTeam;
  }
  
  private Task(String id, String title, String description, String status, String address, Float lattitude, Float longitude, String bucketkey, Team taskForTeam) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.address = address;
    this.lattitude = lattitude;
    this.longitude = longitude;
    this.bucketkey = bucketkey;
    this.taskForTeam = taskForTeam;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Task task = (Task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
              ObjectsCompat.equals(getTitle(), task.getTitle()) &&
              ObjectsCompat.equals(getDescription(), task.getDescription()) &&
              ObjectsCompat.equals(getStatus(), task.getStatus()) &&
              ObjectsCompat.equals(getAddress(), task.getAddress()) &&
              ObjectsCompat.equals(getLattitude(), task.getLattitude()) &&
              ObjectsCompat.equals(getLongitude(), task.getLongitude()) &&
              ObjectsCompat.equals(getBucketkey(), task.getBucketkey()) &&
              ObjectsCompat.equals(getTaskForTeam(), task.getTaskForTeam());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getDescription())
      .append(getStatus())
      .append(getAddress())
      .append(getLattitude())
      .append(getLongitude())
      .append(getBucketkey())
      .append(getTaskForTeam())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Task {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("status=" + String.valueOf(getStatus()) + ", ")
      .append("address=" + String.valueOf(getAddress()) + ", ")
      .append("lattitude=" + String.valueOf(getLattitude()) + ", ")
      .append("longitude=" + String.valueOf(getLongitude()) + ", ")
      .append("bucketkey=" + String.valueOf(getBucketkey()) + ", ")
      .append("taskForTeam=" + String.valueOf(getTaskForTeam()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static Task justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Task(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      description,
      status,
      address,
      lattitude,
      longitude,
      bucketkey,
      taskForTeam);
  }
  public interface TitleStep {
    BuildStep title(String title);
  }
  

  public interface BuildStep {
    Task build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep description(String description);
    BuildStep status(String status);
    BuildStep address(String address);
    BuildStep lattitude(Float lattitude);
    BuildStep longitude(Float longitude);
    BuildStep bucketkey(String bucketkey);
    BuildStep taskForTeam(Team taskForTeam);
  }
  

  public static class Builder implements TitleStep, BuildStep {
    private String id;
    private String title;
    private String description;
    private String status;
    private String address;
    private Float lattitude;
    private Float longitude;
    private String bucketkey;
    private Team taskForTeam;
    @Override
     public Task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Task(
          id,
          title,
          description,
          status,
          address,
          lattitude,
          longitude,
          bucketkey,
          taskForTeam);
    }
    
    @Override
     public BuildStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
     public BuildStep status(String status) {
        this.status = status;
        return this;
    }
    
    @Override
     public BuildStep address(String address) {
        this.address = address;
        return this;
    }
    
    @Override
     public BuildStep lattitude(Float lattitude) {
        this.lattitude = lattitude;
        return this;
    }
    
    @Override
     public BuildStep longitude(Float longitude) {
        this.longitude = longitude;
        return this;
    }
    
    @Override
     public BuildStep bucketkey(String bucketkey) {
        this.bucketkey = bucketkey;
        return this;
    }
    
    @Override
     public BuildStep taskForTeam(Team taskForTeam) {
        this.taskForTeam = taskForTeam;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String description, String status, String address, Float lattitude, Float longitude, String bucketkey, Team taskForTeam) {
      super.id(id);
      super.title(title)
        .description(description)
        .status(status)
        .address(address)
        .lattitude(lattitude)
        .longitude(longitude)
        .bucketkey(bucketkey)
        .taskForTeam(taskForTeam);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder status(String status) {
      return (CopyOfBuilder) super.status(status);
    }
    
    @Override
     public CopyOfBuilder address(String address) {
      return (CopyOfBuilder) super.address(address);
    }
    
    @Override
     public CopyOfBuilder lattitude(Float lattitude) {
      return (CopyOfBuilder) super.lattitude(lattitude);
    }
    
    @Override
     public CopyOfBuilder longitude(Float longitude) {
      return (CopyOfBuilder) super.longitude(longitude);
    }
    
    @Override
     public CopyOfBuilder bucketkey(String bucketkey) {
      return (CopyOfBuilder) super.bucketkey(bucketkey);
    }
    
    @Override
     public CopyOfBuilder taskForTeam(Team taskForTeam) {
      return (CopyOfBuilder) super.taskForTeam(taskForTeam);
    }
  }
  
}
