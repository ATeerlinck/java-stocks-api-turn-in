package edu.wctc.stockpurchase.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import edu.wctc.stockpurchase.entity.StockPurchase;
import edu.wctc.stockpurchase.service.StockPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/stockpurchases")
public class StockPurchaseController {

    private StockPurchaseService service;

    @Autowired
    public StockPurchaseController(StockPurchaseService sps) {
        this.service = sps;
    }

    @GetMapping("")
    public List<StockPurchase> getStockPurchases(){
        return service.getAllStocks();
    }

    @GetMapping("/{purchase_id}")
    public StockPurchase getStockPurchase(@PathVariable String purchase_id){
        return service.getStock(purchase_id);
    }

    @PostMapping("")
    public StockPurchase NewPurchase(){
        StockPurchase s = new StockPurchase();
        s.setPurchase_date(new Date());
        s.setSymbol("GME");
        s.setPrice_per_share(69.69);
        s.setShares(69);
        return service.saveNewPurchase(s);
    }

    @PutMapping("")
    public StockPurchase PutPurchase(){
        StockPurchase s = service.getStock("1");
        s.setPurchase_date(new Date());
        s.setPrice_per_share(24.03);
        s.setShares(-50);
        return service.saveNewPurchase(s);
    }

    @DeleteMapping("/{purchase_id}")
    public String DeletePurchase(@PathVariable String purchase_id){
        try{
            int id = Integer.parseInt(purchase_id);
            service.deletePurchase(id);
            return "Purchase deleted: ID " + purchase_id;
        }  catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Purchase ID must be a number", e);
        }
    }

    @PatchMapping("/{purchase_id}")
    public StockPurchase PatchPurchase(@PathVariable String purchase_id, @RequestBody JsonPatch patch){
        try {
            int id = Integer.parseInt(purchase_id);
            return service.patch(id, patch);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Purchase ID must be a number", e);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid patch format: " + e.getMessage(), e);
        }
    }

}
