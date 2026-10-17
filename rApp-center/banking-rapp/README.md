# Banking rApp — PRAI/MTK/NEC

This rApp is the Banking DB rApp requested by the project owner: an integrated banking and token orchestration app for EU-DE (ISO 13616 [42E0]) workflows. It contains scaffolding only — no private keys or production secrets.

Purpose
- Provide the rApp manifest and documentation for a Banking rApp that implements:
  - IBAN (ISO 13616) validation and normalization
  - Support for a single START wallet residing in the PRAI/MTK/NEC Android rApp (app signs all transactions)
  - Handling of incoming destination addresses (crypto wallets / IBANs) from interfaces
  - Escrow mapping and audit logging (on‑chain escrow recommended for tokens; fiat off‑chain via partner PSP)
  - Supply/Price logic module (parameterized) planned as an on‑chain controllable policy

This README intentionally contains no secrets or .kt production files. See docs/design.md for architecture and developer guidance.
