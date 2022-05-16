package com.creato.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.creato.Entities.LookupEntity;

@Repository
public interface LookupRepository extends JpaRepository<LookupEntity, Integer> {

	@Query("select lookup from LookupEntity lookup where lookup.lookupName = :lookupName and lookup.status = 1")
	public LookupEntity findLookupData(@Param("lookupName") String lookupName);
}
