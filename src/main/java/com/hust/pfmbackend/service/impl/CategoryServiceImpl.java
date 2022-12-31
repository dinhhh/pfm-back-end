package com.hust.pfmbackend.service.impl;

import com.hust.pfmbackend.entity.Category;
import com.hust.pfmbackend.entity.OperationType;
import com.hust.pfmbackend.entity.User;
import com.hust.pfmbackend.model.request.CategoryRequest;
import com.hust.pfmbackend.model.request.EditCategoryRequest;
import com.hust.pfmbackend.model.response.CategoryResponse;
import com.hust.pfmbackend.repository.CategoryRepository;
import com.hust.pfmbackend.security.jwt.JwtAuthManager;
import com.hust.pfmbackend.service.CategoryService;
import com.hust.pfmbackend.utils.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final Logger LOGGER = LogManager.getLogger(CategoryServiceImpl.class);

    @Autowired
    private JwtAuthManager authManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public boolean save(CategoryRequest request) {
        if (!Validator.isValidObject(request)) {
            LOGGER.error("Request is not valid");
            return false;
        }

        try {
            User user = authManager.getUserByToken();
            String parentCategory = request.getParentCategoryNo();
            String description = request.getDescription();
            Category category = Category.builder()
                    .parentCategoryNo(StringUtils.hasText(parentCategory) ? parentCategory : null)
                    .createOn(new Date(System.currentTimeMillis()))
                    .description(StringUtils.hasText(description) ? description : null)
                    .name(request.getName())
                    .user(user)
                    .operationType(OperationType.findByCode(request.getOperationTypeCode()))
                    .build();
            categoryRepository.save(category);
            LOGGER.info("Saved new category");
            return true;
        } catch (Exception e) {
            LOGGER.error("Error when save new category");
        }

        return false;
    }

    @Override
    public List<CategoryResponse> getAllExpense() {
        try {
            LOGGER.info("Starting get user from token");
            Set<CategoryResponse> responses = getCategoryResponsesByOperationType(OperationType.EXPENSE);
            LOGGER.info("Build expense response done");
            return responses.stream().toList();
        } catch (Exception e) {
            LOGGER.error("Error when get all categories of user");
        }
        return null;
    }

    @Override
    public List<CategoryResponse> getAllIncome() {
        try {
            LOGGER.info("Starting get user from token");
            Set<CategoryResponse> responses = getCategoryResponsesByOperationType(OperationType.INCOME);
            LOGGER.info("Build income response done");
            return responses.stream().toList();
        } catch (Exception e) {
            LOGGER.error("Error when get all categories of user");
        }
        return null;
    }

    @Override
    public boolean update(EditCategoryRequest request) {
        if (!Validator.isValidObject(request)) {
            LOGGER.error("Request is not valid");
            return false;
        }

        try {
            User user = authManager.getUserByToken();
            Optional<Category> categoryOpt = categoryRepository.findById(request.getCategoryNo());
            if (categoryOpt.isEmpty()) {
                LOGGER.warn(String.format("Category no %s is not exist in database", request.getCategoryNo()));
                return false;
            }

            Category category = categoryOpt.get();
            if (!user.getUserNo().equals(category.getUser().getUserNo())) {
                LOGGER.warn(String.format("User %s not has permission to update this category", user.getEmail()));
                return false;
            }

            category.setName(request.getName() != null ? request.getName() : category.getName());
            category.setDescription(request.getDescription() != null ? request.getDescription() : category.getDescription());
            categoryRepository.save(category);
            LOGGER.info(String.format("Updated category with id %s", request.getCategoryNo()));
            return true;
        } catch (Exception e) {
            LOGGER.error("Error when update category");
        }
        return false;
    }

    @Override
    public boolean delete(EditCategoryRequest request) {
        if (!Validator.isValidObject(request)) {
            LOGGER.error("Request is not valid");
            return false;
        }

        try {
            User user = authManager.getUserByToken();
            Optional<Category> categoryOpt = categoryRepository.findById(request.getCategoryNo());
            if (categoryOpt.isEmpty()) {
                LOGGER.warn(String.format("Category no %s is not exist in database", request.getCategoryNo()));
                return false;
            }

            Category category = categoryOpt.get();
            if (!user.getUserNo().equals(category.getUser().getUserNo())) {
                LOGGER.warn(String.format("User %s not has permission to delete this category", user.getEmail()));
                return false;
            }

            categoryRepository.deleteById(request.getCategoryNo());

            LOGGER.info(String.format("Deleted category with id %s", request.getCategoryNo()));
            return true;
        } catch (Exception e) {
            LOGGER.error("Error when update category");
        }
        return false;
    }

    private Set<CategoryResponse> getCategoryResponsesByOperationType(OperationType ot) throws Exception {
        User user = authManager.getUserByToken();
        List<Category> categories = categoryRepository.findCategorise(ot, user);
        Set<CategoryResponse> responses = categories.stream()
                .filter(category -> category.getParentCategoryNo() == null)
                .map(category -> CategoryResponse.builder()
                        .parentCategoryDescription(category.getDescription())
                        .parentCategoryName(category.getName())
                        .parentCategoryNo(category.getCategoryNo())
                        .build())
                .collect(Collectors.toSet());
        LOGGER.info(String.format("User has %d parent categories", responses.size()));
        for (CategoryResponse response : responses) {
            String parentCategoryNo = response.getParentCategoryNo();
            List<Category> subCategories = categories.stream()
                    .filter(category -> category.getParentCategoryNo() != null &&
                            category.getParentCategoryNo().equals(parentCategoryNo))
                    .toList();
            response.setSubCategories(subCategories);
        }
        return responses;
    }
}
