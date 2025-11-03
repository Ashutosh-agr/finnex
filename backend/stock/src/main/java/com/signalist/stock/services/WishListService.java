package com.signalist.stock.services;

import com.signalist.stock.dto.WishListRequest;
import com.signalist.stock.dto.WishListResponse;
import com.signalist.stock.entity.User;
import com.signalist.stock.entity.WishList;
import com.signalist.stock.repository.UserRepository;
import com.signalist.stock.repository.WishListRepository;
import com.signalist.stock.util.ObjectIdUtil;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishListService {
    private final UserRepository userRepository;
    private final WishListRepository wishListRepository;
    private final ObjectIdUtil objectIdUtil;

    public WishListService(UserRepository userRepository,WishListRepository wishListRepository,ObjectIdUtil objectIdUtil){
        this.userRepository = userRepository;
        this.wishListRepository = wishListRepository;
        this.objectIdUtil = objectIdUtil;
    }

    public void saveEntity(WishListRequest request){
        String ud = request.getUserId();
        ObjectId userId = objectIdUtil.fromStringSafe(ud);
        WishList w = WishList.builder()
                .userId(userId)
                .symbol(request.getSymbol())
                .company(request.getCompany())
                .addedAt(Date.from(Instant.now()))
                .build();
        wishListRepository.save(w);
    }

    public List<String> getSymbolByEmail(String email){
        if(email == null || email.isBlank()) return List.of();
        User user = userRepository.findByEmail(email);
        if(user == null || user.getId() == null) return List.of();

        return wishListRepository.findByUserId(user.getId())
                .stream()
                .map(WishList::getSymbol)
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    public List<WishListResponse> getSymbolByUserId(ObjectId userId){
        if(userId == null) return List.of();

        return wishListRepository.findByUserId(userId)
                .stream()
                .map(w -> new WishListResponse(
                        w.getSymbol(),
                        w.getCompany(),
                        w.getAddedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public long deleteByUserIdAndSymbol(ObjectId userId,String symbol){
        if(userId == null || symbol == null || symbol.isBlank()) return 0L;

        return wishListRepository.deleteByUserIdAndSymbol(userId, symbol);
    }
}
