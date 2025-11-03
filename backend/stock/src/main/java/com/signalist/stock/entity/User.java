package com.signalist.stock.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
@Builder
public class User {

    @Id
    private ObjectId id;
    @NonNull
    private String fullName;
    @Indexed(unique = true)
    @NonNull
    private String email;
    @NonNull
    private String password;

    private String country;
    private String investmentGoals;
    private String preferredIndustry;
}
