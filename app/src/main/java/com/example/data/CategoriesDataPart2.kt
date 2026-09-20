package com.example.data

import com.example.model.*

object CategoriesDataPart2 {

  private fun defaultBauplan(fachbereich: String): List<BauplanRaum> = listOf(
    BauplanRaum("Raum 1: 1 Studie & Facharchiv", "studie", "45 m²", "Arbeitsplatz für Logbuchführung, theoretische Konzeption & Fachrecherchen"),
    BauplanRaum("Raum 2: Akademie & Seminarbereich", "akademie", "70 m²", "Schulungssaal für Praxiskurse, Methodentraining & Prüfungen"),
    BauplanRaum("Raum 3: Adapter-Verwaltung & Notariat", "adapter", "35 m²", "Ausgabenprüfung, Siegelung & notarielle Tatsachenfeststellung (§ 36 BeurkG)"),
    BauplanRaum("Raum 4: Service- & Beratungsstelle", "service", "50 m²", "Aufnahme von Anliegen, Vergabe physischer NEC-Zertifikate"),
    BauplanRaum("Raum 5: Praxis- & Funktionswerkstatt", "produkt", "65 m²", "Anwendungsorientierte Realisierung, Qualitäts- & Materialtests"),
    BauplanRaum("Raum 6: Dokumenten- & Wertspeicher", "rohstoff", "30 m²", "Archivierung von Notariatsakten, Rohlingen & Schecks"),
    BauplanRaum("Raum 7: Zentraler Park Pavillon", "park", "80 m²", "Begegnungsstätte im Grünen für generationenübergreifende Abstimmungen")
  )

  private fun defaultReferral(): List<ReferralStep> = listOf(
    ReferralStep("E", "Erfinder (Geber)", "Stellt geschöpften MTK-Supply bereit & autorisiert Zertifikate", 0xFF16A34A),
    ReferralStep("P", "Partner (Adapter-Netzwerk)", "Führt Qualifikation durch & dokumentiert im Logbuch", 0xFF2563EB),
    ReferralStep("K", "Kunde / Nehmer", "Nimmt Bildungs- oder Beratungsleistung in Anspruch", 0xFF0A192F),
    ReferralStep("NEC", "Physisches NEC-Zertifikat", "Verbrieft Leistung nach § 36 BeurkG & schließt Kreis zum Erfinder", 0xFFDC2626)
  )

  private fun defaultKapitalfluss(): List<KapitalflussStep> = listOf(
    KapitalflussStep("Erfinder", "0-Kosten-Minting", "Schöpfung von MTK-Währung ohne Fremdkapitalkosten", 0xFF16A34A),
    KapitalflussStep("Adapter-Hülle", "Ausgaben-Verauslagung", "Zahlung von Notar-, Druck- & Raumkosten", 0xFF2563EB),
    KapitalflussStep("Transaktion", "Unberührbare Gebühr", "Automatischer Abzug der Systemgebühr", 0xFF0A192F),
    KapitalflussStep("Gebührenfluss", "MTK-Supply & Wertzuwachs", "Unumkehrbare Rückspeisung in den Währungspool", 0xFFDC2626)
  )

  private fun defaultOrganigramm(kategorieName: String): OrganigrammNode = OrganigrammNode(
    id = "org-root",
    label = "ERFINDER (SUPER-AXIOM)",
    role = "Urheberrecht & Supply",
    children = listOf(
      OrganigrammNode(
        id = "partner-1",
        label = "Partner-Stelle 1: Qualifikation",
        role = "L&Q-Zentrum $kategorieName",
        children = listOf(
          OrganigrammNode("lq-1", "Aktivitäts-Logbuch Büro", "Dokumentation"),
          OrganigrammNode("lq-2", "Prüfungsrat § 36 BeurkG", "Notarielle Feststellung")
        )
      ),
      OrganigrammNode(
        id = "partner-2",
        label = "Partner-Stelle 2: Operativer Adapter",
        role = "Formtrennung & Ausgabenverwaltung",
        children = listOf(
          OrganigrammNode("ad-1", "Ausgaben-Zentrale (0% Steuerlast)", "Buchung"),
          OrganigrammNode("ad-2", "Cube 3b & 3d Schnittstelle", "Treuhand")
        )
      ),
      OrganigrammNode(
        id = "partner-3",
        label = "Partner-Stelle 3: Service & Praxis",
        role = "Kundendialog & Referral",
        children = listOf(
          OrganigrammNode("sp-1", "NEC-Zertifikat Ausgabe", "Sicherheit"),
          OrganigrammNode("sp-2", "Praxis-Werkstatt", "Produktion & Service")
        )
      )
    )
  )

