package com.example.PetAdoption.interfAdaptadora.dtos;

import java.util.UUID;

public class RecommendationDtos {
    public static class Item {
        public UUID petId;
        public String name;
        public String species;
        public String size;
        public String sex;
        public Integer score;
        public String image;
    }

    public static class PageResponse {
        public int page;
        public int size;
        public long totalElements;
        public int totalPages;
        public java.util.List<Item> content;
    }
}