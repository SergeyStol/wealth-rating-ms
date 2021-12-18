package com.example.wealthratingms.slices.clients.db.daos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Sergey Stol
 * 2021-12-17
 */
@Data
@Document("rich_clients")
@AllArgsConstructor
@NoArgsConstructor
public class ClientDao {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private long fortune;
}
