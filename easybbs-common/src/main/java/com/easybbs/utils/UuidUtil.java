package com.easybbs.utils;

import java.util.UUID;

public class UuidUtil {
    public static String[] chars = new String[]{
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };
    // 26 * 2 + 10 一共62个字符
    public static String getShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            // 转换成62进制
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }
    /*
     * 用于生成conversationId
     * Service私有方法
     * */
    private String getConversationId(String sendId, String receiveId) {
        // 比较 sendId 和 receiveId，将较小的放在前面
        String smallerId = sendId.compareTo(receiveId) < 0 ? sendId : receiveId;
        String largerId = sendId.compareTo(receiveId) < 0 ? receiveId : sendId;
        // 拼接较小的id和较大的id，以生成 conversationId
        return smallerId + largerId;
    }
}
 
