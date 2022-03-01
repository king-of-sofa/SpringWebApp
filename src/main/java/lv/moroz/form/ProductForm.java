package lv.moroz.form;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductForm {
    private Long id;
    private String name;
    private Double price;
}
