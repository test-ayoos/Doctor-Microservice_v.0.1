package com.bytatech.ayoos.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bytatech.ayoos.doctor.domain.ProfileInfo;


/**
 * Spring Data  repository for the ProfileInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileInfoRepository extends JpaRepository<ProfileInfo, Long> {

	
}
