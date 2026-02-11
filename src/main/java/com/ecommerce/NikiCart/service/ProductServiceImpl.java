package com.ecommerce.NikiCart.service;

import com.ecommerce.NikiCart.DTO.ProductDTO;
import com.ecommerce.NikiCart.DTO.ProductResponse;
import com.ecommerce.NikiCart.exceptions.APIException;
import com.ecommerce.NikiCart.exceptions.ResourceNotFoundException;
import com.ecommerce.NikiCart.model.Category;
import com.ecommerce.NikiCart.model.Product;
import com.ecommerce.NikiCart.repository.CategoryRepository;
import com.ecommerce.NikiCart.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, Product product) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        product.setImage("Default.png");
        product.setCategory(category);

        double specialPrice = product.getPrice()-((product.getDiscount()*0.01) * product.getPrice());

        product.setSpecialPrice(specialPrice);
        productRepository.save(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products= productRepository.findAll();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category categories = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));

        List<Product> products = productRepository.findByCategoryCategoryId(categoryId);

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        if(products.isEmpty()){
            throw new ResourceNotFoundException(
                    "Product", "keyword", keyword
            );
        }

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, Product product) {
     Product product1 = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("product", "productId", productId));

     product1.setProductName(product.getProductName());
     product1.setDescription(product.getDescription());
     product1.setQuantity(product.getQuantity());
     product1.setDiscount(product.getDiscount());
     product1.setPrice(product.getPrice());
     double specialPrice = product.getPrice()-((product.getDiscount()*0.01) * product.getPrice());
     product1.setSpecialPrice(specialPrice);
     Product savedProduct= productRepository.save(product1);
     return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Producct", "productId", productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }


}
