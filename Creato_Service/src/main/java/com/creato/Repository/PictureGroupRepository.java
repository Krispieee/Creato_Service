package com.creato.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.creato.Entities.PictureGroupEntity;

@Repository
public interface PictureGroupRepository extends JpaRepository<PictureGroupEntity, Long> {

}
