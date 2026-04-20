package io.project.backend.domain.reservation.entity;

import io.project.backend.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meeting_rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingRoom extends BaseTimeEntity {

  @Column(nullable = false, unique = true, length = 100)
  private String name;

  @Column(nullable = false, length = 200)
  private String location;

  @Column(name = "is_active", nullable = false)
  private boolean active = true;

  @Builder
  private MeetingRoom(String name, String location) {
    this.name = name;
    this.location = location;
    this.active = true;
  }

  public void updateInfo(String name, String location, boolean active) {
    this.name = name;
    this.location = location;
    this.active = active;
  }
}
