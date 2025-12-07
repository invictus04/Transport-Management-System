package com.TMS.Transport.Management.System.repository;

import com.TMS.Transport.Management.System.entity.LoadEntity;
import com.TMS.Transport.Management.System.entity.enums.LoadStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoadRepository extends JpaRepository<LoadEntity, UUID>, JpaSpecificationExecutor<LoadEntity> {

    Page<LoadEntity> findByShipperIdAndStatus(String shipperId, LoadStatus status, Pageable pageable);

    Page<LoadEntity> findByStatus(LoadStatus status, Pageable pageable);
}
