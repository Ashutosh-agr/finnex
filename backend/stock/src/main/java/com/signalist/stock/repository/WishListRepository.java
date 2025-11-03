package com.signalist.stock.repository;

import com.signalist.stock.entity.WishList;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface WishListRepository extends MongoRepository<WishList, ObjectId> {
    List<WishList> findByUserId(ObjectId userId);

    @Query(value = "{ 'userId': ?0, 'symbol': { $regex: ?1, $options: 'i' } }", delete = true)
    long deleteByUserIdAndSymbol(ObjectId userId, String symbol);
}
