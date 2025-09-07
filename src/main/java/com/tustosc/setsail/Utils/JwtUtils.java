package com.tustosc.setsail.Utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Random;

@Component
public class JwtUtils {

    @Value("${jwt.token}")
    private String token;

    public String jwtBuilder(String uuid) throws Exception {
        String header=buildHeader();
        String payload=buildPayload(uuid);
        String sign=buildSignature(header, payload);
        return header+"."+payload+"."+sign;
    }

    public String getUuidFromJwt(String jwt){
        return "";
    }

    private String buildHeader() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("alg", "HS256");
        jsonObject.put("typ", "JWT");
        String header = jsonObject.toString();
        // 编码Base64，解码基本就是Encoder变Decoder
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(header.getBytes());
    }

    private String buildPayload(String uuid) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sub", uuid); // 这是要解码出来的内容
        jsonObject.put("exp", System.currentTimeMillis()+24*60*60*1000);
        Random random=new Random();
        jsonObject.put("jit", String.valueOf(random.nextInt(10)));
        jsonObject.put("iat", System.currentTimeMillis());
        String payload = jsonObject.toString();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(payload.getBytes());
    }

    private String buildSignature(String header, String payload) throws Exception {
        return HmacSHA256Utils.hmacSHA256(token, header+"."+payload);
    }
}
