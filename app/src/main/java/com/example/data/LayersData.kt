package com.example.data

import com.example.model.BankingCube
import com.example.model.Layer
import com.example.model.SignalCoupling

object LayersData {
  val layers = listOf(
    Layer(
      id = "I",
      name = "LAYER I: SUPPLY-EBENE (Exklusiv-Schöpfung)",
      untertitel = "ERFINDER (GEBER) [SUPER-AXIOM]",
      superAxiomNotice = "Absolutes Urheberrecht am Währungs-Supply, 0-Kosten-Minting | Hält MTK Single-Währung",
      bullets = listOf(
        "Absolutes Urheberrecht am Währungs-Supply",
        "0-Kosten-Minting | Hält MTK Single-Währung",
        "Operiert ausschließlich als regulärer Marktteilnehmer unter vielen",
        "Keine Struktur persönlicher Bereicherung, keine unzulässige Publikumsemission"
      ),
      details = "Das Super-Axiom manifestiert die Schöpfungshoheit des Erfinders. Neue Währungseinheiten (MTK) werden ohne Fremdkapitalkosten emittiert und fließen zweckgebunden in das geschlossene Ökosystem. Der Erfinder zieht keinen ungerechtfertigten Sondervorteil, sondern unterwirft sich den identischen Verbriefungs- und Pfandregeln (§ 1274/1280 BGB).",
      signalCouplingId = "SC1"
    ),
    Layer(
      id = "II",
      name = "LAYER II: ADAPTER-EBENE (Formtrennung / Hülle)",
      untertitel = "© EINZELUNTERNEHMEN („REINER ADAPTER\")",
      superAxiomNotice = "Schnittstelle | Nur Ausgaben (keine Einnahmen), 0% Steuerlast-Struktur durch Ausgaben-Überschuss",
      bullets = listOf(
        "Reine Schnittstelle zwischen Erfinder, Rechtsträger und Markt",
        "Ausschließlich Ausgaben (keine betrieblichen Einnahmen)",
        "0% Steuerlast-Struktur durch strukturellen Ausgaben-Überschuss",
        "Hält keine eigenen Währungsbestände zur Vermögensbildung"
      ),
      details = "Das Einzelunternehmen fungiert als reine Ausführungshülle und rechtlicher Stoßdämpfer. Da es vereinbarungsgemäß nur die Kosten für Urkunden, Notare, Infrastruktur und Logistik verauslagt, entsteht ein dauerhafter Ausgabenüberhang, der steuerliche Fehlbelastungen neutralisiert.",
      signalCouplingId = "SC2"
    ),
    Layer(
      id = "III",
      name = "LAYER III: VERBRIEFUNGS- & QUALIFIKATIONS-EBENE",
      untertitel = "STAATLICHE INSTANZ: NOTAR & BUNDESMINISTERIEN (BMF & BMBF)",
      superAxiomNotice = "Scheckverbriefung auf Papier (Pay after delivering) – 4 Cubes & Qualifikation nach § 36 BeurkG",
      bullets = listOf(
        "Scheckverbriefung auf physischem Sicherheitspapier (Pay after delivering)",
        "4 Banking-Cubes: Treugeber- und Treuhandkonten zur sauberen Trennung",
        "Aktivitäts-Logbuch aus der „1 Studie\" zur lückenlosen Dokumentation",
        "Erwerb interner Titel mit uneingeschränkter Gültigkeit nach § 36 BeurkG"
      ),
      details = "Auf Ebene III erfolgt die notarielle Beglaubigung und staatliche Tatsachenfeststellung. Alle Bildungs- und Praxisleistungen werden im Logbuch verzeichnet. Die Treuhandstruktur stellt sicher, dass Werte erst freigegeben werden, nachdem die Leistung nachweislich erbracht wurde (Pay after delivering).",
      signalCouplingId = "SC3"
    ),
    Layer(
      id = "IV",
      name = "LAYER IV: LIQUIDITÄTS-EBENE (Mathematisch garantiert)",
      untertitel = "UNBERÜHRBARER GEBÜHRENFLUSS",
      superAxiomNotice = "Direkte & unumkehrbare Speisung zurück in die Single-Währung MTK, Steigert mathematisch zwingend Liquidität und Supply",
      bullets = listOf(
        "Direkte und unumkehrbare Speisung jeder Systemgebühr zurück in MTK",
        "Steigert mathematisch zwingend Liquidität und Währungssubstanz",
        "Kein Abfluss an externe Spekulanten oder Drittbanken",
        "Linearer, manipulationsresistenter Wertzuwachs pro Systemtransaktion"
      ),
      details = "Jede im Netzwerk ausgelöste Interaktion erzeugt einen festgelegten Gebührenimpuls, der unberührbar und automatisch in den zentralen MTK-Supply rückgeführt wird. Dadurch wächst der innere Gegenwert der Single-Währung proportional zur Systemaktivität.",
      signalCouplingId = null
    )
  )

