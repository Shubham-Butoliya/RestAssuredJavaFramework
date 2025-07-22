package com.api.framework.users.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.poiji.annotation.ExcelCellName;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class User {

    @ExcelCellName("name")
    private String name;
    @ExcelCellName("email")
    private String email;
    @ExcelCellName("gender")
    private String gender;
    @ExcelCellName("status")
    private String status;
}
