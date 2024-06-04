package com.easybbs.entity.po;

import java.io.Serializable;
import lombok.Data;

/**
 * 系统设置信息
 * @TableName sys_setting
 */
@Data
public class SysSetting implements Serializable {
    /**
     * 编号
     */
    private String code;

    /**
     * 设置信息
     */
    private String jsonContent;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysSetting other = (SysSetting) that;
        return (this.getCode() == null ? other.getCode() == null : this.getCode().equals(other.getCode()))
            && (this.getJsonContent() == null ? other.getJsonContent() == null : this.getJsonContent().equals(other.getJsonContent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCode() == null) ? 0 : getCode().hashCode());
        result = prime * result + ((getJsonContent() == null) ? 0 : getJsonContent().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", code=").append(code);
        sb.append(", jsonContent=").append(jsonContent);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}