  val signalCouplings = listOf(
    SignalCoupling(
      id = "SC1",
      name = "Signal Coupling 1 (Layer I ↔ Layer II)",
      hinwegSet = "Hinweg / Set (grün): Aktivierung & Werte-Übertragung vom Erfinder zum Adapter",
      rueckwegReset = "Rückweg / Reset (blau): Pay after delivering (Freigabe & Verifizierung der Verauslagung)",
      details = "Koppelt die monetäre Schöpfungsebene des Erfinders mit dem operativen Einzelunternehmen. Der Hinweg autorisiert die Ressourcen, der Rückweg bestätigt die ordnungsgemäße Umsetzung ohne Mittelverlust."
    ),
    SignalCoupling(
      id = "SC2",
      name = "Signal Coupling 2 (Layer II ↔ Layer III)",
      hinwegSet = "Hinweg / Set (grün): Signalweiterleitung der Ausgaben- und Qualifikationsaufträge",
      rueckwegReset = "Rückweg / Reset (blau): Rückkopplung & Bestätigung der notariellen Beglaubigung",
      details = "Überträgt die Verbriefungsanforderungen an Notare und Ministerien. Der Rückweg meldet die erfolgte Beurkundung nach § 36 BeurkG an die Adapter-Hülle zurück."
    ),
    SignalCoupling(
      id = "SC3",
      name = "Signal Coupling 3 (Layer III ↔ Layer IV)",
      hinwegSet = "Hinweg / Set (grün): Liquiditäts-Trigger nach erfolgreicher Leistungsabnahme",
      rueckwegReset = "Rückweg / Reset (blau): Titel-Akkreditierung & finale Verbriefung der MTK-Wertschöpfung",
      details = "Löst den finalen Gebührenstrom in die Single-Währung MTK aus, sobald Titel verliehen und Schecks eingelöst wurden."
    )
  )

  val rightDockedKundenBlock = mapOf(
    "titel" to "RECHTS-ANGEDOCKTER BLOCK: KUNDEN / NEHMER",
    "subtitel" to "Quereinsteiger & Personal im Referral-System organisiert",
    "kontakt" to "Keinerlei Direktkontakt zum Erfinder oder Kernsystem",
    "zertifikat" to "Ausschließliche Initiierung über physische, handsignierte NEC-Zertifikate",
    "bruecken" to "Sicherheitsbrücke: Direkt zu Indirekt ↔ Indirekt zu Direkt",
    "prinzip" to "Schützt das Kernsystem vor Marktvolatilität und unberechtigten Eingriffen."
  )

  val globalFeedbackLoop = mapOf(
    "name" to "GLOBAL FEEDBACK LOOP (Roter Bogen)",
    "ursprung" to "Layer IV (Liquidität) & Kunden-Block",
    "ziel" to "Layer I (Erfinder / Geber)",
    "bedeutung" to "Geschlossene, unendliche Rückkopplungsschleife. Alle erzeugten Mehrwerte und Gebühren stabilisieren das Super-Axiom und garantieren den kontinuierlichen Wertzuwachs des gesamten Ökosystems."
  )

  val bankingCubes = listOf(
    BankingCube(
      id = "3a",
      name = "Treugeberkonto Erfinder",
      typ = "Treugeber",
      inhaber = "Erfinder (privat)",
      verfuegungsmacht = "Wirtschaftliches Eigentum des Erfinders privat",
      zweck = "Hinterlegung der privaten Vermögenswerte zur Besicherung der initialen Systemschöpfung."
    ),
    BankingCube(
      id = "3b",
      name = "Treugeberkonto Adapter",
      typ = "Treugeber",
      inhaber = "Einzelunternehmen (Adapter)",
      verfuegungsmacht = "Geschäftliche Ausgabenmittel",
      zweck = "Bereitstellung von operativen Mitteln für Urkundendruck, Gebühren, Siegel und Notarkosten."
    ),
    BankingCube(
      id = "3c",
      name = "Treuhandkonto Erfinder",
      typ = "Treuhand",
      inhaber = "Notar / Treuhänder",
      verfuegungsmacht = "Verfügungsmacht des Notars zweckgebunden gesichert",
      zweck = "Sperrkonto für Transaktionswerte des Erfinders. Auszahlung nur nach erbrachtem Verbriefungsnachweis."
    ),
    BankingCube(
      id = "3d",
      name = "Treuhandkonto Adapter",
      typ = "Treuhand",
      inhaber = "Notar / Treuhänder",
      verfuegungsmacht = "Verfügungsmacht des Notars für Abwicklungs-Garantie",
      zweck = "Garantiefonds für Geschäftspartner und Kunden. Schließt das Risiko von Leistungsausfällen aus."
    )
  )
}
