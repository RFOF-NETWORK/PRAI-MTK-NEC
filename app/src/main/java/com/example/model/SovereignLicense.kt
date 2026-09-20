package com.example.model

import com.example.crypto.AESEncryption

object SovereignLicenseData {
  const val LICENSE_NAME = "RFOF-NETWORK SOVEREIGN COPYRIGHT & INTELLECTUAL PROPERTY LICENSE (PRAI / MTK / NEC v1.0)"
  const val COPYRIGHT_HOLDER = "RFOF-NETWORK (Urheber & Erfinder)"
  const val REPO_NAME = "PRAI / MTK / NEC App"
  const val GITHUB_ACCOUNT = "RFOF-NETWORK"

  val LICENSE_HASH: String = AESEncryption.sha256(
    "RFOF-NETWORK:PRAI_MTK_NEC:SOVEREIGN_IP_LICENSE:2026:MTK_SINGLE_CURRENCY:28_CATEGORIES:8_PERSPECTIVES:XJUSTIZ_NOTARIAL_PARITY"
  )

  val FULL_LICENSE_TEXT = """
    ============================================================================
    $LICENSE_NAME
    Urheberrechtsschutz, Erfinderprivileg & Exklusivitäts-Charta
    Repository: $GITHUB_ACCOUNT / $REPO_NAME
    Lizenz-Signatur-Hash: $LICENSE_HASH
    ============================================================================

    1. URHEBERSCHAFT & AUSSCHLIESSLICHES EIGENTUM
       Das gesamte Wirtschafts-, Rechts- und Technologie-Ökosystem PRAI / MTK / NEC,
       einschließlich der 4 Kybernetischen Layer (I–IV), der 28 Fachkategorien, der
       8 dialektischen Perspektiven, der Wohnzentren-Architektur und der notariellen
       Scheckverbriefung (§ 36 BeurkG), unterliegt dem ausschließlichen Urheberrecht
       und geistigen Eigentum des GitHub-Accounts $GITHUB_ACCOUNT.

    2. WÄHRUNGSEXKLUSIVITÄT & SINGLE-CURRENCY (MTK)
       Die Währung Montalkanio (MTK) ist eine geschützte, singuläre Souveränitätswährung.
       Es ist Dritten strikt untersagt, MTK zu klonen, zu forken, zu emittieren oder
       ohne Autorisierung von $GITHUB_ACCOUNT in externe Liquiditätspools zu extrahieren.
       Die MTK-Währung verbleibt ausschließlich in der Treuhand- & Reserveverwaltung
       des Admins (RFOF-NETWORK). Externe Nutzer partizipieren über dezentrale
       Multi-Chain-Assets (BTC, ETH, TON) und besicherte Escrow-Zertifikate.

    3. NEC (NON-EXTRACTABLE CODE & CERTIFICATE) DUAL-PARITÄT
       NEC ist keine liquide Spekulationswährung, sondern ein nicht-extrahierbares
       Zertifikat und NFT-Urkunde. Jedes NEC-Zertifikat existiert in unauflöslicher
       Dual-Parität sowohl auf der Sovereign MTK-Blockchain als auch in der notariellen
       Justizdatenbank (XJustiz). Originale verbleiben fest beim Urheber; Dritte
       erhalten bei Übertragung beglaubigte Abschriften (§ 36 BeurkG).

    4. MULTI-ROLLEN- & MITARBEITS-REGELUNG
       (a) ADMIN (RFOF-NETWORK):
           Alleinger Inhaber der System-Souveränität, Master-Escrow-Schlüssel, MTK-Treasury
           und obersten Validierungsrechte.
       (b) ERFINDER (Inventor Role):
           Autorisiert zur Erstellung von Quellcode-Repositories, Entwicklung neuer
           Module innerhalb der 28 Kategorien und Einreichung von NEC-Urkunden.
       (c) PARTNER (Partner Role):
           Berechtigt zur operativen Mitverwaltung in Organisationen (©, GbR, eGbR,
           geGbR, Stiftung), Escrow-Prüfung und Validierung.
       (d) KUNDE (Customer / Client Role):
           Nutzungsberechtigt für dezentrale Transaktionen (BTC, ETH, TON), Abruf
           von beglaubigten Abschriften und Inanspruchnahme von Wohnzentren-Leistungen.

    5. HASH-INTEGRITÄT & BLOCK-VERKNÜPFUNG
       Jeder Block der MTK-Blockchain und jeder Commit veröffentlichter Repositories
       führt die Extension-Lizenz-Signatur:
       BLOCK-EXT-LIC-ID: $LICENSE_HASH
  """.trimIndent()
}
