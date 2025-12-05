package com.TMS.Transport.Management.System.repository;

import com.TMS.Transport.Management.System.entity.TransporterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransporterRepository extends JpaRepository<TransporterEntity, UUID> {
}
