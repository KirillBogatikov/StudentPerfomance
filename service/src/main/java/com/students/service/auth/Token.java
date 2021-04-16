package com.students.service.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import javax.crypto.SecretKey;

import com.students.util.Dates;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class Token {
    private SecretKey secretKey;
	private UUID id;
	private UUID userId;
	private Date issuedAt;
	private Date expiration;

	public Token(SecretKey secretKey, UUID userId) {
		this.secretKey = secretKey;
		id = UUID.randomUUID();
		this.userId = userId;
		issuedAt = Dates.now();
		expiration = Dates.nowPlusHours(24);
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public Date getCreated() {
		return issuedAt;
	}

	public void setCreated(Date created) {
		this.issuedAt = created;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
	public String encode() {
		var claims = new HashMap<String, Object>();
		claims.put("jti", id);
		claims.put("userId", userId);
		claims.put("iat", issuedAt);
		claims.put("exp", expiration);
		return Jwts.builder().addClaims(claims).signWith(secretKey).compact();
	}
	
	public static Token decode(SecretKey secretKey, String tokenBase64) {
		Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(tokenBase64);       
        
		var claims = jws.getBody();
        var token = new Token(secretKey, UUID.fromString(claims.getId()));
        token.setCreated(claims.getIssuedAt());
        token.setUserId(UUID.fromString(claims.get("userId").toString()));
        token.setExpiration(claims.getExpiration());
        
        return token;
	}
	
	public boolean isValid() {
		return this.expiration.getTime() > Dates.now().getTime();
	}
}
