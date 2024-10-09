package com.github.bccyben.common.service;

import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvException;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import com.github.bccyben.common.mappingStrategy.CustomBeanToCSVMappingStrategy;
import com.github.bccyben.common.model.csv.CsvLine;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * csv作成、読み込み
 */
@Service
public class CsvService {

    private static final byte[] UTF8_BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    private static final String CSV = ".csv";
    @Qualifier("innerValidator")
    @Autowired
    private Validator validator;

    /**
     * csvファイル作成
     *
     * @param <T>      the type parameter
     * @param entities the entities
     * @param fileName the file name
     * @param type     the type
     * @return response entity
     * @throws CsvException the csv exception
     * @throws IOException  the io exception
     */
    public <T> ResponseEntity<Resource> writeToCsv(List<T> entities, @Nullable String fileName, Class<T> type)
            throws CsvException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CustomBeanToCSVMappingStrategy<T> mappingStrategy = new CustomBeanToCSVMappingStrategy<>();
        mappingStrategy.setType(type);
        out.write(UTF8_BOM);
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer).withMappingStrategy(
                mappingStrategy).build();
        beanToCsv.write(entities);
        writer.close();
        Resource resource = new ByteArrayResource(out.toByteArray());
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("application/octet-stream")) // text/csvにするとBOMが消される
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""
                                + UriUtils.encode(
                                StringUtils.isNotBlank(fileName) ? fileName + CSV : "output" + CSV,
                                StandardCharsets.UTF_8.name())
                                + "\"")
                .body(resource);
    }

    /**
     * アップロードされたcsvをmodel listに変換
     *
     * @param <T>  the type parameter
     * @param file the file
     * @param type the type
     * @return list
     * @throws IOException the io exception
     */
    public <T extends CsvLine> List<T> readToEntities(MultipartFile file, Class<T> type) throws IOException {
        return readToEntities(file.getInputStream(), type);
    }

    /**
     * アップロードされたcsvをmodel listに変換
     *
     * @param <T>   the type parameter
     * @param input the input
     * @param type  the type
     * @return list
     */
    public <T extends CsvLine> List<T> readToEntities(InputStream input, Class<T> type) {
        Reader reader = new InputStreamReader(new BOMInputStream(input), StandardCharsets.UTF_8);
        // @CsvBindByPositionを無視する
        HeaderColumnNameMappingStrategy<T> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
        mappingStrategy.setType(type);
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader).withType(type).withMappingStrategy(
                mappingStrategy).build();
        // 重大なエラーがある場合giveup
        csvToBean.setThrowExceptions(true);
        // validateはjavaのやつを使う
        List<T> result = new ArrayList<>();
        Iterator<T> lines = csvToBean.iterator();
        Integer lineNumber = 1;
        while (lines.hasNext()) {
            T entity = lines.next();
            entity.setLineNumber(lineNumber);
            entity.validate(validator, Default.class);
            result.add(entity);
            if (lineNumber > 500) break;
            lineNumber++;
        }
        return result;
    }

}
