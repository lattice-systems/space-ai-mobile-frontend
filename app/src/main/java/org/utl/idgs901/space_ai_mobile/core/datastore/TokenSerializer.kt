package org.utl.idgs901.space_ai_mobile.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object TokenSerializer : Serializer<AuthTokens> {
    override val defaultValue: AuthTokens = AuthTokens.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AuthTokens {
        try {
            return AuthTokens.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AuthTokens, output: OutputStream) = t.writeTo(output)
}
