package com.nexride.model;

import java.util.ArrayList;
import java.util.List;

/** Root object persisted to nexride-data.txt by FileStateRepository. */
public class AppState {
  private List<User> users = new ArrayList<>();
  private List<Vehicle> vehicles = new ArrayList<>();
  private List<Rental> rentals = new ArrayList<>();
  private List<RideOffer> rides = new ArrayList<>();
  private List<RideBooking> rideBookings = new ArrayList<>();
  private List<Invoice> invoices = new ArrayList<>();
  private List<Payment> payments = new ArrayList<>();
  private List<CheckoutAttempt> checkoutAttempts = new ArrayList<>();
  private List<Issue> issues = new ArrayList<>();
  private List<Review> reviews = new ArrayList<>();
  private SiteSettings settings = new SiteSettings();

  public List<User> getUsers() { return users; }
  public void setUsers(List<User> v) { this.users = v == null ? new ArrayList<>() : v; }

  public List<Vehicle> getVehicles() { return vehicles; }
  public void setVehicles(List<Vehicle> v) { this.vehicles = v == null ? new ArrayList<>() : v; }

  public List<Rental> getRentals() { return rentals; }
  public void setRentals(List<Rental> v) { this.rentals = v == null ? new ArrayList<>() : v; }

  public List<RideOffer> getRides() { return rides; }
  public void setRides(List<RideOffer> v) { this.rides = v == null ? new ArrayList<>() : v; }

  public List<RideBooking> getRideBookings() { return rideBookings; }
  public void setRideBookings(List<RideBooking> v) { this.rideBookings = v == null ? new ArrayList<>() : v; }

  public List<Invoice> getInvoices() { return invoices; }
  public void setInvoices(List<Invoice> v) { this.invoices = v == null ? new ArrayList<>() : v; }

  public List<Payment> getPayments() { return payments; }
  public void setPayments(List<Payment> v) { this.payments = v == null ? new ArrayList<>() : v; }

  public List<CheckoutAttempt> getCheckoutAttempts() { return checkoutAttempts; }
  public void setCheckoutAttempts(List<CheckoutAttempt> v) { this.checkoutAttempts = v == null ? new ArrayList<>() : v; }

  public List<Issue> getIssues() { return issues; }
  public void setIssues(List<Issue> v) { this.issues = v == null ? new ArrayList<>() : v; }

  public List<Review> getReviews() { return reviews; }
  public void setReviews(List<Review> v) { this.reviews = v == null ? new ArrayList<>() : v; }

  public SiteSettings getSettings() { return settings; }
  public void setSettings(SiteSettings v) { this.settings = v == null ? new SiteSettings() : v; }
}
