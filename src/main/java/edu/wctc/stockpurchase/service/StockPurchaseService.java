package edu.wctc.stockpurchase.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import edu.wctc.stockpurchase.entity.StockPurchase;
import edu.wctc.stockpurchase.repo.StockPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockPurchaseService {
    private StockPurchaseRepository repo;
    private ObjectMapper objectMapper;

    @Autowired
    public StockPurchaseService(StockPurchaseRepository spr, ObjectMapper om) {
        this.repo = spr;
        this.objectMapper = om;
    }

    public List<StockPurchase> getAllStocks(){
        return repo.findAllByOrderById();
    }

    public StockPurchase getStock(String id){
        return getStock(Integer.parseInt(id));
    }

    public StockPurchase getStock(int id){
        return repo.findById(id).orElse(null);
    }
    public StockPurchase update(StockPurchase stockPurchase){
        if (repo.existsById(stockPurchase.getId())) {
            return repo.save(stockPurchase);
        } else {
            return null;
        }
    }

    public StockPurchase patch(int id, JsonPatch patch) throws
            JsonPatchException, JsonProcessingException {
        StockPurchase existingStudent = getStock(id);
        JsonNode patched = patch.apply(objectMapper
                .convertValue(existingStudent, JsonNode.class));
        StockPurchase patchedStudent = objectMapper.treeToValue(patched, StockPurchase.class);
        repo.save(patchedStudent);
        return patchedStudent;
    }

    public StockPurchase saveNewPurchase(StockPurchase s){
        return repo.save(s);
    }
    public void deletePurchase(int id){
        if(repo.existsById(id)){
            repo.deleteById(id);
        }
    }
}
