package org.njupt.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryListVo {
    private String description;
    private Long id;
    private String name;
    private String status;
}
