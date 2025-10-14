package live.sahiwl.pocketlybe.service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.util.function.Function; 
import javax.crypto.spec.SecretKeySpec; 

@Service
public class JwtService {
    
    @Value("${JWT_SECRET}")
    private String secretKey;

        @Value("${JWT_EXPIRATION}") // 24 hours default
    private Long expiration;

    // public JwtService(){
    //     secretKey = generateSecretKey();
    // }

    // private String generateSecretKey(){
    //     try {
    //         KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
    //         SecretKey secretKey = keyGen.generateKey();
    //         System.out.println("Secret Key: "+ secretKey.toString());
    //         return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    //     } catch (Exception e) {
    //         throw new RuntimeException("Error getting secret key: ", e);
    //     }
    // }

    public String generateToken(String username){

        Map<String, Object> claims= new HashMap<>();

        return Jwts.builder()
        .setClaims(claims)
        .setSubject(username)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

     private SecretKeySpec getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
