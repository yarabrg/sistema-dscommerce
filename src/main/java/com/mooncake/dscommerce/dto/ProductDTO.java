package com.mooncake.dscommerce.dto;

import jakarta.validation.constraints.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductDTO {

    private Long id;

    @Size(min = 3, max = 80, message = "Nome precisa ter entre 3 a 80 caracteres")
    @NotBlank(message = "Campo obrigatório")
    private String name;

    @Size(min = 10, message = "Descrição deve ter no mínimo 10 caracteres")
    @NotBlank
    private String description;

    @NotNull(message = "Campo obrigatório")
    @Positive(message = "O preço deve ser positivo")
    private Double price;
    private String imgUrl;

    @NotEmpty(message =  "Pelo menos uma categoria deve ser adicionada")
    private Set<CategoryDTO> categories = new HashSet<>();

    public ProductDTO() {
    }

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }
}
