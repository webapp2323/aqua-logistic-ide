package ua.kiev.prog.oauth2.loginviagoogle.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.kiev.prog.oauth2.loginviagoogle.dto.ProductDTO;
import ua.kiev.prog.oauth2.loginviagoogle.repos.ProductRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(product -> new ProductDTO(product.getId(), product.getName(), product.getUnitPrice())).toList();
    }
}
