package id.co.tirezone.tirezonemobile;

/**
 * Created by Jessica on 16-Jan-17.
 */

public class Customer {
    private String vehicleid;
    private String name;
    private String phone;
    private String email;
    private String car;
    private String address;
    private String postalCode;

    public Customer() {
    }

    public Customer(String vehicleid, String name, String phone, String email, String car, String address, String postalCode) {
        this.vehicleid = vehicleid;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.car = car;
        this.address = address;
        this.postalCode = postalCode;
    }

    public String getVehicleid() {
        return vehicleid;
    }

    public void setVehicleid(String vehicleid) {
        this.vehicleid = vehicleid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
