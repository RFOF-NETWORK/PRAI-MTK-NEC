package com.example.data

import com.example.model.*

object CategoriesDataPart3 {

  private fun defaultBauplan(fachbereich: String): List<BauplanRaum> = listOf(
    BauplanRaum("Raum 1: 1 Studie & Forschungsarchiv", "studie", "45 m²", "Arbeitsplatz für Logbuchführung, theoretische Konzeption & Systemdokumentation"),
    BauplanRaum("Raum 2: Akademie & Kolloquium", "akademie", "70 m²", "Lehrsaal für Fachschulungen, 3+1-Pfad Module & Abschlussprüfungen"),
    BauplanRaum("Raum 3: Adapter-Büro & Notariatsschnittstelle", "adapter", "35 m²", "Belegprüfung, Beglaubigungen nach § 36 BeurkG & Siegelverwaltung"),
    BauplanRaum("Raum 4: Service- & Zertifikatsausgabe", "service", "50 m²", "Kundenempfang, Ausstellung physischer NEC-Urkunden"),
    BauplanRaum("Raum 5: Produkt- & Experimentallabor", "produkt", "65 m²", "Praktische Erprobung, Technologie- & Qualitätstests"),
    BauplanRaum("Raum 6: Rohstoff- & Urkundenarchiv", "rohstoff", "30 m²", "Feuerfestes Archiv für Registerakten, Logbücher & Scheckurkunden"),
    BauplanRaum("Raum 7: Zentraler Park Atrium", "park", "80 m²", "Lichtdurchfluteter Innenhof für interdisziplinären Austausch")
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
    // 20. Recht
    Category(
      id = 20,
      name = "Recht",
      kurzname = "Recht & Notariat",
      rechtsform = "eGbR",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "20.1.1",
          name = "Notarielle Tatsachenfeststellung (§ 36 BeurkG)",
          perspectives = PerspectiveText(
            service = "Beurkundung von Logbüchern, Stundennachweisen & Systemvorgängen.",
            produkt = "Amtliche Urkunden mit Bundessiegel & Eintragung im Notariatsregister.",
            rohstoff = "Beurkundungsgesetz (BeurkG), Dienstsiegel & Urkundenpapier.",
            erfinder = "Verankert das Ökosystem unanfechtbar im staatlichen Rechtsrahmen.",
            partner = "Kooperiert mit zugelassenen Notariaten im Bundesgebiet.",
            kunde = "Besitzt gerichtsfeste Beweise über erbrachte Leistungen.",
            lernen = "Notariatsrecht & Beurkundungspraxis im Logbuch: 350 h.",
            finanzen = "Notargebühren nach GNotKG stellen reguläre Adapter-Ausgaben dar."
          )
        ),
        Subcategory(
          id = "20.1.2",
          name = "Pfandrecht an Rechten (§ 1274 / § 1280 BGB)",
          perspectives = PerspectiveText(
            service = "Konzeptionierung und Anzeige von Pfandrechten zur Wertsicherung.",
            produkt = "Rechtsgültige Pfandverträge & förmliche Zustellungsanzeigen.",
            rohstoff = "Bürgerliches Gesetzbuch (BGB) & Grundbuchordnungen.",
            erfinder = "Schützt Guthaben und Ansprüche vor externen Pfändungen.",
            partner = "Stellt die korrekte Anzeige nach § 1280 BGB sicher.",
            kunde = "Erhält insolvenzresistente Deckung für seine Leistungen.",
            lernen = "Kreditsicherungsrecht & Vollstreckungsabwehr: 280 h.",
            finanzen = "Absicherung der Mittel in Cube 3c & 3d."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Notarielle Verbriefung & Sachenrecht",
        kategorie = "Recht",
        logbuchPrefix = "LQ-RE",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Notarkammer-Vertreter", "Notar (§ 36 BeurkG)", "BMF"),
        lqNachweis = listOf("Volljuristische Fachprüfung oder Äquivalent", "Logbuch 4 Jahre Rechtsbeurkundung", "Urkunden-Sammlung"),
        fsNachweis = listOf("Auditierung aller Pfandakten nach § 1274 BGB", "Bestätigung der Null-Haftung"),
        registerFormat = "MOQ-RE-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Recht"),
      bauplan = defaultBauplan("Recht"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 2, community = 3, city = 3, federal = 3, country = 3, continental = 3, global = 2)
    ),

    // 21. Soziale Sicherung
    Category(
      id = 21,
      name = "Soziale Sicherung",
      kurzname = "Soziales",
      rechtsform = "Stiftung",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF", "BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "21.1.1",
          name = "Solidarische Absicherung & Generationenfonds",
          perspectives = PerspectiveText(
            service = "Absicherung bei Unfall, Krankheit, Erwerbsunfähigkeit & Alter.",
            produkt = "Solidarische Beistandszusagen auf unpfändbarer Treuhandbasis.",
            rohstoff = "Solidarfonds, MTK-Rücklagen & gegenseitige Fürsorgeversprechen.",
            erfinder = "Befreit von der Abhängigkeit an kollabierende Rentensysteme.",
            partner = "Verwaltet den Notfall- und Beistandsfonds vor Ort.",
            kunde = "Lebt in absoluter sozialer Geborgenheit ohne Altersarmut.",
            lernen = "Sozialversicherungsrecht (SGB) & Versicherungsmathematik: 250 h.",
            finanzen = "Finanzierung speist sich direkt aus unberührbarem Gebührenfluss."
          )
        ),
        Subcategory(
          id = "21.1.2",
          name = "Familienbeihilfen & Kinderförderung",
          perspectives = PerspectiveText(
            service = "Auszahlung monatlicher Bildungs- und Entlastungsbeiträge an Familien.",
            produkt = "Direkte Währungszuweisungen für jedes Kind im System.",
            rohstoff = "MTK-Währung, Bildungsgutscheine & Sachwertkarten.",
            erfinder = "Fördert das gesunde Aufwachsen kommender Generationen.",
            partner = "Gibt Kinderkarten im Service-Hub des Wohnzentrums aus.",
            kunde = "Eltern erfahren spürbare materielle und ideelle Entlastung.",
            lernen = "Pädagogische Sozialarbeit im Aktivitäts-Logbuch: 180 h.",
            finanzen = "Reine Ausgabenleistung ohne Steuernachteil."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Solidarische Sicherungssysteme",
        kategorie = "Soziale Sicherung",
        logbuchPrefix = "LQ-SS",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Sozialrat", "Notar (§ 36 BeurkG)", "BMBF"),
        lqNachweis = listOf("Nachweis der Betreuung von 100 Solidarfällen", "Logbuch 1.500 h", "Auditbericht"),
        fsNachweis = listOf("Treuhandabdeckung der Pensionsrückstellungen", "Cube 3a Garantie"),
        registerFormat = "NEC-MVZ-SS-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Soziale Sicherung"),
      bauplan = defaultBauplan("Soziale Sicherung"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 3, community = 3, city = 3, federal = 3, country = 2, continental = 2, global = 1)
    ),

    // 22. Sport, Freizeit und Tourismus
    Category(
      id = 22,
      name = "Sport, Freizeit und Tourismus",
      kurzname = "Sport & Freizeit",
      rechtsform = "©",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "22.1.1",
          name = "Breitensport, Fitness & Arena-Wettkämpfe",
          perspectives = PerspectiveText(
            service = "Ausrichtung von Arena-Duellen, Turnieren & Gesundheitssport.",
            produkt = "Sportstätten im Park, Calisthenics-Parks & Fitnessprogramme.",
            rohstoff = "Sportgeräte, Freiflächen, Tartanbahnen & Sporthallen.",
            erfinder = "Stärkt körperliche Vitalität und gesunden Wettbewerbsgeist.",
            partner = "Organisiert die 4 Wettbewerbsarten (u.a. Dualer Spaßkampf).",
            kunde = "Steigert Fitness, Ausdauer und Teamgeist.",
            lernen = "Sportpädagogik & Trainingslehre im Logbuch: 200 h.",
            finanzen = "Preisgelder werden in MTK-Zertifikaten verliehen."
          )
        ),
        Subcategory(
          id = "22.1.2",
          name = "Sanfter Tourismus & Gästebeherbergung",
          perspectives = PerspectiveText(
            service = "Gästeführungen durch die Wohnzentren, Urlaub auf dem Lande.",
            produkt = "Ökologische Gäste-Lodges mit Blick in den zentralen Park.",
            rohstoff = "Gästezimmer, regionale Biokost & Wanderwege.",
            erfinder = "Öffnet das System für interessierte Außenstehende.",
            partner = "Bewirtschaftet die Beherbergungsstudios im Wohnzentrum.",
            kunde = "Erlebt die Zukunft des Wohnens und Arbeitens hautnah.",
            lernen = "Gastgewerberecht & Tourismusmanagement: 150 h.",
            finanzen = "Einnahmen der Gäste finanzieren laufende Parkpflegeausgaben."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Sportkultur & Erholungswirtschaft",
        kategorie = "Sport, Freizeit und Tourismus",
        logbuchPrefix = "LQ-SF",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Sportbund-Vertreter", "Notar (§ 36 BeurkG)", "BMBF"),
        lqNachweis = listOf("Lizenz A-Trainer oder Äquivalent", "Organisation von 20 Großturnieren", "Logbuch"),
        fsNachweis = listOf("Betriebskostenabrechnung über Adapter-Hülle", "Cube 3b Belege"),
        registerFormat = "NEC-MVZ-SF-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Sport & Freizeit"),
      bauplan = defaultBauplan("Sport & Freizeit"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 3, community = 3, city = 3, federal = 2, country = 2, continental = 2, global = 1)
    ),

    // 23. Staat und Verwaltung
    Category(
      id = 23,
      name = "Staat und Verwaltung",
      kurzname = "Verwaltung",
      rechtsform = "geGbR",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "23.1.1",
          name = "Entbürokratisierung & Schlanke Verwaltung",
          perspectives = PerspectiveText(
            service = "Optimierung von Verwaltungsverfahren & Beseitigung von Vorschriftenstau.",
            produkt = "Standardisierte digitale Aktenführung & Ein-Klick-Bescheinigungen.",
            rohstoff = "Verwaltungsverfahrensgesetz (VwVfG) & Datenregister.",
            erfinder = "Radikale Vereinfachung aller Bürger- und Partnerkontakte.",
            partner = "Leitet das digitale Bürgerbüro im Wohnzentrum.",
            kunde = "Erledigt behördliche Angelegenheiten in wenigen Minuten.",
            lernen = "Verwaltungsrecht & Prozessoptimierung im Logbuch: 250 h.",
            finanzen = "Enorme Kosteneinsparung schont die Stiftungsmittel."
          )
        ),
        Subcategory(
          id = "23.1.2",
          name = "Schnittstellen zu Bundes- & Landesministerien",
          perspectives = PerspectiveText(
            service = "Abstimmung von Akkreditierungsstandards mit BMF und BMBF.",
            produkt = "Anerkannte Berichtsformate & behördliche Kooperationsverträge.",
            rohstoff = "Ministerialerlasse, Haushaltsgesetze & Rechtsgutachten.",
            erfinder = "Stellt volle staatliche Konformität und Rechtssicherheit her.",
            partner = "Führt die Korrespondenz mit den Aufsichtsbehörden.",
            kunde = "Besitzt Titel, die auch extern uneingeschränkt anerkannt werden.",
            lernen = "Ministerialrecht & Föderalismus im Studienbuch: 300 h.",
            finanzen = "Geprüfte Mittelverwendung nach Bundeshaushaltsordnung (BHO)."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Öffentliche Administration & Staatsorganisation",
        kategorie = "Staat und Verwaltung",
        logbuchPrefix = "LQ-SV",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Verwaltungschef", "Notar", "BMF"),
        lqNachweis = listOf("Höherer Verwaltungsdienst-Äquivalent", "Logbuch 4 Jahre Ministerialpraxis", "Thesis"),
        fsNachweis = listOf("Rechnungsprüfungsbericht des BMF", "Cube 3d Garantie"),
        registerFormat = "MOQ-SV-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Staat & Verwaltung"),
      bauplan = defaultBauplan("Staat & Verwaltung"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 2, familial = 1, community = 3, city = 3, federal = 3, country = 3, continental = 2, global = 1)
    ),

    // 24. Umwelt
    Category(
      id = 24,
      name = "Umwelt",
      kurzname = "Umwelt & Natur",
      rechtsform = "Stiftung",
      ebeneKreis = 2,
      notarRelevant = true,
      ministerien = listOf("BMF", "BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "24.1.1",
          name = "Artenschutz, Renaturierung & Waldaufbau",
          perspectives = PerspectiveText(
            service = "Wiederaufforstung klimaresistenter Mischwälder & Moorrenaturierung.",
            produkt = "Intakte Ökosysteme, CO2-Senken & Trinkwasserschutzgebiete.",
            rohstoff = "Baumsetzlinge, Torfmoose, Totholz & heimische Tierarten.",
            erfinder = "Sichert die Bewahrung der Schöpfung für alle Zeitalter.",
            partner = "Führt Renaturierungsprojekte im Umfeld der Wohnzentren durch.",
            kunde = "Genießt reine Luft, sauberes Wasser und blühende Landschaften.",
            lernen = "Forstwissenschaft & Naturschutzbiologie im Logbuch: 350 h.",
            finanzen = "Stiftungsfinanzierte Flächenkäufe zur dauerhaften Stilllegung."
          )
        ),
        Subcategory(
          id = "24.1.2",
          name = "Kreislaufwirtschaft & Zero-Waste-Konzepte",
          perspectives = PerspectiveText(
            service = "Vollständiges stoffliches Recycling aller Abfallströme.",
            produkt = "Wiederaufbereitete Werkstoffe, Kompost & Sekundärrohstoffe.",
            rohstoff = "Sortenreiner Kunststoff, Metalle, Glas & organische Abfälle.",
            erfinder = "Beendet das Zeitalter der Wegwerfwirtschaft durch Kreisläufe.",
            partner = "Betreibt die Recycling- und Upcycling-Studios.",
            kunde = "Hinterlässt einen minimalen ökologischen Fußabdruck.",
            lernen = "Abfallwirtschaftsrecht & Verfahrenstechnik: 200 h.",
            finanzen = "Gleichzeitige Kostensenkung durch Materialwiederverwendung."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Ökologische Biosphären-Regeneration",
        kategorie = "Umwelt",
        logbuchPrefix = "LQ-UM",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Naturschutzbeirat", "Notar", "BMBF"),
        lqNachweis = listOf("Nachweis der Renaturierung von mindestens 50 Hektar", "Logbuch 4 Jahre", "Audit"),
        fsNachweis = listOf("Grundbuchlicher Eintrag von Naturschutz-Dienstbarkeiten", "Cube 3c"),
        registerFormat = "MOQ-UM-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Umwelt"),
      bauplan = defaultBauplan("Umwelt"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 3, community = 3, city = 3, federal = 3, country = 3, continental = 3, global = 3)
    ),

    // 25. Verkehr
    Category(
      id = 25,
      name = "Verkehr",
      kurzname = "Mobilität",
      rechtsform = "©",
      ebeneKreis = 2,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "25.1.1",
          name = "Schienenverkehr & Multimodale Korridore",
          perspectives = PerspectiveText(
            service = "Anbindung der Wohnzentren an das überregionale Bahn- und Güternetz.",
            produkt = "Eigene Haltepunkte, Güterterminals & automatisierte Shuttles.",
            rohstoff = "Gleisanlagen, Stahl, Oberleitungen & Elektrofahrzeuge.",
            erfinder = "Ermöglicht emissionsfreie Mobilität im gesamten Bundesgebiet.",
            partner = "Koordiniert den Fahrbetrieb und Flottenunterhalt.",
            kunde = "Reist komfortabel, staufrei und schnell zwischen den Zentren.",
            lernen = "Eisenbahnbetriebsleiter-Zertifikat & Streckenpraxis: 300 h.",
            finanzen = "Infrastrukturkosten mindern Steuerlast auf 0%."
          )
        ),
        Subcategory(
          id = "25.1.2",
          name = "E-Mobilität, Lastenradnetze & Mikromobilität",
          perspectives = PerspectiveText(
            service = "Bereitstellung von geteilten E-Autos, Lastenrädern und Ladeinfrastruktur.",
            produkt = "Verleihstationen im Park mit automatischer Buchung via App.",
            rohstoff = "Lastenfahrräder, Batteriezellen, Aluminium & Solardächer.",
            erfinder = "Schafft autofreie, ruhige Innenbereiche in den Wohnzentren.",
            partner = "Wartet die Fahrzeugflotte im Werkstatt-Studio.",
            kunde = "Nutzt jederzeit das passende Fahrzeug ohne Anschaffungskosten.",
            lernen = "Zweiradmechanik & Batterietechnologie im Logbuch: 180 h.",
            finanzen = "Minutengenaue MTK-Abrechnung fließt in Gebührenpool."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Zukunftsfähige Mobilitätssysteme",
        kategorie = "Verkehr",
        logbuchPrefix = "LQ-VK",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Verkehrsplaner", "Notar (§ 36 BeurkG)", "BMF"),
        lqNachweis = listOf("Mobilitätskonzept für 2 Wohnzentren", "Logbuch 1.500 Betriebsstunden", "Sicherheitszertifikat"),
        fsNachweis = listOf("Flottenfinanzierungsnachweis über Adapter-Hülle", "Cube 3b Belege"),
        registerFormat = "NEC-MVZ-VK-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Verkehr"),
      bauplan = defaultBauplan("Verkehr"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 2, community = 3, city = 3, federal = 3, country = 3, continental = 2, global = 1)
    ),

    // 26. Verteidigung
    Category(
      id = 26,
      name = "Verteidigung",
      kurzname = "Zivilschutz",
      rechtsform = "GbR",
      ebeneKreis = 2,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "26.1.1",
          name = "Ziviler Katastrophenschutz & Krisenvorsorge",
          perspectives = PerspectiveText(
            service = "Schutz bei Naturkatastrophen, Hochwasser, Stromausfall & Unwettern.",
            produkt = "Krisenfeste Schutzbauten, Vorratslager & autonome Brunnen.",
            rohstoff = "Notstromaggregate, Wasseraufbereitung, Funkgeräte & Deiche.",
            erfinder = "Garantiert das Überleben der Gemeinschaft in extremen Notlagen.",
            partner = "Bildet freiwillige Hilfskorps im 3+1-Pfad aus.",
            kunde = "Ist zu jeder Zeit vor elementaren Gefahren geschützt.",
            lernen = "Katastrophenschutzrecht & Taktische Führung im Logbuch: 320 h.",
            finanzen = "Stiftungsfinanzierte Notfallreserven auf Cube 3a."
          )
        ),
        Subcategory(
          id = "26.1.2",
          name = "Resilienz & Strategische Schutzinfrastrukturen",
          perspectives = PerspectiveText(
            service = "Härtung kritischer Netze gegen elektromagnetische Impulse (EMP).",
            produkt = "Faradaysche Schutzkäfige & geschützte Serverräume.",
            rohstoff = "Kupferabschirmungen, Filteranlagen & redundant ausgelegte Leitungen.",
            erfinder = "Schützt die Lebensnerven des dezentralen Ökosystems.",
            partner = "Überwacht die Resilienz aller technischen Knoten.",
            kunde = "Genießt unterbrechungsfreie Versorgung auch bei Blackouts.",
            lernen = "Hochfrequenztechnik & baulicher Zivilschutz: 250 h.",
            finanzen = "Ausgaben-Verauslagung über Adapter-Konto 3b."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Zivilen Bevölkerungsschutz & Resilienz",
        kategorie = "Verteidigung",
        logbuchPrefix = "LQ-VT",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Katastrophenschutz-Beirat", "Notar", "BMF"),
        lqNachweis = listOf("Leitung von 5 Katastrophenschutzübungen", "Logbuch 4 Jahre", "Resilienzprüfung"),
        fsNachweis = listOf("Auditierung der Krisen-Garantiefonds", "Cube 3d Freigaben"),
        registerFormat = "MOQ-VT-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Verteidigung"),
      bauplan = defaultBauplan("Verteidigung"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 2, familial = 3, community = 3, city = 3, federal = 3, country = 3, continental = 2, global = 2)
    ),

    // 27. Wirtschaft
    Category(
      id = 27,
      name = "Wirtschaft",
      kurzname = "Wirtschaft & Handel",
      rechtsform = "geGbR",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "27.1.1",
          name = "Dezentraler Handel & MTK-Zahlungsökonomie",
          perspectives = PerspectiveText(
            service = "Betrieb des internen Marktplatzes für Waren, Dienstleistungen & Talente.",
            produkt = "Vollständig integrierte MTK-Wallets & Kassen-Schnittstellen.",
            rohstoff = "Wirtschaftsgüter, Produktionskapazitäten & Lieferverträge.",
            erfinder = "Beseitigt Inflation und Finanzspekulation durch Single-Währung.",
            partner = "Vermittelt Transaktionen zwischen Handwerkern und Abnehmern.",
            kunde = "Kauft und verkauft ohne Bankgebühren und ohne Währungsverlust.",
            lernen = "Volkswirtschaftslehre (VWL) & Tokenomics im Logbuch: 300 h.",
            finanzen = "Unberührbare Gebühr stärkt bei jedem Kauf den MTK-Supply."
          )
        ),
        Subcategory(
          id = "27.1.2",
          name = "Mittelstandsförderung & Handwerkerverbünde",
          perspectives = PerspectiveText(
            service = "Gründungsbegleitung, Einkaufsbündelung & gemeinsame Werkzeugnutzung.",
            produkt = "Genossenschaftliche Werkzeugpools & Adapter-Partnerschaften.",
            rohstoff = "CNC-Maschinen, 3D-Drucker, Rohmaterialien & Hallen.",
            erfinder = "Macht kleine und mittlere Betriebe krisen- und insolvenzfest.",
            partner = "Verbindet regionale Betriebe zum produktiven Netzwerk.",
            kunde = "Erhält erstklassige handwerkliche Qualität aus der Region.",
            lernen = "Betriebsführung im Handwerk & Meisterrecht: 250 h.",
            finanzen = "0% Steuerlast-Struktur schützt Gewinne vor Zwangsvollstreckung."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Dezentrale Realwirtschaft & Handel",
        kategorie = "Wirtschaft",
        logbuchPrefix = "LQ-WI",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Wirtschaftskammer", "Notar", "BMF"),
        lqNachweis = listOf("Bilanzprüfung von 50 Partnerbetrieben", "Logbuch 4 Jahre Handelspraxis", "Thesis"),
        fsNachweis = listOf("Nachweis der stabilen MTK-Kaufkraft", "Cube 3b/3d Audit"),
        registerFormat = "MOQ-WI-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Wirtschaft"),
      bauplan = defaultBauplan("Wirtschaft"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 2, community = 3, city = 3, federal = 3, country = 3, continental = 3, global = 2)
    ),

    // 28. Wissenschaft, Forschung und Technologie
    Category(
      id = 28,
      name = "Wissenschaft, Forschung und Technologie",
      kurzname = "Wissenschaft & Quanten",
      rechtsform = "©",
      ebeneKreis = 2,
      notarRelevant = true,
      ministerien = listOf("BMF", "BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "28.1.1",
          name = "Quantenmechanik, Kybernetik & Grundlagenforschung",
          perspectives = PerspectiveText(
            service = "Erforschung komplexer Systeme, Quanteninformatik & Systemtheorie.",
            produkt = "Wissenschaftliche Monografien, Peer-Reviewed Papers & Prototypen.",
            rohstoff = "Quantencomputer-Simulatoren, Messgeräte & theoretische Modelle.",
            erfinder = "Entwickelt das mathematische Fundament des Super-Axioms weiter.",
            partner = "Leitet das Forschungsinstitut im zweiten Kreis.",
            kunde = "Profitiert von bahnbrechenden Technologien für Energie und Medizin.",
            lernen = "Theoretische Physik & höhere Mathematik im Studienbuch: 450 h.",
            finanzen = "Stiftungsfinanzierte Spitzenforschung ohne Drittmitteleinfluss."
          )
        ),
        Subcategory(
          id = "28.1.2",
          name = "Technologietransfer in die 28 Kategorien",
          perspectives = PerspectiveText(
            service = "Praktische Überführung neuester Forschungsergebnisse in alle Studios.",
            produkt = "Anwendungsreife Patente, Open-Source-Pläne & Fertigungsstraßen.",
            rohstoff = "Halbleiter, Sensorik, Software-Frameworks & Laborausrüstung.",
            erfinder = "Garantiert den kontinuierlichen technologischen Vorsprung.",
            partner = "Schult Partner der anderen 27 Kategorien in neuen Verfahren.",
            kunde = "Nutzt weltweit fortschrittlichste Produkte und Dienstleistungen.",
            lernen = "Technologietransfer & Innovationsmanagement: 300 h.",
            finanzen = "Reinvestition von Lizenzgebühren in den MTK-Währungspool."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Systemtheorie, Kybernetik & Technologie",
        kategorie = "Wissenschaft, Forschung und Technologie",
        logbuchPrefix = "LQ-WF",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Forschungsrats-Präsident", "Notar", "BMBF"),
        lqNachweis = listOf("Dissertation zur Systemkybernetik", "Logbuch 4 Jahre Laborforschung", "Ministerielle Anerkennung"),
        fsNachweis = listOf("Vollständiges Audit des unberührbaren Gebührenflusses", "Cube 3a-3d Siegel"),
        registerFormat = "MOQ-WF-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Wissenschaft & Technologie"),
      bauplan = defaultBauplan("Wissenschaft & Technologie"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 1, community = 3, city = 3, federal = 3, country = 3, continental = 3, global = 3)
    )
  )
}
