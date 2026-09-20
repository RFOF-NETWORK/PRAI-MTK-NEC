package com.example.data

import com.example.model.WohnzentrumModul

object KreiseData {
  val kreis1Info = mapOf(
    "titel" to "ERSTER KREIS: DIE ERSTEN 4 JAHRE (FUNDAMENT)",
    "untertitel" to "Ausbildung, Qualifikation & rechtliche Festigung in Deutschland",
    "status" to "Quereinsteiger → Partner → Master of Quantum",
    "kernpunkte" to listOf(
      "Basis der Entwicklung: Die „1 Studie\" und das lückenlose Aktivitäts-Logbuch",
      "3+1-Jahres-Pfad: Nach 3 Jahren Titel I (NEC-MVZ), nach 4 Jahren Titel II (Master of Quantum)",
      "Rechtliche Verankerung: Notarielle Tatsachenfeststellung nach § 36 BeurkG",
      "Finanzielle Disziplin: Pfandrechtliche Sicherung nach § 1274/1280 BGB",
      "Übertritt: Erst nach Erreichen des 4. Jahres ist der Übertritt in den zweiten Kreis möglich"
    ),
    "philosophie" to "Im ersten Kreis wird das unerschütterliche Fundament gegossen. Jeder Teilnehmer durchdringt die 8 Perspektiven in seiner Kategorie und beweist absolute Zuverlässigkeit in der dezentralen Abwicklung."
  )

  val kreis2Info = mapOf(
    "titel" to "ZWEITER KREIS: DAS EIGENE ÖKOSYSTEM (SKALIERUNG)",
    "untertitel" to "Autonome Wohn- und Arbeitszentren: Deutschland → EU → Global",
    "status" to "Vollständige Selbstverwaltung & krisensichere Lebensräume",
    "kernpunkte" to listOf(
      "Struktureller Aufbau: Ausgehend von deutschen Modellzentren über die EU zur weltweiten Vernetzung",
      "Architektur: Zentraler Park in der Mitte, umschlossen von Arbeitsstudios, Forschungsräumen & Wohnungen",
      "Drei Säulen: Wirtschaft/Verwaltung/Recht, Bildung/Forschung/Wissenschaft, Gesundheit/Soziales/Kultur",
      "Ergebnis: Geschlossener, krisen- und inflationsresistenter Lebens- und Arbeitsraum",
      "Währungshoheit: Vollkommene Abwicklung über die MTK-Single-Währung mit unberührbarem Gebührenfluss"
    ),
    "philosophie" to "Der zweite Kreis realisiert die Vision des autarken Lebensraums. Er verbindet Natur (zentraler Park), Wissenschaft, Wirtschaft und Wohnen zu einer synergetischen Einheit."
  )

  val wohnzentren = listOf(
    WohnzentrumModul(
      name = "Zentrales Wohnzentrum Alpha",
      standort = "Deutschland (Federal Citys)",
      typ = "Hauptstandort & Modellzentrum",
      beschreibung = "Referenzarchitektur mit 12 Hektar zentralem Park, 8 Arbeitsstudios, eigener Akademie und notarieller Beglaubigungsstelle.",
      parkFlaeche = "120.000 m²",
      kapazitaet = "1.200 Partner & Familien"
    ),
    WohnzentrumModul(
      name = "Campus Rhein-Main Europa",
      standort = "Deutschland / Hessen",
      typ = "Finanz- & Technologiestudio",
      beschreibung = "Schwerpunkt auf Tokenomics, MTK-Liquiditätsüberwachung und bundesweiter Koordination der Adapter-Einzelunternehmen.",
      parkFlaeche = "85.000 m²",
      kapazitaet = "850 Partner"
    ),
    WohnzentrumModul(
      name = "Wohnzentrum Continental Nord",
      standort = "Europäische Union (EU)",
      typ = "Kontinentale Schnittstelle",
      beschreibung = "Grenzüberschreitende Kooperation für europäische Rechtsharmonisierung und Bildungsstandards nach BMBF-Äquivalent.",
      parkFlaeche = "150.000 m²",
      kapazitaet = "1.800 Partner"
    ),
    WohnzentrumModul(
      name = "Global Resilience Habitat",
      standort = "Global (Skalierungsphase)",
      typ = "Autonomes Zukunfts-Ökosystem",
      beschreibung = "Vollständig energieautarkes Zentrum mit geschlossenen Stoffkreisläufen, agroforstwirtschaftlichem Ringpark und Quantum-Lab.",
      parkFlaeche = "300.000 m²",
      kapazitaet = "3.500 Partner"
    )
  )

  val moduleArchitektur = listOf(
    mapOf("titel" to "Zentraler Park", "desc" to "Das grüne Herzstück der Anlage: Erholungsraum, Mikroklima-Regulator und Treffpunkt für alle Generationen."),
    mapOf("titel" to "Integrierte Arbeitsstudios", "desc" to "Hochmoderne digitale und physische Werkstätten für die 28 Kategorien, direkt an den Park angrenzend."),
    mapOf("titel" to "Forschungs- und Bildungsräume", "desc" to "Akademien für den 3+1-Pfad mit direktem Zugriff auf die Studien-Archive und Fachbibliotheken."),
    mapOf("titel" to "Verwaltungs- und Notariate", "desc" to "Staatlich anerkannte Schnittstellen für § 36 BeurkG Tatsachenfeststellungen und Urkundenausgabe."),
    mapOf("titel" to "Wohnmodule & Familienresidenzen", "desc" to "Schallisolierte, nachhaltige Wohngebäude mit Blick in den Park, harmonisch in das Gesamtensemble integriert.")
  )
}
