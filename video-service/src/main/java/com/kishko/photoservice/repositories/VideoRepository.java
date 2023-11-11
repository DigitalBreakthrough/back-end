package com.kishko.photoservice.repositories;

import com.kishko.photoservice.entities.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, String> {

}
