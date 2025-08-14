package dev.matheus.MagicFridgeAI.Mapper;

import dev.matheus.MagicFridgeAI.DTOs.FoodItemDTO;
import dev.matheus.MagicFridgeAI.Model.FoodItem;
import org.springframework.stereotype.Component;

@Component
public class FoodItemMapper {

    public static FoodItem map(FoodItemDTO dto){
        FoodItem foodItem = new FoodItem();
        foodItem.setId(dto.getId());
        foodItem.setNome(dto.getNome());
        foodItem.setCategoria(dto.getCategoria());
        foodItem.setQuantidade(dto.getQuantidade());
        foodItem.setValidade(dto.getValidade());
        return foodItem;
    }

    public FoodItemDTO map(FoodItem foodItem){
        FoodItemDTO dto = new FoodItemDTO();
        dto.setId(foodItem.getId());
        dto.setNome(foodItem.getNome());
        dto.setCategoria(foodItem.getCategoria());
        dto.setQuantidade(foodItem.getQuantidade());
        dto.setValidade(foodItem.getValidade());
        return dto;
    }

}
