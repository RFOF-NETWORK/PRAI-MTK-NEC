# Android rApp skeleton notes

This folder describes the Android rApp (Kotlin) skeleton and integration points. The real application must store the START wallet private key inside Android Keystore or StrongBox and never commit keys to source control.

Developer checklist:
- Implement Wallet using Android Keystore
- Implement UI for propose → sign → broadcast
- Use local database (Room) for audit logs and queued proposals
- Provide debug/test mode using ephemeral testnet wallets (do not use production wallet in debug)

Build & test:
- Use Gradle
- Provide a signing config that is NOT committed (use a placeholder keystore in .gitignore)
