package cinema.controllers;

import cinema.exceptions.ErrorsMessage;
import cinema.models.Seat;
import cinema.models.Cinema;
import cinema.models.Stats;
import cinema.models.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@Component
public class CinemaController {

    @Autowired
    private Cinema cinema;

    @Autowired
    private Stats stats;

    @GetMapping("/seats")
    public Cinema returnSeats() {
        return cinema;
    }

    @PostMapping(path = "/purchase", produces = "application/json")
    public ResponseEntity<?> purchaseSeat(@RequestBody Seat seat) {
        if (seat.getRow() > 9 || seat.getColumn() > 9 ||
                seat.getRow() < 1 || seat.getColumn() < 1) {
            return new ResponseEntity<>(ErrorsMessage.outOfBounds(),
                    HttpStatus.BAD_REQUEST);
        }

        Seat currentSeat = cinema.findSeatByParameter(
                availSeat -> availSeat.getRow() == seat.getRow()
                        && availSeat.getColumn() == seat.getColumn());

        if (currentSeat.isBooked()) {
            return new ResponseEntity<>(ErrorsMessage.alreadyPurchased(),
                    HttpStatus.BAD_REQUEST);
        }

        currentSeat.setBooked(true);
        return new ResponseEntity<>("{\"token\": "
                + "\"" + currentSeat.getToken() + "\",\n"
                + "\"ticket\":{"
                + "\"row\": " + currentSeat.getRow() + ",\n"
                + "\t\t\"column\": " + currentSeat.getColumn() + ",\n"
                + "\t\t\"price\": " + currentSeat.getPrice()
                + "}}",
                HttpStatus.OK);
    }

    @PostMapping(path = "/return", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> returnTicket(@RequestBody Token token) {
        Seat currentSeat;
        try {
            currentSeat = cinema.findSeatByParameter(
                    availSeat -> availSeat.getToken().equals(token.getToken()));
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(ErrorsMessage.wrongToken(), HttpStatus.BAD_REQUEST);
        }

        currentSeat.setBooked(false);
        return new ResponseEntity<>("{\"returned_ticket\": {"
                + "\"row\": " + currentSeat.getRow() + ",\n"
                + "\t\t\"column\": " + currentSeat.getColumn() + ",\n"
                + "\t\t\"price\": " + currentSeat.getPrice()
                + "}}",
                HttpStatus.OK);

    }

    @PostMapping(path = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> returnStats(@RequestParam(required = false) @RequestBody String  password) {
        if (password == null || !password.equals("super_secret")) {
            return new ResponseEntity<>(ErrorsMessage.wrongPassword(), HttpStatus.UNAUTHORIZED);
        }

        stats.setCurrent_income(cinema.getAvailable_seats()
                .stream()
                .filter(Seat::isBooked)
                .mapToInt(Seat::getPrice)
                .sum());
        stats.setNumber_of_available_seats(cinema.getAvailable_seats()
                .stream()
                .filter(seat->!seat.isBooked())
                .count());
        stats.setNumber_of_purchased_tickets(cinema.getAvailable_seats()
                .stream()
                .filter(Seat::isBooked)
                .count());
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
}
