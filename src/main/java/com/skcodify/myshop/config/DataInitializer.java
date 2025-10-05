package com.skcodify.myshop.config;

import com.skcodify.myshop.domain.Product;
import com.skcodify.myshop.domain.User;
import com.skcodify.myshop.domain.UserType;
import com.skcodify.myshop.repository.ProductRepository;
import com.skcodify.myshop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Initializes the database with sample data on application startup.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public DataInitializer(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0 && productRepository.count() == 0) {
            logger.info("Database is empty. Seeding with sample data...");
            seedUsersAndProducts();
            logger.info("Sample data has been seeded.");
        } else {
            logger.info("Database is not empty. Skipping data seeding.");
        }
    }

    private void seedUsersAndProducts() {
        // Create a Seller
        User seller = new User();
        seller.setName("Alice's Art");
        seller.setEmail("alice@example.com");
        seller.setPhone("1234567890");
        seller.setUserType(UserType.SELLER);
        seller.setShopName("Alice's Art & Craft");
        seller.setVerified(true);

        // Create a Buyer
        User buyer = new User();
        buyer.setName("Bob Buyer");
        buyer.setEmail("bob@example.com");
        buyer.setPhone("0987654321");
        buyer.setUserType(UserType.BUYER);
        buyer.setApartmentNumber("A-101");

        userRepository.saveAll(List.of(seller, buyer));

        // Create Products for the Seller
        Product product1 = new Product();
        product1.setId("prod-001");
        product1.setName("Handmade Ceramic Mug");
        product1.setDescription("A beautiful, one-of-a-kind ceramic mug, perfect for your morning coffee.");
        product1.setPrice(new BigDecimal("25.50"));
        product1.setCategory("Pottery");
        product1.setStock(15);
        product1.setStatus("available");
        product1.setImageUrls(List.of("https://via.placeholder.com/300/FFC107/000000?Text=Mug+1"));
        product1.setSeller(seller);
        product1.setPostedDate(ZonedDateTime.now().minusDays(10));

        Product product2 = new Product();
        product2.setId("prod-002");
        product2.setName("Abstract Canvas Painting");
        product2.setDescription("A vibrant 24x36 inch abstract painting to brighten up any room.");
        product2.setPrice(new BigDecimal("150.00"));
        product2.setCategory("Art");
        product2.setStock(5);
        product2.setStatus("available");
        product2.setImageUrls(Arrays.asList(
                "https://via.placeholder.com/300/00BCD4/FFFFFF?Text=Painting+1",
                "https://via.placeholder.com/300/009688/FFFFFF?Text=Painting+2"
        ));
        product2.setSeller(seller);
        product2.setPostedDate(ZonedDateTime.now().minusDays(9));

        productRepository.saveAll(List.of(product1, product2));
    }
}