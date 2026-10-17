package org.rfof.shared

enum class Role { GUEST, USER, RFOF_NETWORK, SATORAMY }

object RoleManager {
    private var currentRole: Role = Role.GUEST

    fun setRole(role: Role) { currentRole = role }
    fun getRole(): Role = currentRole

    fun requireAdmin() {
        if (!(currentRole == Role.RFOF_NETWORK || currentRole == Role.SATORAMY)) throw SecurityException("Admin access required")
    }

    fun canViewAdmin(): Boolean = (currentRole == Role.RFOF_NETWORK || currentRole == Role.SATORAMY)

    fun isGuest(): Boolean = currentRole == Role.GUEST
}
