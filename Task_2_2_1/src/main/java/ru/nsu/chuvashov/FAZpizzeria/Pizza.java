package ru.nsu.chuvashov.FAZpizzeria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Pizza {
    private int id;
    private String name;
    private int price;
    private int quantity;
    private String phone;
    @Setter
    private boolean cooked;
}
