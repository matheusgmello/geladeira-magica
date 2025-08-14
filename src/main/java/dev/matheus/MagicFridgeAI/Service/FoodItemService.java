package dev.matheus.MagicFridgeAI.Service;

import dev.matheus.MagicFridgeAI.DTOs.FoodItemDTO;
import dev.matheus.MagicFridgeAI.Mapper.FoodItemMapper;
import dev.matheus.MagicFridgeAI.Model.FoodItem;
import dev.matheus.MagicFridgeAI.Repository.FoodItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodItemService {

    private FoodItemRepository foodItemRepository;
    private FoodItemMapper foodItemMapper;

    public FoodItemService(FoodItemRepository foodItemRepository, FoodItemMapper foodItemMapper) {
        this.foodItemRepository = foodItemRepository;
        this.foodItemMapper = foodItemMapper;
    }

    // Salva um item
    public FoodItemDTO salvar(FoodItemDTO foodItemDTO) {
        FoodItem foodItem = foodItemMapper.map(foodItemDTO);
        FoodItem itemSalvo = foodItemRepository.save(foodItem);
        return foodItemMapper.map(itemSalvo);
    }

    // Lista todos os itens
    public List<FoodItem> listar(){
        return foodItemRepository.findAll();
    }

    // Lista um item por ID
    public FoodItemDTO listarPorId(Long id){
        Optional<FoodItem> foodItemId = foodItemRepository.findById(id);
        return foodItemId.map(foodItemMapper::map).orElse(null);
    }

    // Deleta um item
    public void deletar(Long id){
        foodItemRepository.deleteById(id);
    }

    // Atualiza parcialmente um item
    public FoodItemDTO atualizarParcialmente(Long id, FoodItemDTO foodItemDTO) {
        Optional<FoodItem> foodItemExistente = foodItemRepository.findById(id);

        if (foodItemExistente.isPresent()) {
            FoodItem foodItem = foodItemExistente.get();

            if (foodItemDTO.getNome() != null) {
                foodItem.setNome(foodItemDTO.getNome());
            }
            if (foodItemDTO.getQuantidade() != null) {
                foodItem.setQuantidade(foodItemDTO.getQuantidade());
            }
            if (foodItemDTO.getValidade() != null) {
                foodItem.setValidade(foodItemDTO.getValidade());
            }
            if (foodItemDTO.getCategoria() != null) {
                foodItem.setCategoria(foodItemDTO.getCategoria());
            }

            FoodItem foodItemAtualizado = foodItemRepository.save(foodItem);
            return foodItemMapper.map(foodItemAtualizado);
        }


        return null;
    }

}