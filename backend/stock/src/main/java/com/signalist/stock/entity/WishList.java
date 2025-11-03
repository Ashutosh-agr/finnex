package com.signalist.stock.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "wishlist")
@CompoundIndexes({
        @CompoundIndex(name = "user_symbol_idx", def = "{'userId': 1, 'symbol': 1}", unique = true)
})
@Builder
public class WishList {
    @Id
    private ObjectId id;

    @Indexed
    private ObjectId userId;

    private String symbol;

    private String company;

    private Date addedAt = new Date();
}
