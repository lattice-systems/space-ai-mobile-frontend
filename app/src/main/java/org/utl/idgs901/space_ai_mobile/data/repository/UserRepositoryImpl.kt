package org.utl.idgs901.space_ai_mobile.data.repository

import kotlinx.coroutines.delay
import org.utl.idgs901.space_ai_mobile.domain.model.UserProfile
import org.utl.idgs901.space_ai_mobile.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    
    override suspend fun getUserProfile(userId: String): Result<UserProfile> {
        delay(1500) // Simulate network delay for skeleton loading
        return Result.success(
            UserProfile(
                id = userId,
                name = "Juan Pablo Rea Cano",
                email = "juan@utleon.edu.mx",
                matricula = "23001501",
                group = "IDGS-801",
                career = "Ingeniería en Desarrollo y Gestión de Software",
                division = "Tecnologías de la Información",
                campus = "Universidad Tecnológica de León",
                phone = "4771234567",
                isVerified = true
            )
        )
    }

    override suspend fun updatePassword(currentPassword: String, newPassword: String): Result<Unit> {
        delay(2000) // Simulate network delay
        return Result.success(Unit)
    }
}
