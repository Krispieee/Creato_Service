package com.creato.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.creato.Entities.PictureEntity;

public interface PictureRepository extends JpaRepository<PictureEntity, Long> {

}
