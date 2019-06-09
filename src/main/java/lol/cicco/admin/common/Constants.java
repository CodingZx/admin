package lol.cicco.admin.common;

import java.util.UUID;

public class Constants {
    public static final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    public static final String ADMIN_USER_TOKEN = "ADMIN_USER_TOKEN";

    public static final String CICCO_PERMISSION_LIST = "CICCO_PERMISSION_LIST";

    /**
     * 404页面
     */
    public static final String NOT_FOUND_PAGE = "/404";
    /**
     * 登录页面
     */
    public static final String LOGIN_PAGE = "/";


    public static class RSA {
        /**
         * token公钥信息
         */
        public static final String TOKEN_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFNaDu1cC/" +
                "fUqKqhhZTnrl5zraYj45EbSSlDNCnE/UhWukhht2MCIvrfy+FhXcgJgT4qhYXhIdU92BPqc++CE1gmVTPyawdQqVvBqhfloBontb/" +
                "CabLfcU5PMh0GkBEY3aUUkbtxX4HkkF43LP6EekNicy7qiX6YheReiJlXurRQIDAQAB";
        /**
         * token私钥信息
         */
        public static final String TOKEN_PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMU" +
                "1oO7VwL99SoqqGFlOeuXnOtpiPjkRtJKUM0KcT9SFa6SGG3YwIi+t/L4WFdyAmBPiqFheEh1T3YE+pz74ITWCZVM/" +
                "JrB1CpW8GqF+WgGie1v8Jpst9xTk8yHQaQERjdpRSRu3FfgeSQXjcs/oR6Q2JzLuqJfpiF5F6ImVe6tFAgMBAAECgYEAiFsWVU1SopR87/" +
                "fAMi/DZidFWC5Jm6gUVK1H7CysPeOqk+1uG4AYgyMn+D7JCteXT1iO0drpZ4HPTEgH19A1FNPaDLhXVmTwv+YaQkOhl476Sr+" +
                "e5QCBi4iRh90l8Iy5hwsuas+QMWeKBBEfFnHXnqmoZKwJkH+RsRjhLAZpzQECQQD7HttnF5qzRrJvdU4O+GKwUPPy8t9qoZBNL" +
                "Yx6P5STwPm7YSo9Kip3WF8AXzxdCbiX9vxcOrLN/RZcvzONCWShAkEAyQqaZ7xiSHQOYJ8WOrfB20CTkjzTWOgCKESUuPvBVNR1zn6Aa1E0sEmcU8+" +
                "MQxvuKg1JSrmSe738nIfS7wwgJQJBAIbelbuSo9VxPNg7ykakBGzprXtoEbH1P8kTk4T8rNGGLRoEH2lmQhSC89MBUyws0mVbk0H9jJaxPNdnIUq4J" +
                "sECQQCSaRZp1df9zAdsdm9J8+lKM41FQuboKI7ppIhq722syc3BxH7hqRImEHW4073iYcTg+ywVL3Bujb4agRAs5zfBAkBFPschMc6sGu" +
                "Qgm+ZLnkf5UvCu87e1rp7R+KfF00Gtwxvu7m0wfHf9awlQG3ycDLutotndimtUQfB+55FCsvYx";

    }

}
