package cinema.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;

public class Seat {
    private int row;
    private int column;
    private int price;

    @JsonIgnore
    private boolean isPurchased = false;

    @JsonIgnore
    private UUID token;

    public Seat(){
    }

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Seat(int row, int column, int price) {
        this.row = row;
        this.column = column;
        this.price = price;
        this.token = UUID.randomUUID();
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @JsonIgnore
    public UUID getToken() {
        return token;
    }

    @JsonIgnore
    public void setToken(UUID token) {
        this.token = token;
    }

    @JsonIgnore
    public boolean isBooked() {
        return isPurchased;
    }

    @JsonIgnore
    public void setBooked(boolean purchased) {
        isPurchased = purchased;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "row=" + row +
                ", column=" + column +
                ", price=" + price +
                '}';
    }
}
