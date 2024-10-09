package com.github.bccyben.common.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class MailModel implements Serializable {
    /**
     * 件名
     */
    private String subject;
    /**
     * 本文
     */
    private String bodyHtml;
    /**
     * テンプレートID
     */
    private String templateName;
}
