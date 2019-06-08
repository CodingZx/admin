package lol.cicco.admin.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Select {
    private UUID id;
    private String name;
}
