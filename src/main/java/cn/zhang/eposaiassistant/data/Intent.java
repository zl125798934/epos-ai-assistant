package cn.zhang.eposaiassistant.data;

/**
 * 用户意图枚举
 */
public enum Intent {
    /**
     * 知识问答：涉及保险保全知识咨询，走 RAG
     */
    KNOWLEDGE,

    /**
     * 操作类：保单查询、保全办理等，走 Tool/Agent
     */
    OPERATION,

    /**
     * 闲聊：问候、感谢、无关对话等，纯 LLM 对话
     */
    CHAT
}
