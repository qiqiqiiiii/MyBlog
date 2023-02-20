package org.njupt.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "更新标签dto")
public class TagUpdateDto {
    private Long id;
    private String name;
    private String remark;
}
