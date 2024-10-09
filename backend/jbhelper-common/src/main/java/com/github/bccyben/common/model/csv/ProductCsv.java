package com.github.bccyben.common.model.csv;

import java.time.LocalDate;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvCustomBindByName;
import com.github.bccyben.common.annotation.NotDisplayHeaderCsv;
import com.github.bccyben.common.csvConverter.NoErrorButNull;
import com.github.bccyben.common.validator.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * csv model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductCsv extends CsvLine {

    /**
     * 商品ID
     */
    @CsvBindByName(column = "商品ID")
    @CsvBindByPosition(position = 0)
    @NotDisplayHeaderCsv
    private String productId;

    /**
     * 商品名
     */
    @CsvBindByName(column = "商品名")
    @CsvBindByPosition(position = 1)
    @ValidMust(field = "商品名")
    private String productName;

    /**
     * 情報
     */
    @CsvBindByName(column = "情報")
    @CsvBindByPosition(position = 2)
    private String info;

    /**
     * 何らかの日付
     */
    @CsvBindByName(column = "何らかの日付")
    @ValidMust(field = "何らかの日付")
    @CsvCustomBindByName(column = "何らかの日付", converter = NoErrorButNull.class)
    @CsvBindByPosition(position = 3)
    private LocalDate date = LocalDate.now();

}
