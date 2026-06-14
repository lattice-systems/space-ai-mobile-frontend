package org.utl.idgs901.space_ai_mobile.core.security

import android.util.Base64
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoManager @Inject constructor(
    private val keystoreManager: KeystoreManager
) {

    companion object {
        private const val ALGORITHM = "AES/GCM/NoPadding"
        private const val TAG_LENGTH = 128
        private const val IV_LENGTH = 12
    }

    fun encrypt(plainText: String): String {
        if (plainText.isEmpty()) return ""
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, keystoreManager.getSecretKey())
        
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
        
        // Combine IV and Encrypted Data: IV(12) + CipherText
        val combined = ByteArray(iv.size + encryptedData.size)
        System.arraycopy(iv, 0, combined, 0, iv.size)
        System.arraycopy(encryptedData, 0, combined, iv.size, encryptedData.size)
        
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    fun decrypt(encryptedBase64: String): String {
        if (encryptedBase64.isEmpty()) return ""
        val combined = Base64.decode(encryptedBase64, Base64.NO_WRAP)
        
        if (combined.size < IV_LENGTH) return ""
        
        val iv = combined.copyOfRange(0, IV_LENGTH)
        val encryptedData = combined.copyOfRange(IV_LENGTH, combined.size)
        
        val cipher = Cipher.getInstance(ALGORITHM)
        val spec = GCMParameterSpec(TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, keystoreManager.getSecretKey(), spec)
        
        val decodedData = cipher.doFinal(encryptedData)
        return String(decodedData, StandardCharsets.UTF_8)
    }
}
