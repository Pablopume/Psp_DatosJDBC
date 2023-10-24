package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {
    private int id;
    private String name;
    private String description;
    private int price;

    @Override
    public String toString() {
        return name;
    }
}
