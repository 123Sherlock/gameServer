package cgx.logic.resource.give.check;

import cgx.logic.resource.ResourceWrap;

public class ResCheckResultCode {

    /**
     * 资源内容
     */
    private ResourceWrap _resource;

    /**
     * 错误码
     */
    private int _errorCode;

    public ResCheckResultCode() {
    }

    public ResCheckResultCode(ResourceWrap resource, int errorCode) {
        _resource = resource;
        _errorCode = errorCode;
    }

    public ResourceWrap getResource() {
        return _resource;
    }

    public void setResource(ResourceWrap resource) {
        _resource = resource;
    }

    public int getErrorCode() {
        return _errorCode;
    }

    public void setErrorCode(int errorCode) {
        _errorCode = errorCode;
    }
}
