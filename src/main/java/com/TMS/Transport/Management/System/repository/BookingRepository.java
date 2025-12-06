package com.TMS.Transport.Management.System.repository;

import com.TMS.Transport.Management.System.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, UUID> {

    List<BookingEntity> findByLoad_LoadId(UUID loadId);

    @Query("SELECT SUM(b.allocatedTrucks) FROM BookingEntity b WHERE b.load.loadId = :loadId AND b.status = 'CONFIRMED'")
    Integer getSumOfAllocatedTrucks(@Param("loadId") UUID loadId);
}
