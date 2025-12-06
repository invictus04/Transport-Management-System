package com.TMS.Transport.Management.System.service.implementation;

import com.TMS.Transport.Management.System.dto.BookingDto;
import com.TMS.Transport.Management.System.entity.*;
import com.TMS.Transport.Management.System.entity.enums.BidStatus;
import com.TMS.Transport.Management.System.entity.enums.BookingStatus;
import com.TMS.Transport.Management.System.entity.enums.LoadStatus;
import com.TMS.Transport.Management.System.exception.InsufficientCapacityException;
import com.TMS.Transport.Management.System.exception.InvalidStatusTransitionException;
import com.TMS.Transport.Management.System.exception.ResourceNotFoundException;
import com.TMS.Transport.Management.System.repository.BidRepository;
import com.TMS.Transport.Management.System.repository.BookingRepository;
import com.TMS.Transport.Management.System.repository.LoadRepository;
import com.TMS.Transport.Management.System.repository.TransporterRepository;
import com.TMS.Transport.Management.System.service.interfaces.BookingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BidRepository bidRepository;
    private final LoadRepository loadRepository;
    private final TransporterRepository transporterRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingDto creatingBooking(BookingDto bookingDto) {
        BidEntity bid = bidRepository.findById(bookingDto.getBid().getBidId())
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found: " + bookingDto.getBid().getBidId()));

        LoadEntity load = bid.getLoad();

        TransporterEntity transporter = bid.getTransporter();

        if (bid.getStatus() != BidStatus.PENDING) {
            throw new InvalidStatusTransitionException("Bid is not in PENDING state.");
        }
        if (load.getStatus() == LoadStatus.BOOKED || load.getStatus() == LoadStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("Load is already closed.");
        }

        Integer currentlyAllocated = bookingRepository.getSumOfAllocatedTrucks(load.getLoadId());
        if(currentlyAllocated == null) currentlyAllocated = 0;
        int remainingTrucks = load.getNoOfTrucks() - currentlyAllocated;

        if(bid.getTrucksOffered() > remainingTrucks) {
            throw new InsufficientCapacityException("Load only has " + remainingTrucks + " trucks remaining, but bid offers " + bid.getTrucksOffered());
        }

        boolean truckFound = false;
        for(AvailableTrucks trucks: transporter.getAvailableTrucks()) {
            if(trucks.getTruckType().equalsIgnoreCase(load.getTruckType())) {
                if(trucks.getCount() < bid.getTrucksOffered()) {
                    throw new InsufficientCapacityException("Transporter no longer has enough trucks available.");
                }
                trucks.setCount(trucks.getCount() - bid.getTrucksOffered());
                truckFound = true;
                break;
            }
        }

        if(!truckFound) {
            throw new InsufficientCapacityException("Transporter does not have the required truck type anymore.");
        }
        transporterRepository.save(transporter);

        BookingEntity booking = new BookingEntity();
        booking.setLoad(load);
        booking.setBid(bid);
        booking.setTransporter(transporter);
        booking.setAllocatedTrucks(bid.getTrucksOffered());
        booking.setFinalRate(bid.getProposedRate());
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setBookedAt(LocalDateTime.now());

        bid.setStatus(BidStatus.ACCEPTED);
        bidRepository.save(bid);


        BookingEntity savedBooking = bookingRepository.save(booking);

        int newAllocatedTotal = currentlyAllocated + bid.getTrucksOffered();

        if(newAllocatedTotal >= load.getNoOfTrucks()) {
            load.setStatus(LoadStatus.BOOKED);

            try {
                loadRepository.save(load);
            } catch (ObjectOptimisticLockingFailureException e) {
                throw  e;
            }
        }

        return modelMapper.map(savedBooking, BookingDto.class);
    }

    @Override
    public BookingDto getBookingById(UUID bookingId) {
        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));
        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    @Transactional
    public void cancelBooking(UUID bookingId) {

        BookingEntity booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + bookingId));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("Booking is already cancelled.");
        }

        LoadEntity load = booking.getLoad();
        TransporterEntity transporter = booking.getTransporter();

        for (AvailableTrucks truck : transporter.getAvailableTrucks()) {
            if (truck.getTruckType().equalsIgnoreCase(load.getTruckType())) {
                truck.setCount(truck.getCount() + booking.getAllocatedTrucks());
                break;
            }
        }
        transporterRepository.save(transporter);

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        if (load.getStatus() == LoadStatus.BOOKED) {
            load.setStatus(LoadStatus.OPEN_FOR_BIDS);
            loadRepository.save(load);
        }

    }
}
