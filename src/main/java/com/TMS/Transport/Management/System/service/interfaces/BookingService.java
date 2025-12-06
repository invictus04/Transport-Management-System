package com.TMS.Transport.Management.System.service.interfaces;

import com.TMS.Transport.Management.System.dto.BookingDto;

import java.util.UUID;

public interface BookingService {

    BookingDto creatingBooking(BookingDto bookingDto);

    BookingDto getBookingById(UUID bookingId);

    void cancelBooking(UUID bookingId);
}
