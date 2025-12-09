package com.TMS.Transport.Management.System.controller;

import com.TMS.Transport.Management.System.dto.BookingDto;
import com.TMS.Transport.Management.System.dto.responses.BookingResponseDto;
import com.TMS.Transport.Management.System.service.interfaces.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/booking")
    public ResponseEntity<BookingResponseDto> createBooking(@RequestBody BookingDto bookingDto) {
        BookingResponseDto createdBooking = bookingService.creatingBooking(bookingDto);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<BookingResponseDto> getDetails(@PathVariable UUID bookingId) {
        return ResponseEntity.ok(bookingService.getBookingById(bookingId));
    }

    @PatchMapping("/booking/{bookingId}/cancel")
    public ResponseEntity<String> cancelBooking(@PathVariable UUID bookingId){
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }
}
