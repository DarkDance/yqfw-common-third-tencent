package cn.jzyunqi.common.third.tencent.qq.callback.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wiiyaya
 * @since 2025/12/13
 */
@Getter
@Setter
public class AttachData {
    @JsonProperty("content_type")
    private String contentType;
    private String filename;
    private Integer height;
    private Integer width;
    private Integer size;
    private String url;
}
