# Banking rApp — Design & Implementation Plan

This document translates the project owner requirements into concrete technical modules and implementation steps.

1) Purpose
- Provide a Banking rApp implementing IBAN (ISO 13616) validation, destination handling (crypto addresses), a single START wallet model (app side signing), escrow mapping, and a parameterized supply/price logic module.

2) High-level architecture
- app-android/: Kotlin rApp (START wallet lives here) — signs transactions, manages local keystore
- web/: PWA for admin & demo, deployed to GitHub Pages
- server/: Kotlin Ktor server (stubs) — validates addresses, creates unsigned tx proposals, stores audit logs
- contracts/: Solidity contracts for ERC20 + Escrow (not included here yet)

3) Key constraints
- PRIVATE KEYS MUST NEVER BE IN REPO
- Server is non‑signing by default
- Production keystores use Android Keystore / HSM

4) IBAN (ISO 13616)
- Implement validation server-side (Kotlin) and client-side demo (web)
- Steps: normalize, move first 4 chars to end, convert letters to numbers, compute mod 97 == 1

5) Supply/Price logic
- Implement as parametrized module; on‑chain actions (mint/burn) gated by multisig governance

6) Escrow & Banking
- Token escrow on‑chain (Escrow contract)
- Fiat settlements handled off‑chain by PSP; server maps IBAN requests to on‑chain events and records audit data

7) Development & deployment
- Branch: feature/add-banking-rapp
- CI: GitHub Actions (build/test), deploy web to GitHub Pages

8) Next deliverables (if you approve)
- contracts/ (solidity) + tests
- server/ (Ktor) skeleton code + unit tests
- app-android/ skeleton project (Kotlin) with wallet stub and signing flow example

Security & Compliance reminder: implement KYC/AML flows before processing fiat; external audit for contracts required.
