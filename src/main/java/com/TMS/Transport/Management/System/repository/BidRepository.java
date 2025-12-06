package com.TMS.Transport.Management.System.repository;

import com.TMS.Transport.Management.System.entity.BidEntity;
import com.TMS.Transport.Management.System.entity.enums.BidStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BidRepository extends JpaRepository<BidEntity, UUID> {

    List<BidEntity> findByLoad_LoadId(UUID loadId);

    List<BidEntity> findByLoad_LoadIdAndTransporter_TransporterIdAndStatus(UUID loadId, UUID transporterId, BidStatus status);

    boolean existsByLoad_LoadIdAndTransporter_TransporterId(UUID loadId, UUID transporterId);
}
