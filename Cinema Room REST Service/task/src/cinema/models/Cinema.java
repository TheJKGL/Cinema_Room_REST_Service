package cinema.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

@Component
public class Cinema {
    private int total_rows;
    private int total_columns;
    private ArrayList<Seat> available_seats;

    public Cinema(){
        total_rows = 9;
        total_columns = 9;
        available_seats = new ArrayList<>();

        for (int i = 1; i <= getTotal_rows(); i++) {
            for (int j = 1; j <= getTotal_columns(); j++) {
                if (i <= 4) {
                    available_seats.add(new Seat(i, j, 10));
                } else {
                    available_seats.add(new Seat(i, j, 8));
                }
            }
        }
    }

    @JsonIgnore
    public Seat findSeatByParameter(Predicate<Seat> parameter){
        return available_seats
                .stream()
                .filter(parameter)
                .findFirst().orElseThrow(NoSuchElementException::new);
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(int total_rows) {
        this.total_rows = total_rows;
    }

    public int getTotal_columns() {
        return total_columns;
    }

    public void setTotal_columns(int total_columns) {
        this.total_columns = total_columns;
    }

    public ArrayList<Seat> getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(ArrayList<Seat> available_seats) {
        this.available_seats = available_seats;
    }


}