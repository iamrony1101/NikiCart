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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        boolean exist = productRepository.existsByProductNameIgnoreCase(productDTO.getProductName());
        if(exist){
            throw new APIException("Product already exist with the given name " + productDTO.getProductName());
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Product product = modelMapper.map(productDTO, Product.class);

        product.setImage("Default.png");
        product.setCategory(category);

        double specialPrice = product.getPrice()-((product.getDiscount()*0.01) * product.getPrice());

        product.setSpecialPrice(specialPrice);
       Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }



    @Override
    public ProductResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder) {

        if(pageNumber<=0){
            throw new APIException("Page Number not valid");
        }
        Pageable pageDetails = PageRequest.of(
                pageNumber-1,
                pageSize,
                Sort.by(sortOrder.equalsIgnoreCase("asc") ?
                        Sort.Direction.ASC : Sort.Direction.DESC, sortBy)
                );
        Page<Product> productPage= productRepository.findAll(pageDetails);
        List<Product> products = productPage.getContent();

        if(products.isEmpty())
            throw  new APIException("No Product Exist");

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPage.getNumber()+1);
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId,int pageNumber, int pageSize, String sortBy, String sortOrder) {

        if(pageNumber<=0){
            throw new APIException("Page Number not valid");
        }
        Pageable pageDetails = PageRequest.of(
                pageNumber-1,
                pageSize,
                Sort.by(sortOrder.equalsIgnoreCase("asc")
                        ?
                        Sort.Direction.ASC : Sort.Direction.DESC, sortBy)
        );

        Category categories = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        Page<Product> products = productRepository.findByCategoryCategoryId(categoryId, pageDetails);

        if (products.getContent().isEmpty()) {
            throw new ResourceNotFoundException("Product", "categoryId", categoryId);
        }

        List<ProductDTO> productDTOS = products.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(products.getNumber()+1);
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLastPage(products.isLast());
        return productResponse;
    }
    @Override
    public ProductResponse getProductsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder) {

        if(pageNumber<=0){
            throw new APIException("Page Number not valid"+pageNumber);
        }
        Pageable pageable = PageRequest.of(
                pageNumber-1,
                pageSize,
                Sort.by(sortOrder.equalsIgnoreCase("ASC") ?
                        Sort.Direction.ASC : Sort.Direction.DESC, sortBy)
        );
        Page<Product> products = productRepository.findByProductNameLikeIgnoreCase("%" + keyword + "%", pageable);
        if(products.getContent().isEmpty()){
            throw new ResourceNotFoundException(
                    "Product", "keyword", keyword
            );
        }

        List<ProductDTO> productDTOS = products.getContent().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(products.getNumber()+1);
        productResponse.setPageSize(products.getSize());
        productResponse.setTotalElements(products.getTotalElements());
        productResponse.setTotalPages(products.getTotalPages());
        productResponse.setLastPage(products.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
     Product product1 = productRepository.findById(productId)
             .orElseThrow(()-> new ResourceNotFoundException("product", "productId", productId));

     Product product = modelMapper.map(productDTO, Product.class);

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
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        String fileName = fileService.uploadImage(path, image);

        productFromDB.setImage(fileName);

        Product updatedProduct = productRepository.save(productFromDB);

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }




}
