package api.payload;

public class Bookingdates {
    private String checkin;
    private String checkout;

    // Default Constructor
    public Bookingdates() {}

    // Getters and Setters
    public String getCheckin() { return checkin; }
    public void setCheckin(String checkin) { this.checkin = checkin; }

    public String getCheckout() { return checkout; }
    public void setCheckout(String checkout) { this.checkout = checkout; }
}
