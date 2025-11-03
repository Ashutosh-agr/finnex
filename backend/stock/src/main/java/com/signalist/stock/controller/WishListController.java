package com.signalist.stock.controller;

import com.signalist.stock.dto.WishListRequest;
import com.signalist.stock.dto.WishListResponse;
import com.signalist.stock.services.WishListService;
import com.signalist.stock.util.ObjectIdUtil;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/wishlist")
public class WishListController {
    private final WishListService wishListService;
    private final ObjectIdUtil objectIdUtil;

    public WishListController(WishListService wishListService,ObjectIdUtil objectIdUtil){
        this.wishListService = wishListService;
        this.objectIdUtil = objectIdUtil;
    }

    @PostMapping("/add")
    public void addToWishlist(@RequestBody WishListRequest request){
        wishListService.saveEntity(request);
    }

    @GetMapping("/wishlist")
    public List<WishListResponse> getFromWishList(@RequestParam(required = true) String userId){
        ObjectId userid = objectIdUtil.fromStringSafe(userId);
        return wishListService.getSymbolByUserId(userid);
    }

    @DeleteMapping("/remove")
    public long removeFromWishlist(@RequestParam String userId, @RequestParam String symbol) {
        ObjectId userid = objectIdUtil.fromStringSafe(userId);
       return wishListService.deleteByUserIdAndSymbol(userid, symbol);
    }
}
