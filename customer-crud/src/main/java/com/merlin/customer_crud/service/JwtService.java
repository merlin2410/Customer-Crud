package com.merlin.customer_crud.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//Contains various methods related to token generation,validation etc.
@Component
public class JwtService {

    public static final String SECRET_KEY = "C9F9E51B4A060072D5ED8343F00C3E54118BE8AB204795C6FD02151EA2AE0547A59B6BF8A84271E0562B603B2EB410B7F1C1E70DC28BEE003A7338F979297A9ACF4F35EA06ECF676FFFA0EEBC4BCC46E250792158A4DDD952B43B65FC89C3178AAFCBDA6D0A6A2A19BE49C99A78111162CA542F04FCC5B6E17446892A5866ADB";

    //Generates token, takes username as input
    public String generateToken(String userName){
        //A Map object is created which can have key,value pairs
        Map<String,Object> claims = new HashMap<>();

        //Returns token as a string.
        return createToken(claims,userName);
    }

    //Used to create token based on claims which is Map object and username which is a string
    public String createToken(Map<String,Object> claims, String userName){
        //This creates a Jwt builder object which is then converted to string using compact()
        //The header, payload(claims), signature are combined and jwt token is created
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*15))
                .signWith(getSignKey(),
                        SignatureAlgorithm.HS256).compact();
    }

    //Create a sign key in Keys type
    public Key getSignKey(){
        //Decodes the key into bytes array
        byte keyBytes[] = Decoders.BASE64.decode(SECRET_KEY);

        //Returns the key after encoding it with hmacShakey 256 algorithm
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //Extracts username from the provided JWT token in String format
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Extracts expiration date
    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    //This function takes token in String format and a Function interface arguements
    //Function interface here in turn takes Claims (contains all claims) object as arguement and returns an arbitary type T
    //In the above function extractClaim, claimResolver is getExpiration method which returns Date object
    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Takes token in String format as input
    //Returns Claims
    public Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Checks whether token is expired or not
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    //Validate token
    //Token and userDetails are the input
    public Boolean validateToken(String token, UserDetails userDetails){
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}

