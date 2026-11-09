package com.nexride;
import static org.junit.jupiter.api.Assertions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexride.model.Motorcycle;
import com.nexride.model.User;
import com.nexride.repository.FileStateRepository;
import com.nexride.service.RentalService;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
class RentalRequestsSectionTest {
 @TempDir Path temp;
 @Test void customerCanRequestAnAvailableBikeForOneDay() throws Exception {
  var repo = new FileStateRepository(new ObjectMapper(), temp.toString());
  try {
   var customer = new User(); customer.setName("Test Rider");
   var bike = new Motorcycle(); bike.setName("Test Bike"); bike.setDailyRate(2200);
   repo.update(state -> { state.getUsers().add(customer); state.getVehicles().add(bike); return null; });
   String day = LocalDate.now(ZoneId.of("Asia/Colombo")).plusDays(3).toString();
   var rental = new RentalService(repo).request(customer, bike.getId(), day, day, "Test request");
   assertEquals("PENDING", rental.getStatus()); assertEquals(2200, rental.getTotal());
  } finally { repo.close(); }
 }
}
