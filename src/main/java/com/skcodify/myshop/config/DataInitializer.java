package com.skcodify.myshop.config;

import com.skcodify.myshop.domain.*;
import com.skcodify.myshop.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Initializes the database with sample data on application startup.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final DeliveryVehicleRepository deliveryVehicleRepository;
    private final CartRepository cartRepository;
    private final ReviewRepository reviewRepository;
    private final ShopFrontRepository shopFrontRepository;


    public DataInitializer(UserRepository userRepository, 
            ProductRepository productRepository, 
            DeliveryPartnerRepository deliveryPartnerRepository,
            DeliveryVehicleRepository deliveryVehicleRepository, 
            CartRepository cartRepository,
            ReviewRepository reviewRepository,
            ShopFrontRepository shopFrontRepository) {
                
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.deliveryPartnerRepository = deliveryPartnerRepository;
        this.deliveryVehicleRepository = deliveryVehicleRepository;
        this.cartRepository = cartRepository;
        this.reviewRepository = reviewRepository;
        this.shopFrontRepository = shopFrontRepository;
    }

    @Override
    public void run(String... args) {
        logger.info("users: " + userRepository.count());
        logger.info("products: " + productRepository.count());
        logger.info("carts: " + cartRepository.count());
        logger.info("reviews: " + reviewRepository.count());
        logger.info("shopfronts: " + shopFrontRepository.count());


        // Check if data already exists to prevent re-seeding on every restart if not using in-memory DB
        if (userRepository.count() == 0 && productRepository.count() == 0 && cartRepository.count() == 0 && reviewRepository.count() == 0 && shopFrontRepository.count() == 0) {
            logger.info("Database is empty. Seeding with sample data...");
            seedData();
            logger.info("Sample data has been seeded.");
        } else {
            logger.info("Database is not empty. Skipping data seeding.");
        }
    }

    @Transactional
    private void seedData() {
        // Create an Admin
        User admin = new User();
        admin.setName("Admin User");
        admin.setEmail("admin@example.com");
        admin.setPhone("0000000000");
        admin.setUserType(UserType.ADMIN);
        admin.setVerified(true);

        // --- Create Sellers ---
        User seller1 = new User();
        seller1.setName("Alice's Art");
        seller1.setEmail("alice@example.com");
        seller1.setPhone("1234567890");
        seller1.setUserType(UserType.SELLER);
        seller1.setShopName("Alice's Art & Craft");
        seller1.setVerified(true);

        User seller2 = new User();
        seller2.setName("Green Grocers");
        seller2.setEmail("grocer@example.com");
        seller2.setPhone("2345678901");
        seller2.setUserType(UserType.SELLER);
        seller2.setShopName("Green Valley Grocers");
        seller2.setVerified(true);

        User seller3 = new User();
        seller3.setName("Pantry Provisions");
        seller3.setEmail("pantry@example.com");
        seller3.setPhone("3456789012");
        seller3.setUserType(UserType.SELLER);
        seller3.setShopName("The Modern Pantry");
        seller3.setVerified(false);

        // --- Create Buyers ---
        User buyer1 = new User();
        buyer1.setName("Bob Buyer");
        buyer1.setEmail("bob@example.com");
        buyer1.setPhone("0987654321");
        buyer1.setUserType(UserType.BUYER);
        buyer1.setApartmentNumber("A-101");

        User buyer2 = new User();
        buyer2.setName("Diana Prince");
        buyer2.setEmail("diana@example.com");
        buyer2.setPhone("1122334455");
        buyer2.setUserType(UserType.BUYER);
        buyer2.setApartmentNumber("B-202");

        // Create a Delivery Partner User
        User deliveryPartnerUser = new User();
        deliveryPartnerUser.setName("Ravi Kumar");
        deliveryPartnerUser.setEmail("ravi.dp@example.com");
        deliveryPartnerUser.setPhone("9876543210");
        deliveryPartnerUser.setUserType(UserType.DELIVERY_PARTNER);
        deliveryPartnerUser.setVerified(true);

        // Save all users first to get their generated IDs
        userRepository.saveAll(List.of(admin, seller1, seller2, seller3, buyer1, buyer2, deliveryPartnerUser));

        // --- Create Delivery Vehicle and Partner ---

        // Create a Delivery Vehicle
        DeliveryVehicle vehicle = new DeliveryVehicle();
        vehicle.setId("dv-001");
        vehicle.setVehicleNumber("KA-01-AB-1234");
        vehicle.setVehicleType(VehicleType.BIKE);
        deliveryVehicleRepository.save(vehicle);

        // Create a Delivery Partner Profile
        DeliveryPartner deliveryPartner = new DeliveryPartner();
        deliveryPartner.setId("dp-001");
        deliveryPartner.setUser(deliveryPartnerUser);
        deliveryPartner.setName(deliveryPartnerUser.getName());
        deliveryPartner.setPhone(deliveryPartnerUser.getPhone());
        deliveryPartner.setVehicle(vehicle);
        deliveryPartner.setAvailable(true);
        deliveryPartnerRepository.save(deliveryPartner);

        // --- Create Products ---
        List<User> sellers = List.of(seller1, seller2, seller3);
        Random rand = new Random();

        Product product1 = new Product();
        product1.setId("prod-001");
        product1.setName("Fresh Organic Apples");
        product1.setDescription("A bag of 6 crisp and juicy organic apples, fresh from the farm.");
        product1.setPrice(new BigDecimal("4.99"));
        product1.setCategory("Fruits");
        product1.setStock(50);
        product1.setStatus("available");
        product1.setImageUrls(new ArrayList<>(List.of("https://images.unsplash.com/photo-1571771894824-269f85b51a89?q=80&w=2080")));
        User randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product1.setUserId(randomSeller.getId());
        product1.setPostedDate(ZonedDateTime.now().minusDays(2));

        Product product2 = new Product();
        product2.setId("prod-002");
        product2.setName("Artisanal Sourdough Bread");
        product2.setDescription("A freshly baked loaf of artisanal sourdough bread with a crispy crust.");
        product2.setPrice(new BigDecimal("7.50"));
        product2.setCategory("Bakery");
        product2.setStock(20);
        product2.setStatus("available");
        product2.setImageUrls(new ArrayList<>(List.of("https://images.unsplash.com/photo-1598373182133-52452f741e1d?q=80&w=2070")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product2.setUserId(randomSeller.getId());
        product2.setPostedDate(ZonedDateTime.now().minusDays(1));

        // --- More Food Products ---

        Product product3 = new Product();
        product3.setId("prod-003");
        product3.setName("Organic Tomatoes");
        product3.setDescription("A pound of fresh, ripe organic tomatoes on the vine.");
        product3.setPrice(new BigDecimal("3.99"));
        product3.setCategory("Vegetables");
        product3.setStock(40);
        product3.setStatus("available");
        product3.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1597498178148-94e034b933e2?q=80&w=2070", "https://images.unsplash.com/photo-1615485925575-b0354a433921?q=80&w=1974")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product3.setUserId(randomSeller.getId());
        product3.setPostedDate(ZonedDateTime.now().minusDays(3));

        Product product4 = new Product();
        product4.setId("prod-004");
        product4.setName("Fresh Carrots");
        product4.setDescription("A bunch of sweet and crunchy organic carrots.");
        product4.setPrice(new BigDecimal("2.49"));
        product4.setCategory("Vegetables");
        product4.setStock(60);
        product4.setStatus("available");
        product4.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1598170845058-32fe411e85bb?q=80&w=1974", "https://images.unsplash.com/photo-1589923188900-85dae523342d?q=80&w=2070")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product4.setUserId(randomSeller.getId());
        product4.setPostedDate(ZonedDateTime.now().minusDays(3));

        Product product5 = new Product();
        product5.setId("prod-005");
        product5.setName("Broccoli Florets");
        product5.setDescription("A bag of fresh broccoli florets, ready to cook.");
        product5.setPrice(new BigDecimal("3.29"));
        product5.setCategory("Vegetables");
        product5.setStock(30);
        product5.setStatus("available");
        product5.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1540914124281-3425879413d5?q=80&w=2070", "https://images.unsplash.com/photo-1628779238953-a057a08b5318?q=80&w=2070")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product5.setUserId(randomSeller.getId());
        product5.setPostedDate(ZonedDateTime.now().minusDays(2));

        Product product6 = new Product();
        product6.setId("prod-006");
        product6.setName("Whole Milk (1 Gallon)");
        product6.setDescription("One gallon of fresh, pasteurized whole milk.");
        product6.setPrice(new BigDecimal("5.99"));
        product6.setCategory("Dairy");
        product6.setStock(25);
        product6.setStatus("available");
        product6.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1550583724-b2692b2ae38c?q=80&w=1964", "https://images.unsplash.com/photo-1634232777388-a2b3e3da784d?q=80&w=1974")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product6.setUserId(randomSeller.getId());
        product6.setPostedDate(ZonedDateTime.now().minusDays(1));

        Product product7 = new Product();
        product7.setId("prod-007");
        product7.setName("Greek Yogurt");
        product7.setDescription("A container of plain, unsweetened Greek yogurt.");
        product7.setPrice(new BigDecimal("4.50"));
        product7.setCategory("Dairy");
        product7.setStock(35);
        product7.setStatus("available");
        product7.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1632432499343-347a58323319?q=80&w=1974", "https://images.unsplash.com/photo-1559948274-53f875b41935?q=80&w=2070")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product7.setUserId(randomSeller.getId());
        product7.setPostedDate(ZonedDateTime.now().minusDays(4));

        Product product8 = new Product();
        product8.setId("prod-008");
        product8.setName("Sharp Cheddar Cheese Block");
        product8.setDescription("An 8oz block of aged sharp cheddar cheese.");
        product8.setPrice(new BigDecimal("6.25"));
        product8.setCategory("Dairy");
        product8.setStock(40);
        product8.setStatus("available");
        product8.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1619860861573-a33d6b6c1643?q=80&w=1964", "https://images.unsplash.com/photo-1589881133825-bbb3b9471b6c?q=80&w=2070")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product8.setUserId(randomSeller.getId());
        product8.setPostedDate(ZonedDateTime.now().minusDays(5));

        Product product9 = new Product();
        product9.setId("prod-009");
        product9.setName("Free-Range Chicken Breast");
        product9.setDescription("Two pounds of boneless, skinless free-range chicken breast.");
        product9.setPrice(new BigDecimal("12.99"));
        product9.setCategory("Meat");
        product9.setStock(15);
        product9.setStatus("available");
        product9.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1604503468825-a2744650151c?q=80&w=1974", "https://images.unsplash.com/photo-1587593810167-a84920ea0781?q=80&w=2070")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product9.setUserId(randomSeller.getId());
        product9.setPostedDate(ZonedDateTime.now().minusDays(1));

        Product product10 = new Product();
        product10.setId("prod-010");
        product10.setName("Wild-Caught Salmon Fillet");
        product10.setDescription("A one-pound fillet of fresh, wild-caught salmon.");
        product10.setPrice(new BigDecimal("18.50"));
        product10.setCategory("Seafood");
        product10.setStock(10);
        product10.setStatus("available");
        product10.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1599043513900-ed6fe01d3833?q=80&w=2070", "https://images.unsplash.com/photo-1615141982483-18ce1a3579d4?q=80&w=1970")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product10.setUserId(randomSeller.getId());
        product10.setPostedDate(ZonedDateTime.now().minusDays(2));

        Product product11 = new Product();
        product11.setId("prod-011");
        product11.setName("Organic Quinoa");
        product11.setDescription("A 2lb bag of organic white quinoa.");
        product11.setPrice(new BigDecimal("9.99"));
        product11.setCategory("Grains");
        product11.setStock(50);
        product11.setStatus("available");
        product11.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1516684732162-798a0062be99?q=80&w=2070", "https://images.unsplash.com/photo-1603631240313-aa345348b57c?q=80&w=1964")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product11.setUserId(randomSeller.getId());
        product11.setPostedDate(ZonedDateTime.now().minusDays(10));

        Product product12 = new Product();
        product12.setId("prod-012");
        product12.setName("Spaghetti Pasta");
        product12.setDescription("A 1lb box of classic spaghetti pasta.");
        product12.setPrice(new BigDecimal("1.99"));
        product12.setCategory("Grains");
        product12.setStock(100);
        product12.setStatus("available");
        product12.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1587874522923-853b01c3d377?q=80&w=1974", "https://images.unsplash.com/photo-1621996346565-e326e2021e3a?q=80&w=2070")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product12.setUserId(randomSeller.getId());
        product12.setPostedDate(ZonedDateTime.now().minusDays(15));

        Product product13 = new Product();
        product13.setId("prod-013");
        product13.setName("Classic Potato Chips");
        product13.setDescription("A large bag of classic salted potato chips.");
        product13.setPrice(new BigDecimal("3.79"));
        product13.setCategory("Snacks");
        product13.setStock(75);
        product13.setStatus("available");
        product13.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1541592106381-b31e9677c0e5?q=80&w=1974", "https://images.unsplash.com/photo-1613919113643-2515a2b397a3?q=80&w=1974")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product13.setUserId(randomSeller.getId());
        product13.setPostedDate(ZonedDateTime.now().minusDays(5));

        Product product14 = new Product();
        product14.setId("prod-014");
        product14.setName("Dark Chocolate Bar (70%)");
        product14.setDescription("A 3.5oz bar of rich, 70% cacao dark chocolate.");
        product14.setPrice(new BigDecimal("3.49"));
        product14.setCategory("Snacks");
        product14.setStock(60);
        product14.setStatus("available");
        product14.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1603203093831-d43431479159?q=80&w=2070", "https://images.unsplash.com/photo-1511381939415-e340a6479939?q=80&w=1974")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product14.setUserId(randomSeller.getId());
        product14.setPostedDate(ZonedDateTime.now().minusDays(8));

        Product product15 = new Product();
        product15.setId("prod-015");
        product15.setName("Freshly Squeezed Orange Juice");
        product15.setDescription("A half-gallon of freshly squeezed orange juice, not from concentrate.");
        product15.setPrice(new BigDecimal("6.99"));
        product15.setCategory("Drinks");
        product15.setStock(20);
        product15.setStatus("available");
        product15.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1600271886742-f049cd451bba?q=80&w=1974", "https://images.unsplash.com/photo-1577685948902-a3d7d8e53b23?q=80&w=1974")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product15.setUserId(randomSeller.getId());
        product15.setPostedDate(ZonedDateTime.now().minusDays(1));

        Product product16 = new Product();
        product16.setId("prod-016");
        product16.setName("Whole Bean Coffee");
        product16.setDescription("A 12oz bag of medium roast whole bean arabica coffee.");
        product16.setPrice(new BigDecimal("14.99"));
        product16.setCategory("Drinks");
        product16.setStock(40);
        product16.setStatus("available");
        product16.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1511920183353-3c7c9c5285d8?q=80&w=1974", "https://images.unsplash.com/photo-1599160219458-9f1529eb64a7?q=80&w=2070")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product16.setUserId(randomSeller.getId());
        product16.setPostedDate(ZonedDateTime.now().minusDays(20));

        Product product17 = new Product();
        product17.setId("prod-017");
        product17.setName("Canned Diced Tomatoes");
        product17.setDescription("A 28oz can of fire-roasted diced tomatoes.");
        product17.setPrice(new BigDecimal("2.29"));
        product17.setCategory("Canned Goods");
        product17.setStock(80);
        product17.setStatus("available");
        product17.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1580992847833-a39c4a751252?q=80&w=2070", "https://images.unsplash.com/photo-1576045057995-568f588f82fb?q=80&w=1974")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product17.setUserId(randomSeller.getId());
        product17.setPostedDate(ZonedDateTime.now().minusDays(30));

        Product product18 = new Product();
        product18.setId("prod-018");
        product18.setName("Canned Chickpeas");
        product18.setDescription("A 15.5oz can of organic chickpeas (garbanzo beans).");
        product18.setPrice(new BigDecimal("1.79"));
        product18.setCategory("Canned Goods");
        product18.setStock(120);
        product18.setStatus("available");
        product18.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1503524279369-97d2f82d4689?q=80&w=1974", "https://images.unsplash.com/photo-1558393393-99c961a35979?q=80&w=1974")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product18.setUserId(randomSeller.getId());
        product18.setPostedDate(ZonedDateTime.now().minusDays(30));

        Product product19 = new Product();
        product19.setId("prod-019");
        product19.setName("Cage-Free Eggs (Dozen)");
        product19.setDescription("A dozen large, brown, cage-free eggs.");
        product19.setPrice(new BigDecimal("4.99"));
        product19.setCategory("Dairy");
        product19.setStock(30);
        product19.setStatus("available");
        product19.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1598965675045-a5b542544120?q=80&w=2070", "https://images.unsplash.com/photo-1519996529931-28324d5a630e?q=80&w=1974")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product19.setUserId(randomSeller.getId());
        product19.setPostedDate(ZonedDateTime.now().minusDays(1));

        Product product20 = new Product();
        product20.setId("prod-020");
        product20.setName("Almond Milk");
        product20.setDescription("A half-gallon of unsweetened vanilla almond milk.");
        product20.setPrice(new BigDecimal("4.29"));
        product20.setCategory("Drinks");
        product20.setStock(25);
        product20.setStatus("available");
        product20.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1563180226-e09f5b54950a?q=80&w=1974", "https://images.unsplash.com/photo-1619797367841-6a7550875b12?q=80&w=1974")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product20.setUserId(randomSeller.getId());
        product20.setPostedDate(ZonedDateTime.now().minusDays(6));

        Product product21 = new Product();
        product21.setId("prod-021");
        product21.setName("Gluten-Free Rolled Oats");
        product21.setDescription("A 32oz container of gluten-free old-fashioned rolled oats.");
        product21.setPrice(new BigDecimal("8.49"));
        product21.setCategory("Grains");
        product21.setStock(45);
        product21.setStatus("available");
        product21.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1607582280932-d89242d86217?q=80&w=1974", "https://images.unsplash.com/photo-1532597411303-5ff131a5dcd3?q=80&w=1974")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product21.setUserId(randomSeller.getId());
        product21.setPostedDate(ZonedDateTime.now().minusDays(12));

        Product product22 = new Product();
        product22.setId("prod-022");
        product22.setName("Extra Virgin Olive Oil");
        product22.setDescription("A 500ml bottle of cold-pressed extra virgin olive oil.");
        product22.setPrice(new BigDecimal("11.99"));
        product22.setCategory("Pantry");
        product22.setStock(30);
        product22.setStatus("available");
        product22.setImageUrls(new ArrayList<>(Arrays.asList("https://images.unsplash.com/photo-1572481492584-35b7f353c159?q=80&w=1974", "https://images.unsplash.com/photo-1626082626944-805f45317c69?q=80&w=1964")));
        randomSeller = sellers.get(rand.nextInt(sellers.size()));
        product22.setUserId(randomSeller.getId());
        product22.setPostedDate(ZonedDateTime.now().minusDays(25));

        productRepository.saveAll(List.of(product1, product2, product3, product4, product5, product6, product7, product8, product9, product10, product11, product12, product13, product14, product15, product16, product17, product18, product19, product20, product21, product22));
    
        // --- Create a Cart for a Buyer ---
        logger.info("Seeding cart data for a sample user...");
        Cart cartForBuyer1 = new Cart();
        cartForBuyer1.setUser(buyer1);

        CartItem cartItem1 = new CartItem();
        cartItem1.setProduct(product1); // Fresh Organic Apples
        cartItem1.setQuantity(2);
        cartForBuyer1.addCartItem(cartItem1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setProduct(product2); // Artisanal Sourdough Bread
        cartItem2.setQuantity(1);
        cartForBuyer1.addCartItem(cartItem2);

        cartRepository.save(cartForBuyer1);

        // --- Create Reviews for Products ---
        logger.info("Seeding review data for sample products...");
        Review review1 = new Review();
        review1.setId("rev-001");
        review1.setProduct(product1); // Apples
        review1.setUser(buyer2); // Diana Prince
        review1.setRating(5);
        review1.setComment("These apples are incredibly crisp and fresh! Best I've had in a while.");
        review1.setReviewDate(ZonedDateTime.now().minusDays(1));

        Review review2 = new Review();
        review2.setId("rev-002");
        review2.setProduct(product3); // Tomatoes
        review2.setUser(buyer1); // Bob Buyer
        review2.setRating(4);
        review2.setComment("Great tomatoes, perfect for salads. A bit pricey but worth it for the quality.");
        review2.setReviewDate(ZonedDateTime.now().minusHours(5));
        reviewRepository.saveAll(List.of(review1, review2));

        // --- Create a Shop Front for a Seller ---
        logger.info("Seeding shop front data for a sample seller...");
        ShopFront shopFront1 = new ShopFront();
        shopFront1.setUser(seller1); // Alice's Art
        shopFront1.setBannerImageUrl("https://images.unsplash.com/photo-1531973576160-7125cd663d86?q=80&w=2070");
        shopFront1.setProfileImageUrl("https://images.unsplash.com/photo-1579783902614-a3fb3927b6a5?q=80&w=1945");
        shopFront1.setShopTagline("Handcrafted wonders for your home.");
        shopFront1.setThemeColor("#E9D8A6"); // A nice beige color
        shopFront1.setSocialMediaLinks(List.of("https://instagram.com/alicesart", "https://facebook.com/alicesart"));
        shopFrontRepository.save(shopFront1);
    }


    
}