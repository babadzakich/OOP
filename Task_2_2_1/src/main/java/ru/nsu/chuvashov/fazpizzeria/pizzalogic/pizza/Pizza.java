package ru.nsu.chuvashov.fazpizzeria.pizzalogic.pizza;

import lombok.*;

/**
 * Pizza class.
 */
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
