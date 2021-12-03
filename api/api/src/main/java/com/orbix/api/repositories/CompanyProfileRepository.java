package com.orbix.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orbix.api.models.CompanyProfile;

@Repository
public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, Long> {

	Optional<CompanyProfile> findByCompanyKey(String string);

}
