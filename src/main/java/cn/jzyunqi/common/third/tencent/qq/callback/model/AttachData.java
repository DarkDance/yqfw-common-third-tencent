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
    /**
     * 文件类型，"image/jpeg","image/png","image/gif"，"file"，"video/mp4"，"voice"
     */
    @JsonProperty("content_type")
    private String contentType;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 图片高度
     */
    private Integer height;

    /**
     * 	图片宽度
     */
    private Integer width;

    /**
     * 文件大小
     */
    private Integer size;

    /**
     * 文件链接
     */
    private String url;
}
