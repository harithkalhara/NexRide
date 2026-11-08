package com.nexride.repository;

import com.nexride.model.*;
import java.util.*;
import java.util.function.Function;

public interface StateRepository {
    <T> T read(Function<State, T> action);
    <T> T update(Function<State, T> action);

    class State {
        private final List<Invoice> invoices = new ArrayList<>();
        private final List<Payment> payments = new ArrayList<>();
        private final List<CheckoutAttempt> checkoutAttempts = new ArrayList<>();
        private final List<User> users = new ArrayList<>();
        private final SiteSettings settings = new SiteSettings();

        public List<Invoice> getInvoices() { return invoices; }
        public List<Payment> getPayments() { return payments; }
        public List<CheckoutAttempt> getCheckoutAttempts() { return checkoutAttempts; }
        public List<User> getUsers() { return users; }
        public SiteSettings getSettings() { return settings; }
    }
}
