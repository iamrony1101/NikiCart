package com.ecommerce.NikiCart.service;

import com.ecommerce.NikiCart.DTO.CategoryDTO;
import com.ecommerce.NikiCart.DTO.CategoryResponse;
import com.ecommerce.NikiCart.DTO.ProductResponse;
import com.ecommerce.NikiCart.exceptions.APIException;
import com.ecommerce.NikiCart.exceptions.ResourceNotFoundException;
import com.ecommerce.NikiCart.model.Category;
import com.ecommerce.NikiCart.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private ModelMapper modelMapper;


    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categories = categoryPage.getContent();
        if(categories.isEmpty()){
            throw new APIException("No Category exist");
        }
        else {
            List<CategoryDTO> categoryDTOS = categories.stream()
                    .map(category -> modelMapper.map(category, CategoryDTO.class))
                    .toList();

            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setContent(categoryDTOS);
            categoryResponse.setPageNumber(categoryPage.getNumber());
            categoryResponse.setPageSize(categoryPage.getSize());
            categoryResponse.setTotalElements(categoryPage.getTotalElements());
            categoryResponse.setTotalPages(categoryPage.getTotalPages());
            categoryResponse.setLastPage(categoryPage.isLast());
            return categoryResponse;
        }
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category savedCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if(savedCategory !=null){
            throw new APIException("Category with the name " + categoryDTO.getCategoryName() + " already exist ! ! !");
        }
        else {
           Category category1=  categoryRepository.save(category);
           CategoryDTO categoryDTO1 = modelMapper.map(category1, CategoryDTO.class);
           return categoryDTO1;
        }

    }

    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category newCategory = categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", id));

        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(id);
        newCategory = categoryRepository.save(category);
        return modelMapper.map(newCategory, CategoryDTO.class);
    }

    public CategoryDTO deleteCategory(Long id) {
        Category delete = categoryRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", id));
        categoryRepository.delete(delete);
        return modelMapper.map(delete, CategoryDTO.class);
    }


}
