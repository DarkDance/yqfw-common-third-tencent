package cn.jzyunqi.common.model.tree;

import lombok.Builder;
import lombok.Data;

/**
 * @author wiiyaya
 * @date 2022/11/7.
 */
@Data
@Builder
public class TreePathFeatureIns implements TreePathFeature<Long>{

    /**
     * 名称
     */
    private Long id;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 路径
     */
    private String path;

    /**
     * 深度
     */
    private Integer depth;

    /**
     * 次序
     */
    private Integer sequence;
}
