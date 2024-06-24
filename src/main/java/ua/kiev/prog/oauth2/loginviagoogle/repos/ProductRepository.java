package ua.kiev.prog.oauth2.loginviagoogle.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kiev.prog.oauth2.loginviagoogle.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}