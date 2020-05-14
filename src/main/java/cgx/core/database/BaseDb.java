package cgx.core.database;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BaseDb {

    public Long getId() {
        return _id;
    }

    public void setId(Long id) {
        _id = id;
    }

    protected Long _id;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
