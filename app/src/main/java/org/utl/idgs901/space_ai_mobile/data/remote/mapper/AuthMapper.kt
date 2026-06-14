package org.utl.idgs901.space_ai_mobile.data.remote.mapper

import org.utl.idgs901.space_ai_mobile.data.remote.dto.UserDto
import org.utl.idgs901.space_ai_mobile.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        email = email,
        folio = folio,
        role = role
    )
}
