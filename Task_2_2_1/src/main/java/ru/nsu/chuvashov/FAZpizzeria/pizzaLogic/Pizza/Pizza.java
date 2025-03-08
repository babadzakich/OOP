package ru.nsu.chuvashov.FAZpizzeria.pizzaLogic.Pizza;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@NonNull
public class Pizza {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String phone;
    @Setter
    private boolean cooked;
}
