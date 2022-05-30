package com.creato.Repository;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.creato.Entities.PictureGroupEntity;
import com.creato.Entities.UserEntity;

@Repository
public interface PictureGroupRepository extends JpaRepository<PictureGroupEntity, Long> {
	
	@Query(value = "select grp from PictureGroupEntity grp where grp.createdBy = :username")
	public List<PictureGroupEntity> getPictureByUsername(@Param("username") UserEntity username);
	
	@Query("select grp from PictureGroupEntity grp join UserEntity user on grp.createdBy.id = user.id")
	public List<PictureGroupEntity> getAllImages(PageRequest page);
}
