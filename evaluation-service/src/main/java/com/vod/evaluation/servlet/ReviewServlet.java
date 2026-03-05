package com.vod.evaluation.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vod.evaluation.entity.Review;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@WebServlet(name = "ReviewServlet", urlPatterns = "/reviews/*")
public class ReviewServlet extends HttpServlet {

    private MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final RestTemplate restTemplate = new RestTemplate();
    private final List<Review> fallbackReviews = new CopyOnWriteArrayList<>();
    private final AtomicLong fallbackIdSequence = new AtomicLong(1);
    private volatile boolean mongoAvailable = true;

    @Override
    public void init() throws ServletException {
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        this.mongoTemplate = ctx.getBean(MongoTemplate.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Review> reviews = findAllReviews();
                objectMapper.writeValue(resp.getWriter(), reviews);
            } else if (pathInfo.startsWith("/movie/")) {
                String[] parts = pathInfo.split("/");
                Long movieId = Long.parseLong(parts[2]);

                if (parts.length == 4 && "average".equals(parts[3])) {
                    // Calculate average
                    List<Review> reviews = findReviewsByMovieId(movieId);
                    double average = reviews.stream()
                            .mapToInt(Review::getRating)
                            .average()
                            .orElse(0.0);
                    resp.getWriter().write(String.valueOf(average));
                } else {
                    // Get reviews for movie
                    List<Review> reviews = findReviewsByMovieId(movieId);
                    objectMapper.writeValue(resp.getWriter(), reviews);
                }
            } else if (pathInfo.startsWith("/user/")) {
                String[] parts = pathInfo.split("/");
                Long userId = Long.parseLong(parts[2]);
                List<Review> reviews = findReviewsByUserId(userId);
                objectMapper.writeValue(resp.getWriter(), reviews);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try {
            Review review = objectMapper.readValue(req.getInputStream(), Review.class);
            if (review.getMovieId() == null || review.getUserId() == null || review.getRating() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Missing required fields\"}");
                return;
            }

            // Check if user has rented the movie
            // Call Film Service API
            String filmServiceUrl = "http://localhost:12082/reservations/check?movieId=" + review.getMovieId()
                    + "&userId=" + review.getUserId();
            Boolean hasRented = restTemplate.getForObject(filmServiceUrl, Boolean.class);

            if (Boolean.TRUE.equals(hasRented)) {
                review.setCreatedAt(LocalDateTime.now());
                saveReview(review);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                objectMapper.writeValue(resp.getWriter(), review);
            } else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("{\"error\": \"User has not rented this movie\"}");
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private List<Review> findAllReviews() {
        if (mongoAvailable) {
            try {
                return mongoTemplate.findAll(Review.class);
            } catch (Exception ignored) {
                mongoAvailable = false;
            }
        }
        return new ArrayList<>(fallbackReviews);
    }

    private List<Review> findReviewsByMovieId(Long movieId) {
        if (mongoAvailable) {
            try {
                Query query = new Query(Criteria.where("movieId").is(movieId));
                return mongoTemplate.find(query, Review.class);
            } catch (Exception ignored) {
                mongoAvailable = false;
            }
        }
        return fallbackReviews.stream()
                .filter(r -> movieId.equals(r.getMovieId()))
                .toList();
    }

    private List<Review> findReviewsByUserId(Long userId) {
        if (mongoAvailable) {
            try {
                Query query = new Query(Criteria.where("userId").is(userId));
                return mongoTemplate.find(query, Review.class);
            } catch (Exception ignored) {
                mongoAvailable = false;
            }
        }
        return fallbackReviews.stream()
                .filter(r -> userId.equals(r.getUserId()))
                .toList();
    }

    private void saveReview(Review review) {
        if (mongoAvailable) {
            try {
                mongoTemplate.save(review);
                return;
            } catch (Exception ignored) {
                mongoAvailable = false;
            }
        }

        if (review.getId() == null || review.getId().isBlank()) {
            review.setId(String.valueOf(fallbackIdSequence.getAndIncrement()));
        }
        fallbackReviews.add(review);
    }
}
