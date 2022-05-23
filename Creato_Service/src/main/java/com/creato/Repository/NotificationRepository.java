package com.creato.Repository;


import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.creato.Entities.NotificationEntity;

import net.bytebuddy.asm.Advice.OffsetMapping.Sort;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>{
	
	@Query("Select noti from NotificationEntity noti where noti.isRead = 0")
	public List<NotificationEntity> getUnReadNotification();
	
}
