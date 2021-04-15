package security

import "crypto/sha256"

var (
    salt = []byte("0e2e8323-6fcc-4811-a2da-88fffdc9325b")
    saltPosition = 3
)

/*
 * How does it works!
 * We need to create unique and safe hash by password.
 * So, first step is generating SHA-256 hash of password.
 * Then, we insert SALT into hash in random (no) position.
 * Last step is generation final SHA-256 hash of (hash+salt) value
 */
func GetPasswordHash(password string) []byte {
    sha := sha256.New()
    hash := sha.Sum([]byte(password))
    
    //insert SALT
    hash = append(append(hash[:saltPosition], salt...), hash[saltPosition:]...)
    
    sha = sha256.New()
    return sha.Sum(hash)
}