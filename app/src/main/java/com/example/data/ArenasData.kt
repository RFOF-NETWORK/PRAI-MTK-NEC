package com.example.data

import com.example.model.ArenaLevel

object ArenasData {
  val levels = listOf(
    mapOf(
      "key" to "personal",
      "name" to "1. Personal",
      "stufe" to "Ebene 1",
      "sterneMax" to "3",
      "fokus" to "Individuum & Kompetenz",
      "desc" to "Innere Souveränität, fachliche Reife, persönliche Integrität und ethische Verpflichtung gegenüber dem System."
    ),
    mapOf(
      "key" to "familial",
      "name" to "2. Familial",
      "stufe" to "Ebene 2",
      "sterneMax" to "3",
      "fokus" to "Familie & Generationen",
      "desc" to "Familiäre Absicherung, Erhalt geschaffener Werte, Weitergabe von Wissen und generationenübergreifender Schutz."
    ),
    mapOf(
      "key" to "community",
      "name" to "3. Community",
      "stufe" to "Ebene 3",
      "sterneMax" to "3",
      "fokus" to "Gemeinschaft & Quartier",
      "desc" to "Direkte Kollaboration mit Partnern, lokale Netzwerke, Job-Zentren und nachbarschaftliche Synergien."
    ),
    mapOf(
      "key" to "city",
      "name" to "4. City",
      "stufe" to "Ebene 4",
      "sterneMax" to "3",
      "fokus" to "Kommune & Stadt",
      "desc" to "Urbane Integration, Kooperation mit lokalen Behörden, Stadtplanung und wohnraumnahe Arbeitsstudios."
    ),
    mapOf(
      "key" to "federal",
      "name" to "5. Federal",
      "stufe" to "Ebene 5",
      "sterneMax" to "3",
      "fokus" to "Bundesland & Region",
      "desc" to "Regionale Cluster, Kammern, länderspezifische Rechtsstrukturen und Landesministerien."
    ),
    mapOf(
      "key" to "country",
      "name" to "6. Country",
      "stufe" to "Ebene 6",
      "sterneMax" to "3",
      "fokus" to "Nationalstaat (Deutschland)",
      "desc" to "Bundesrechtliche Absicherung, BMF/BMBF-Konformität, Notariatsnetzwerk und Bundesbank-Schnittstellen."
    ),
    mapOf(
      "key" to "continental",
      "name" to "7. Continental",
      "stufe" to "Ebene 7",
      "sterneMax" to "3",
      "fokus" to "Europa (EU)",
      "desc" to "Europäische Harmonisierung, grenzüberschreitende Wohnzentren und kontinentale Wirtschaftsbeziehungen."
    ),
    mapOf(
      "key" to "global",
      "name" to "8. Global",
      "stufe" to "Ebene 8",
      "sterneMax" to "3",
      "fokus" to "Weltweite Skalierung",
      "desc" to "Globale Resilienz, dezentrale Selbstverwaltung, weltweiter MTK-Liquiditätspool und völkerrechtliche Neutralität."
    )
  )

  val wettbewerbsarten = listOf(
    mapOf(
      "titel" to "Effizienz-Challenge",
      "symbol" to "⚡",
      "ziel" to "Optimierung der Prozesslaufzeiten",
      "beschreibung" to "Wettbewerb zwischen Job-Zentren zur Reduktion von Bearbeitungszeiten zwischen Scheckausstellung und notarieller Beurkundung."
    ),
    mapOf(
      "titel" to "Struktur- und Innovations-Pokal",
      "symbol" to "🏆",
      "ziel" to "Höchste Qualität in der 8-Sichten-Matrix",
      "beschreibung" to "Auszeichnung von Partnern, die neue Unterkategorien oder vorbildliche Qualifikationskonzepte erarbeitet haben."
    ),
    mapOf(
      "titel" to "Stufen-Duell",
      "symbol" to "⚔️",
      "ziel" to "Aufstieg im 3+1-Jahres-Pfad",
      "beschreibung" to "Peer-to-Peer Leistungsvergleich in den 14 Ausbildungsmodulen mit gegenseitiger Begutachtung der Logbücher."
    ),
    mapOf(
      "titel" to "Dualer Spaßkampf",
      "symbol" to "🎯",
      "ziel" to "Teamgeist & interdisziplinärer Austausch",
      "beschreibung" to "Kreative Fallstudien-Wettkämpfe zwischen Teams unterschiedlicher Kategorien (z.B. Kultur vs. Energie)."
    )
  )
}
