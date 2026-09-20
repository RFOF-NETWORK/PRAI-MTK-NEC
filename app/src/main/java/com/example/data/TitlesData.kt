package com.example.data

import com.example.model.ArenaLevel
import com.example.model.TitleInfo

object TitlesData {
  val titles = listOf(
    TitleInfo(
      id = "nec-mvz",
      name = "NEC Management Verwalter & Zertifizierer",
      zeitpunktJahre = 3,
      aussteller = listOf("Adapter-Einzelunternehmen", "Staatlicher Notar (§ 36 BeurkG)"),
      wirkung = "Befähigt zur eigenständigen Durchführung von Prüfungen, Zertifizierungen von Quereinsteigern und Freigabe physischer NEC-Urkunden im dezentralen Netzwerk.",
      registerFormat = "NEC-MVZ-[Jahr]-[Register-Nr.] (z.B. NEC-MVZ-2026-0842)",
      lqVoraussetzung = listOf(
        "Absolvierung von mindestens 3.000 Praxisstunden im Logbuch",
        "Erfolgreicher Abschluss der Fachmodule 1 bis 14",
        "Nachweis der kontinuierlichen Prozessbegleitung im Job-Zentrum",
        "Notarielle Beglaubigung der Identität und Tätigkeitsnachweise"
      ),
      fsVoraussetzung = listOf(
        "Einwandfreie Führung des Adapter-Ausgabenjournals",
        "Nachweis der korrekten Gebührenabführung in den MTK-Pool",
        "Pfandrechtliche Besicherung nach § 1274/1280 BGB",
        "Hinterlegung der Abwicklungsbürgschaft in Cube 3d"
      )
    ),
    TitleInfo(
      id = "master-quantum",
      name = "Master of Quantum",
      zeitpunktJahre = 4,
      aussteller = listOf("Erfinder", "Staatlicher Notar", "Bundesministerien (BMF & BMBF)"),
      wirkung = "Höchste Akkreditierungsstufe im PRAI / MTK / NEC Ökosystem. Berechtigt zur strategischen Leitung von Wohnzentren, Systemerweiterungen im zweiten Kreis und zur internationalen Vertretung.",
      registerFormat = "MOQ-[Jahr]-[Ebene]-[Register-Nr.] (z.B. MOQ-2027-GLOBAL-0019)",
      lqVoraussetzung = listOf(
        "Vierjährige kontinuierliche Dokumentation in der „1 Studie\"",
        "Mindestens 4.800 verifizierte Praxis- und Forschungsstunden",
        "Master-Thesis zur Systemökonomie oder dezentralen Wertschöpfung",
        "Ministerielle Begutachtung und Anerkennung nach BMBF-Standards"
      ),
      fsVoraussetzung = listOf(
        "Vollständige Integration in den globalen Gebührenfluss",
        "Systemweite Auditierung aller Transaktionsketten",
        "Erfolgreiche Leitung eines eigenständigen Forschungs- oder Wohnzentrums",
        "Verpflichtung auf das unumstößliche Super-Axiom"
      )
    )
  )

  val timelineSteps = listOf(
    mapOf(
      "jahr" to "Jahr 1",
      "titel" to "Einstieg & 1 Studie",
      "status" to "Quereinsteiger / Junior-Partner",
      "desc" to "Eröffnung des Aktivitäts-Logbuchs, Grundausbildung in Systemtheorie, Recht (§§ 36 BeurkG, 1274 BGB) und MTK-Tokenomics."
    ),
    mapOf(
      "jahr" to "Jahr 2",
      "titel" to "Operative Praxis & Job-Zentrum",
      "status" to "NEC-Praktiker & Projektleiter",
      "desc" to "Leitung von Praxisprojekten in der Adapter-Hülle, Begleitung von Kunden-Referrals und Vertiefung der 8 Perspektiven."
    ),
    mapOf(
      "jahr" to "Jahr 3",
      "titel" to "Titel I: NEC Management Verwalter",
      "status" to "Akkreditierter Zertifizierer",
      "desc" to "Prüfung und Verleihung des ersten Haupttitels. Notarielle Beurkundung. Eröffnung eigener Beratungs- und Prüfstellen."
    ),
    mapOf(
      "jahr" to "Jahr 4",
      "titel" to "Titel II: Master of Quantum",
      "status" to "Master of Quantum / System-Architekt",
      "desc" to "Abschluss des 3+1-Pfads. Höchste Auszeichnung mit Einbindung der Bundesministerien. Berechtigung zum Übergang in den zweiten Kreis."
    )
  )
}
