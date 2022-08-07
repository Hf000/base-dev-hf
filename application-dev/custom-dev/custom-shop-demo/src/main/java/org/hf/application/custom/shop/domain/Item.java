package org.hf.application.custom.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:01
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Item implements Serializable {

    private String id;
    private String name;
    private String description;
    private String images;
    private String video;
    private Integer count;
    private Integer price;
}
