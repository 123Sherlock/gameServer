package cgx.logic.define;

/**
 * 错误码定义
 * TODO: 也许可以改成可附加参数的形式
 */
public interface ErrorCodeDefine {

    /*--------------------通用--------------------*/
    /** 成功 */
    int SUCCESS = 0;
    /** 参数错误 */
    int E1_001 = 1001;
    /** 配置不存在 */
    int E1_002 = 1002;

    /*--------------------登录--------------------*/
    /** 不能重复登录 */
    int E2_001 = 2001;

    /*--------------------资源--------------------*/
    /** 资源类型不存在 */
    int E3_001 = 3001;
    /** 数量不合法 */
    int E3_002 = 3002;
    /** 道具背包已满 */
    int E3_003 = 3003;
    /** 道具背包找不到空格 */
    int E3_004 = 3004;
}
