package com.hd.micromonitorservice.entity;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: liwei
 * @Description:
 */
@Getter
@Setter
@Document(collection="cost")
@AllArgsConstructor
//@NoArgsConstructor
@ApiModel("uri访问")
public class UriCost {
    String tm;
    String uri;
    Integer cost;
    Float avgcost;
    Integer totalCost;
    Integer count;
}
