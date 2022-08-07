package org.hf.application.custom.shop.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 20:05
*/
public class JwtTokenUtil {

    //秘钥
    public static final String SECRETUSER="5pil6aOO5YaN576O5Lmf5q+U5LiN5LiK5bCP6ZuF55qE56yR";//用户

    /***
     * 生成令牌-普通用户
     * @param uid:唯一标识符
     * @param ttlMillis:有效期
     * @return
     * @throws Exception
     */
    public static String generateTokenUser(String uid,Map<String,Object> payload, long ttlMillis) throws Exception {
        return generateToken(uid,payload,ttlMillis,SECRETUSER);
    }

    /***
     * 生成令牌
     * @param uid:唯一标识符
     * @param ttlMillis:有效期
     * @return
     * @throws Exception
     */
    public static String generateToken(String uid,Map<String,Object> payload, long ttlMillis,String secret) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Key signingKey = new SecretKeySpec(secret.getBytes(), signatureAlgorithm.getJcaName());

        Map<String,Object> header=new HashMap<String,Object>();
        header.put("typ","JWT");
        header.put("alg","HS256");
        JwtBuilder builder = Jwts.builder().setId(uid)
                .setIssuedAt(now)
                .setIssuer(uid)
                .setSubject(uid)
                .setHeader(header)
                .signWith(signatureAlgorithm, signingKey);

        //设置载体
        builder.addClaims(payload);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }


    /***
     * 解密JWT令牌
     */
    public static Map<String, Object> parseToken(String token){
        //以Bearer开头处理
        if(token.startsWith("Bearer")){
            token=token.substring(6).trim();
        }

        //秘钥处理
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key signingKey = new SecretKeySpec(SECRETUSER.getBytes(), signatureAlgorithm.getJcaName());

        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    public static void main(String[] args) throws Exception {
        Map<String,Object> payload = new HashMap<>();
        payload.put("username","hufei");
        payload.put("aaa","ccc");
        payload.put("bbb","ddd");
        String token = generateTokenUser(UUID.randomUUID().toString(), payload, 10000000L);
        System.out.println(token);
        System.out.println(parseToken(token));
    }

}



