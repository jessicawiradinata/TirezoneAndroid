package id.co.tirezone.tirezonemobile;

/**
 * Created by Jessica on 25-Jan-17.
 */

public class Sales {
    private String invoiceno;
    private String date;
    private String notes;
    private String technician;
    private int mileage;
    private String cartkey;
    private String customerkey;

    public Sales() {
    }

    public Sales(String invoiceno, String date, String notes, String technician, int mileage, String cartkey, String customerkey) {
        this.invoiceno = invoiceno;
        this.date = date;
        this.notes = notes;
        this.technician = technician;
        this.mileage = mileage;
        this.cartkey = cartkey;
        this.customerkey = customerkey;
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public String getCartkey() {
        return cartkey;
    }

    public void setCartkey(String cartkey) {
        this.cartkey = cartkey;
    }

    public String getCustomerkey() {
        return customerkey;
    }

    public void setCustomerkey(String customerkey) {
        this.customerkey = customerkey;
    }
}
