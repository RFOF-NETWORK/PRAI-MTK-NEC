# Server notes for Banking rApp

This folder contains the API specification and guidelines for the server that supports the Banking rApp. The server MUST NOT hold START wallet private keys. The server is allowed to:

- validate incoming destination addresses (IBAN, crypto addresses)
- create unsigned transaction proposals
- store audit logs and metadata
- forward signed transactions to the network when client provides signed TX

Recommended endpoints (example):
- POST /tx/propose
- POST /tx/signed
- GET /tx/status/:id
- POST /iban/validate

Security notes:
- Do not commit any .env containing credentials.
- Use .env.example and GitHub Secrets for CI.
- Use TLS, strict auth for admin endpoints, and rate limiting.