  val categories = listOf(
    // 11. Gesundheit
    Category(
      id = 11,
      name = "Gesundheit",
      kurzname = "Gesundheit",
      rechtsform = "©",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF", "BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "11.1.1",
          name = "Ganzheitliche Medizin & Prävention",
          perspectives = PerspectiveText(
            service = "Präventive Gesundheitsberatung, Ernährungscoaching & Heilpraktik.",
            produkt = "Naturheilkundliche Rezepturen & individuelle Präventionspläne.",
            rohstoff = "Heilkräuter, Mikronährstoffe & reines Quellwasser.",
            erfinder = "Fördert gesundheitliche Selbstbestimmung ohne Pharma-Abhängigkeit.",
            partner = "Betreibt Gesundheitszentren direkt an den Parkanlagen.",
            kunde = "Erreicht dauerhaftes Wohlbefinden und Vitalität.",
            lernen = "Heilberufliche Module & 400 h Praxisführung im Logbuch.",
            finanzen = "Abrechnung über Cube 3c Treuhandguthaben."
          )
        ),
        Subcategory(
          id = "11.1.2",
          name = "Notfallmedizin & Krisenversorgung",
          perspectives = PerspectiveText(
            service = "Ersthelfer-Schulungen, Defibrillator-Netze & Notfalldienst.",
            produkt = "Standardisierte Notfallrucksäcke & Telemedizin-Stationen.",
            rohstoff = "Medizinischer Sauerstoff, Verbandsstoffe & Medikamente.",
            erfinder = "Stellt Währung für die Ausstattung von Rettungspunkten.",
            partner = "Organisiert Bereitschaftsdienste im Wohnzentrum.",
            kunde = "Erfährt lebensrettende Hilfe in Minutenschnelle.",
            lernen = "Rettungssanitäter-Äquivalent im Logbuch: 300 h.",
            finanzen = "Ausgaben-Überschuss durch Bereitstellung von Notfalltechnik."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Gesundheitswesen & Resilienz",
        kategorie = "Gesundheit",
        logbuchPrefix = "LQ-GE",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Ärzte-/Heilbeirat", "Notar (§ 36 BeurkG)", "BMBF"),
        lqNachweis = listOf("Nachweis von 500 präventiven Beratungen", "Logbuch Praxisstunden", "Prüfung"),
        fsNachweis = listOf("Haftpflichtdeckung & Treuhandnachweis", "Cube 3d Garantie"),
        registerFormat = "NEC-MVZ-GE-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Gesundheit"),
      bauplan = defaultBauplan("Gesundheit"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 3, community = 3, city = 3, federal = 2, country = 2, continental = 1, global = 1)
    ),

    // 12. Innere Sicherheit
    Category(
      id = 12,
      name = "Innere Sicherheit",
      kurzname = "Sicherheit",
      rechtsform = "geGbR",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "12.1.1",
          name = "Objektschutz & Dezentrale Sicherheit",
          perspectives = PerspectiveText(
            service = "Sicherheitsanalysen, Zutrittskontrollen & Schutz der Wohnzentren.",
            produkt = "Moderne Zugangssysteme mit physischer NEC-Verifikation.",
            rohstoff = "Sicherheitselektronik, Kameras & physische Schließanlagen.",
            erfinder = "Schützt die physische Integrität der Infrastrukturen.",
            partner = "Stellt geschultes Sicherheitspersonal bereit.",
            kunde = "Lebt und arbeitet in absolut geschützter Atmosphäre.",
            lernen = "Sicherheitsgewerberecht (§ 34a GewO) & Deeskalation: 250 h.",
            finanzen = "Sicherheitskosten als reguläre Adapter-Betriebsausgaben."
          )
        ),
        Subcategory(
          id = "12.1.2",
          name = "Cyber-Security & Kryptografische Souveränität",
          perspectives = PerspectiveText(
            service = "Penetrationstests, Verschlüsselungsaudits & Netzwerkschutz.",
            produkt = "Kryptografische Hardware-Sicherheitsmodule für MTK-Knoten.",
            rohstoff = "Open-Source-Code, kryptografische Algorithmen & Firewalls.",
            erfinder = "Garantiert die Unangreifbarkeit des 0-Kosten-Minting.",
            partner = "Betreibt das Cyber-Defense-Center im IT-Studio.",
            kunde = "Seine Daten und Guthaben sind vor Hackerangriffen sicher.",
            lernen = "Kryptografie & Netzwerksicherheit im Logbuch: 350 h.",
            finanzen = "Investition in Hardwaresicherheit neutralisiert Steuerlast."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Systemintegrität & Sicherheit",
        kategorie = "Innere Sicherheit",
        logbuchPrefix = "LQ-IS",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Sicherheitsdirektor", "Notar", "BMF"),
        lqNachweis = listOf("Auditbericht Systemresilienz", "Logbuch 4 Jahre Schutzpraxis", "Zertifikat"),
        fsNachweis = listOf("Treuhandbesicherung der Sicherheitsinfrastruktur", "Cube 3c Garantien"),
        registerFormat = "MOQ-IS-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Innere Sicherheit"),
      bauplan = defaultBauplan("Innere Sicherheit"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 3, community = 3, city = 3, federal = 3, country = 3, continental = 2, global = 2)
    ),

    // 13. Kultur
    Category(
      id = 13,
      name = "Kultur",
      kurzname = "Kultur",
      rechtsform = "eGbR",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "13.1.1",
          name = "Klassische & Zeitgenössische Künste",
          perspectives = PerspectiveText(
            service = "Organisation von Konzerten, Ausstellungen, Theater & Lesungen.",
            produkt = "Kunstwerke, Musikproduktionen & limitierte Urkunden-Kataloge.",
            rohstoff = "Farben, Leinwände, Musikinstrumente & Tonstudio-Technik.",
            erfinder = "Stiftet Raum für geistige Entfaltung und ästhetische Bildung.",
            partner = "Kuratieren die Galerie- und Konzerträume im Atrium.",
            kunde = "Erlebt Kultur als inspirierenden Teil des täglichen Lebens.",
            lernen = "Kunst- & Musiktheorie im Aktivitäts-Logbuch: 200 h.",
            finanzen = "Kulturfonds speist sich aus unberührbarem Gebührenfluss."
          )
        ),
        Subcategory(
          id = "13.1.2",
          name = "Brauchtum & Handwerkstradition",
          perspectives = PerspectiveText(
            service = "Pflege regionaler Handwerkstechniken (z.B. Holzbildhauerei, Töpferei).",
            produkt = "Handgefertigte Gebrauchs- und Kunstgegenstände.",
            rohstoff = "Ton, regionales Holz, Stein, Textil & Leder.",
            erfinder = "Verbindet traditionelle Meisterschaft mit neuer Ökonomie.",
            partner = "Leitet Lehrwerkstätten für Gesellen und Meister.",
            kunde = "Erhält langlebige, authentische Handwerkskunst.",
            lernen = "Meisterlehre & Werkstattpraxis: 400 h im Logbuch.",
            finanzen = "Kaufabwicklung direkt in MTK ohne Zwischenhändler."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Kultur & Schöpferisches Schaffen",
        kategorie = "Kultur",
        logbuchPrefix = "LQ-KU",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Kulturrat", "Notar (§ 36 BeurkG)", "BMBF"),
        lqNachweis = listOf("Dokumentation von 20 öffentlichen Kulturprojekten", "Logbuch", "Ausstellungskatalog"),
        fsNachweis = listOf("Stiftungsnachweis der uneigennützigen Kulturförderung", "Cube 3b Belege"),
        registerFormat = "NEC-MVZ-KU-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Kultur"),
      bauplan = defaultBauplan("Kultur"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 3, community = 3, city = 3, federal = 2, country = 2, continental = 2, global = 1)
    ),

    // 14. Landwirtschaft und Ernährung
    Category(
      id = 14,
      name = "Landwirtschaft und Ernährung",
      kurzname = "Ernährung",
      rechtsform = "Stiftung",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "14.1.1",
          name = "Regenerative Landwirtschaft & Permakultur",
          perspectives = PerspectiveText(
            service = "Bodenaufbau, Humusbildung & ökologische Fruchtfolgeplanung.",
            produkt = "Frische, giftfreie Lebensmittel in Demeter-/Bioland-Qualität.",
            rohstoff = "Saatgut, Kompost, natürlicher Dünger & Regenwasser.",
            erfinder = "Schützt die Lebensgrundlagen durch nachhaltige Bewirtschaftung.",
            partner = "Bewirtschaftet die Gürtel um die Wohnzentren.",
            kunde = "Ernährt sich und seine Familie mit gesündesten Erzeugnissen.",
            lernen = "Permakultur-Diplom & 350 Stunden Feldarbeit im Logbuch.",
            finanzen = "Direkte Abnahmeverträge sichern Landwirten feste MTK-Erlöse."
          )
        ),
        Subcategory(
          id = "14.1.2",
          name = "Regionale Vorratshaltung & Lebensmittelverarbeitung",
          perspectives = PerspectiveText(
            service = "Haltbarmachung, Fermentation, Müllerei & Bäckereihandwerk.",
            produkt = "Eingemachtes, handwerkliches Sauerteigbrot, Käse & Öle.",
            rohstoff = "Getreide, Früchte, Milch & Kaltpress-Ölsaaten.",
            erfinder = "Garantiert die Nahrungsmittelsouveränität vor Krisen.",
            partner = "Betreibt die Back- und Verarbeitungsstudios im Zentrum.",
            kunde = "Genießt traditionell hergestellte Grundnahrungsmittel.",
            lernen = "Lebensmittelhygiene (HACCP) & Vorratswirtschaft: 200 h.",
            finanzen = "Investitionen in Kühllager über Ausgaben der Adapter-Hülle."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Agrarökologie & Ernährungssouveränität",
        kategorie = "Landwirtschaft und Ernährung",
        logbuchPrefix = "LQ-LE",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Landwirtschaftskammer", "Notar", "BMF"),
        lqNachweis = listOf("Bodenanalysen & Ernteprotokolle über 4 Zyklen", "Logbuch", "Autarkiegutachten"),
        fsNachweis = listOf("Pfandrechtliche Absicherung landwirtschaftlicher Flächen (§ 1274 BGB)", "Cube 3c"),
        registerFormat = "MOQ-LE-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Landwirtschaft & Ernährung"),
      bauplan = defaultBauplan("Landwirtschaft & Ernährung"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 3, community = 3, city = 3, federal = 3, country = 3, continental = 2, global = 1)
    ),

    // 15. Medien, Kommunikation und Informationstechnik
    Category(
      id = 15,
      name = "Medien, Kommunikation und Informationstechnik",
      kurzname = "IT & Medien",
      rechtsform = "©",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF", "BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "15.1.1",
          name = "Dezentrale Kommunikationsnetzwerke",
          perspectives = PerspectiveText(
            service = "Aufbau zensurresistenter Mesh-Netzwerke & verschlüsselter Knoten.",
            produkt = "Sichere Messenger, Peer-to-Peer-Tools & Datenrelais.",
            rohstoff = "Funkfrequenzen, Glasfaser, Router & Open-Source-Protokolle.",
            erfinder = "Sichert den freien und unzensierten Informationsfluss.",
            partner = "Wartet die Antennen und Routerknoten in den Hubs.",
            kunde = "Kommuniziert privat ohne Datenweitergabe an Werbekonzerne.",
            lernen = "Netzwerktechnik & Funklizenzen im Logbuch: 300 h.",
            finanzen = "Netzwerkgebühren fließen direkt in den MTK-Pool zurück."
          )
        ),
        Subcategory(
          id = "15.1.2",
          name = "Verifizierter Journalismus & Dokumentation",
          perspectives = PerspectiveText(
            service = "Rechercheunabhängige Berichterstattung über Systemereignisse.",
            produkt = "Revidierte Faktenchecks, System-Magazine & Videopodcasts.",
            rohstoff = "Primärquellen, Zeugenaussagen & notarielle Belege.",
            erfinder = "Verhindert Propaganda und Desinformation im Ökosystem.",
            partner = "Veröffentlicht monatliche Bulletins im Wohnzentrum.",
            kunde = "Erhält objektive, verlässliche Fakten zur Entscheidungsfindung.",
            lernen = "Presserecht & Quellenschutz im Aktivitäts-Logbuch: 220 h.",
            finanzen = "Honorarzahlung nach Pay after delivering Prinzip via Cube 3c."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für IT-Infrastruktur & Medienrecht",
        kategorie = "Medien, Kommunikation und Informationstechnik",
        logbuchPrefix = "LQ-IT",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "IT-Architekt", "Notar (§ 36 BeurkG)", "BMBF"),
        lqNachweis = listOf("Netzwerkprotokoll-Zertifizierung", "Logbuch 1.500 Betriebsstunden", "Quellcode-Audit"),
        fsNachweis = listOf("Kryptografischer Nachweis der Netzwerksicherheit", "Cube 3d Bürgschaft"),
        registerFormat = "NEC-MVZ-IT-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("IT & Medien"),
      bauplan = defaultBauplan("IT & Medien"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 2, community = 3, city = 3, federal = 3, country = 3, continental = 3, global = 3)
    ),

    // 16. Migration, Flüchtlingspolitik und Integration
    Category(
      id = 16,
      name = "Migration, Flüchtlingspolitik und Integration",
      kurzname = "Migration",
      rechtsform = "geGbR",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF", "BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "16.1.1",
          name = "Qualifizierende Integration & Sprachförderung",
          perspectives = PerspectiveText(
            service = "Intensiv-Sprachkurse, Berufsorientierung & Patenschaften.",
            produkt = "Mehrsprachige Bildungsleitfäden & Qualifikationszertifikate.",
            rohstoff = "Sprachdidaktik, Kulturvermittler & Lehrmaterialien.",
            erfinder = "Integrierte Einbindung durch den fairen 3+1-Jahres-Pfad.",
            partner = "Begleitet Zuwanderer als persönliche Mentoren.",
            kunde = "Findet rasch einen anerkannten Platz in Gesellschaft und Wirtschaft.",
            lernen = "Deutsch als Zweitsprache (DaZ) & Rechtskunde im Logbuch: 350 h.",
            finanzen = "Stiftungsfinanzierte Stipendien ohne staatliche Sozialhilfe."
          )
        ),
        Subcategory(
          id = "16.1.2",
          name = "Rechtliche Statusprüfung & Notarielle Feststellung",
          perspectives = PerspectiveText(
            service = "Aufnahme von Lebensläufen und ausländischen Berufsabschlüssen.",
            produkt = "Notariell beglaubigte Tatsachenfeststellungen nach § 36 BeurkG.",
            rohstoff = "Zeugnisse, Identitätsnachweise & beeidigte Übersetzungen.",
            erfinder = "Schafft unbürokratische Anerkennung auf Leistungsbasis.",
            partner = "Koordiniert die Notariatsprüfung mit staatlichen Behörden.",
            kunde = "Erreicht unverzüglich Rechtsklarheit über seine Qualifikation.",
            lernen = "Migrationsrecht & Beurkundungsgesetz in der Praxis: 200 h.",
            finanzen = "Übernahme aller Notar- und Übersetzungskosten durch Adapter."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Integrationsökonomie",
        kategorie = "Migration, Flüchtlingspolitik und Integration",
        logbuchPrefix = "LQ-MI",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Integrationsrat", "Notar (§ 36 BeurkG)", "BMBF"),
        lqNachweis = listOf("Nachweis von 100 erfolgreichen Integrationsprozessen", "Logbuch", "Sprachprüfung C1"),
        fsNachweis = listOf("Freistellungsbescheid & Stiftungsbericht", "Cube 3b Nachweise"),
        registerFormat = "NEC-MVZ-MI-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Migration & Integration"),
      bauplan = defaultBauplan("Migration & Integration"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 2, community = 3, city = 3, federal = 2, country = 2, continental = 2, global = 2)
    ),

    // 17. Öffentliche Finanzen, Steuern und Abgaben
    Category(
      id = 17,
      name = "Öffentliche Finanzen, Steuern und Abgaben",
      kurzname = "Finanzen & Steuern",
      rechtsform = "Stiftung",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "17.1.1",
          name = "0% Steuerlast-Struktur & Ausgabenoptimierung",
          perspectives = PerspectiveText(
            service = "Prüfung aller Betriebsausgaben zur Gewährleistung des Ausgabenüberschusses.",
            produkt = "Rechtskonforme Ausgabenjournale & Null-Steuer-Erklärungen.",
            rohstoff = "Einkommensteuergesetz (EStG), Abgabenordnung (AO) & Belege.",
            erfinder = "Etabliert ein Modell völliger Legalität ohne unzulässige Einnahmen.",
            partner = "Führt das Einzelunternehmen streng nach dem Adapter-Prinzip.",
            kunde = "Sieht volle Transparenz und keine verdeckten Steuerbelastungen.",
            lernen = "Steuerrecht & Betriebswirtschaft im Logbuch: 300 h.",
            finanzen = "0% Ertragsteuerlast durch dauerhaften Ausgabenüberhang."
          )
        ),
        Subcategory(
          id = "17.1.2",
          name = "Banking Cubes & Treuhandverwaltung",
          perspectives = PerspectiveText(
            service = "Laufendes Monitoring der 4 Cubes (3a-3d) und Scheckverbriefung.",
            produkt = "Treuhandbilanzen & Freigabeprotokolle nach § 1274/1280 BGB.",
            rohstoff = "Kontoauszüge, Notarsiegel & Überweisungsaufträge.",
            erfinder = "Hält sein wirtschaftliches Eigentum in Cube 3a geschützt.",
            partner = "Stellt Transaktionsanträge nach erfolgter Leistung.",
            kunde = "Genießt 100% Insolvenzschutz durch Notartreuhand.",
            lernen = "Treuhandrecht & Bankenaufsichtsrecht im Logbuch: 250 h.",
            finanzen = "Mathematischer Gebührenfluss speist MTK-Supply bei jedem Transfer."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Finanzkybernetik & Steuerarchitektur",
        kategorie = "Öffentliche Finanzen, Steuern und Abgaben",
        logbuchPrefix = "LQ-FS",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Finanzexperte", "Notar", "BMF"),
        lqNachweis = listOf("Auditierung von 100 Adapter-Abschlüssen", "Logbuch 4 Jahre Finanzpraxis", "Thesis"),
        fsNachweis = listOf("Lückenlose Bestätigung der 0%-Steuerlast durch Finanzamt", "Cube 3a-3d Audit"),
        registerFormat = "MOQ-FS-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Öffentliche Finanzen"),
      bauplan = defaultBauplan("Öffentliche Finanzen"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 2, community = 3, city = 3, federal = 3, country = 3, continental = 3, global = 2)
    ),

    // 18. Politisches Leben, Parteien
    Category(
      id = 18,
      name = "Politisches Leben, Parteien",
      kurzname = "Parteien & Politik",
      rechtsform = "GbR",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "18.1.1",
          name = "Parteienunabhängige Bürgeraufklärung",
          perspectives = PerspectiveText(
            service = "Vergleich von Wahlprogrammen, Abstimmungsverhalten & Gesetzesfolgen.",
            produkt = "Neutrale Politik-Kompass-Berichte & Wahlanalysen.",
            rohstoff = "Parteiprogramme, Bundestags-Drucksachen & Haushaltsdaten.",
            erfinder = "Stellt vollständige parteipolitische Neutralität sicher.",
            partner = "Organisiert überparteiliche Podiumsdiskussionen im Park.",
            kunde = "Trifft fundierte politische Entscheidungen ohne Manipulation.",
            lernen = "Parteiengesetz (PartG) & Demokratietheorie im Logbuch: 180 h.",
            finanzen = "Keinerlei Parteispenden oder staatliche Parteienfinanzierung."
          )
        ),
        Subcategory(
          id = "18.1.2",
          name = "Direktdemokratische Entscheidungsfindung",
          perspectives = PerspectiveText(
            service = "Moderation von Bürgerkonventen und soziokratischen Beschlüssen.",
            produkt = "Verbindliche Bürgergutachten für kommunale Bauprojekte.",
            rohstoff = "Konsensmethoden, Diskussionsprotokolle & Bürgerabstimmungen.",
            erfinder = "Fördert das Prinzip wahrer Subsidiarität von unten nach oben.",
            partner = "Führt Bürgerentscheide in den Wohnzentren durch.",
            kunde = "Gestaltet seine direkte Lebensumgebung aktiv und verbindlich mit.",
            lernen = "Moderation & soziokratische Entscheidungsfindung: 200 h.",
            finanzen = "Reine Auslagenübernahme über Adapter-Konto."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Politische Bildung & Bürgerpartizipation",
        kategorie = "Politisches Leben, Parteien",
        logbuchPrefix = "LQ-PL",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Bürgerrat", "Notar (§ 36 BeurkG)", "BMF"),
        lqNachweis = listOf("Organisation von 10 Bürgerkonventen", "Logbuch 1.500 h", "Fachgutachten"),
        fsNachweis = listOf("Nachweis parteipolitischer Unabhängigkeit", "Cube 3b Belege"),
        registerFormat = "NEC-MVZ-PL-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Politisches Leben"),
      bauplan = defaultBauplan("Politisches Leben"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 2, familial = 2, community = 3, city = 3, federal = 3, country = 3, continental = 2, global = 1)
    ),

    // 19. Raumordnung, Bau- und Wohnungswesen
    Category(
      id = 19,
      name = "Raumordnung, Bau- und Wohnungswesen",
      kurzname = "Bau & Wohnen",
      rechtsform = "©",
      ebeneKreis = 2,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "19.1.1",
          name = "Wohnzentren-Architektur & Bauplanung",
          perspectives = PerspectiveText(
            service = "Architekturplanung modularer Wohnzentren mit zentralem Park.",
            produkt = "Genehmigungsfähige Baupläne & ökologische Holzmodul-Konzepte.",
            rohstoff = "Konstruktionsholz, Lehm, Strohdämmung & Naturstein.",
            erfinder = "Schafft lebenswerte, gesunde und bezahlbare Wohnräume.",
            partner = "Koordiniert ausführende Handwerksbetriebe im Studioverbund.",
            kunde = "Bewohnt schadstofffreie, krisensichere Wohnmodule im Grünen.",
            lernen = "Baurecht (BauGB), Statik & ökologisches Bauen im Logbuch: 400 h.",
            finanzen = "Baukosten finanzieren sich über MTK-Kapitalfluss ohne Bankzinsen."
          )
        ),
        Subcategory(
          id = "19.1.2",
          name = "Zentraler Park & Grünflächengestaltung",
          perspectives = PerspectiveText(
            service = "Landschaftsarchitektur, Biotopvernetzung & Regenwassermanagement.",
            produkt = "Erholungsparkanlagen mit Teichen, Spazierwegen & Freiluftbühnen.",
            rohstoff = "Bäume, Sträucher, Wasserpflanzen, Kies & Natursteine.",
            erfinder = "Stellt die Natur als Herzstück jedes Wohnzentrums ins Zentrum.",
            partner = "Pflegt die Parkanlagen im dauerhaften Jahreszyklus.",
            kunde = "Findet Ruhe, Inspiration und Begegnung direkt vor der Haustür.",
            lernen = "Landschaftsökologie & Baumpflege im Studienbuch: 250 h.",
            finanzen = "Pflegekosten sind Ausgabenpositionen der Adapter-Hülle."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Ökologische Raum- & Siedlungsplanung",
        kategorie = "Raumordnung, Bau- und Wohnungswesen",
        logbuchPrefix = "LQ-BW",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Architektenkammer", "Notar", "BMF"),
        lqNachweis = listOf("Entwurf & Bauleitung eines vollständigen Wohnzentrums", "Logbuch 4 Jahre", "Prüfung"),
        fsNachweis = listOf("Grundbuchliche & pfandrechtliche Sicherung aller Liegenschaften", "Cube 3c"),
        registerFormat = "MOQ-BW-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Bau & Wohnen"),
      bauplan = defaultBauplan("Bau & Wohnen"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 3, community = 3, city = 3, federal = 3, country = 3, continental = 2, global = 2)
    )
  )
}
