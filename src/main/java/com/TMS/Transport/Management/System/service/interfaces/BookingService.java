package com.TMS.Transport.Management.System.service.interfaces;

import com.TMS.Transport.Management.System.dto.BookingDto;
import com.TMS.Transport.Management.System.dto.responses.BookingResponseDto;

import java.util.UUID;

public interface BookingService {

    BookingResponseDto creatingBooking(BookingDto bookingDto);

    BookingDto getBookingById(UUID bookingId);

    String cancelBooking(UUID bookingId);
}
