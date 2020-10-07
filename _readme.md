# DHBW.Krypto-MSA

von 8093702 und 9752762

## Beispiel anhand der Nachricht "morpheus"
**Encrypt/Decrypt with RSA**  
`encrypt message "morpheus" using RSA and keyfile rsa_keyfile1.json`  
`decrypt message "AKTmQUEs5g3s" using RSA and keyfile rsa_keyfile1.json`  
`crack encrypted message "AKTmQUEs5g3s" using RSA`  
Cracking fails due to timeout

**Encrypt/Decrypt with Shift**  
`encrypt message "morpheus" using shift and keyfile shift_keyfile.json`  
`decrypt message "rtwumjzx" using shift and keyfile shift_keyfile.json`  
`crack encrypted message "rtwumjzx" using shift`
