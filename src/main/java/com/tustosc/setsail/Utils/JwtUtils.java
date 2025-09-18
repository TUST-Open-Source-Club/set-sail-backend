package com.tustosc.setsail.Utils;

import com.tustosc.setsail.Entiy.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    @Value("${jwt.token}")
    private String token;

    private String uuid;
    private List<String> privileges;
    public String jwtBuilder(String uuid, List<String> privileges) throws Exception {
        String header=buildHeader();
        String payload=buildPayload(uuid, privileges);
        String sign=buildSignature(header, payload);
        return header+"."+payload+"."+sign;
    }

    public JwtUtils id(String id){
        this.uuid=id;
        return this;
    }

    public JwtUtils role(Role role){
        privileges.add(role.toString());
        return this;
    }

    public String build() throws Exception {
        uuid="";
        privileges.clear();
        return jwtBuilder(uuid, privileges);
    }

    public JwtUtils builder(){
        return this;
    }

    public String getUuidFromJwt(String jwt){
        try {
            String[] parts = jwt.split("\\.");
            String payload = parts[1];
            // 解析 payload
            Base64.Decoder decoder = Base64.getDecoder();
            String payloadJson = new String(decoder.decode(payload));
            JSONObject jsonObject = new JSONObject(payloadJson);
            return jsonObject.getString("sub");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT", e);
        }
    }

    public List<Role> getRoleFromJwt(String jwt){
        try {
            String[] parts = jwt.split("\\.");
            String payload = parts[1];
            // 解析 payload
            Base64.Decoder decoder = Base64.getDecoder();
            String payloadJson = new String(decoder.decode(payload));
            JSONObject jsonObject = new JSONObject(payloadJson);
            JSONArray privileges = jsonObject.getJSONArray("sub");
            List<String> privilegesList = new ArrayList<>();
            for (int i = 0; i < privileges.length(); i++) {
                privilegesList.add(privileges.getString(i));
            }
            return privilegesList.stream().map(Role::from).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT", e);
        }
    }

    public Boolean verifyJwt(String jwt) throws Exception {
            String[] parts = jwt.split("\\.");
            String header = parts[0];
            String payload = parts[1];
            String sign = buildSignature(header, payload);
            if(sign.equals(parts[2])){
                return true;
            }
            return false;
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

    private String buildPayload(String uuid, List<String> privileges) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sub", uuid); // 这是要解码出来的内容
        jsonObject.put("exp", System.currentTimeMillis()+24*60*60*1000);
        Random random=new Random();
        jsonObject.put("jit", String.valueOf(random.nextInt(10)));
        jsonObject.put("iat", System.currentTimeMillis());
        jsonObject.put("pri", privileges);
        String payload = jsonObject.toString();
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(payload.getBytes());
    }

    private String buildSignature(String header, String payload) throws Exception {
        return HmacSHA256Utils.hmacSHA256(token, header+"."+payload);
    }
}
