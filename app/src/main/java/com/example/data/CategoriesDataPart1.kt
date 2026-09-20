package com.example.data

import com.example.model.*

object CategoriesDataPart1 {

  private fun defaultBauplan(fachbereich: String): List<BauplanRaum> = listOf(
    BauplanRaum("Raum 1: 1 Studie & Forschung", "studie", "45 m²", "Arbeitsplatz für Logbuchführung, theoretische Konzeption & Fallanalysen"),
    BauplanRaum("Raum 2: NEC-Akademie & Schulung", "akademie", "70 m²", "Lehrsaal für Quereinsteiger, Modulunterricht & Peer-Learning"),
    BauplanRaum("Raum 3: Adapter-Büro & Notariat", "adapter", "35 m²", "Verwaltung der Ausgabenbelege, Siegelprüfung & Notarabstimmung (§ 36 BeurkG)"),
    BauplanRaum("Raum 4: Service-Hub", "service", "50 m²", "Kundenaufnahme, Beratungsgespräche & Ausgabe physischer Zertifikate"),
    BauplanRaum("Raum 5: Produkt- & Materialwerkstatt", "produkt", "65 m²", "Herstellung standardisierter Arbeitsmittel & Qualitätssicherung"),
    BauplanRaum("Raum 6: Rohstoff- & Dokumentenlager", "rohstoff", "30 m²", "Archiv für Logbücher, Scheckformulare & physische Urkundenrohlinge"),
    BauplanRaum("Raum 7: Grüner Atrium-Garten", "park", "80 m²", "Erholungszone & interdisziplinäre Dialoge nach dem Park-Prinzip")
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
    // 1. Arbeit und Beschäftigung
    Category(
      id = 1,
      name = "Arbeit und Beschäftigung",
      kurzname = "Arbeit",
      rechtsform = "©",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF", "BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "1.1.1",
          name = "Arbeitsvermittlung & Quereinstieg",
          perspectives = PerspectiveText(
            service = "Individuelle Berufsberatung, Talent-Screening & modulare Vermittlung.",
            produkt = "Standardisierte Quereinsteiger-Profile & Kompetenzzertifikate.",
            rohstoff = "Menschliche Arbeitskraft, Zeitkontingente & Erfahrungsschätze.",
            erfinder = "Stellt Währungs-Supply für Ausbildungsstipendien bereit.",
            partner = "Leitet das Job-Zentrum & betreut Bewerber persönlich.",
            kunde = "Erhält transparente Einstiegschancen ohne Vorkenntnisse.",
            lernen = "Pflichtmodul L&Q: 120 h Logbuch-Dokumentation von Beratungssituationen.",
            finanzen = "Vergütung nach dem Pay-after-delivering Prinzip via Cube 3c."
          )
        ),
        Subcategory(
          id = "1.1.2",
          name = "Arbeitsrecht & Tarifwesen",
          perspectives = PerspectiveText(
            service = "Rechtsberatung für faire Arbeitsbedingungen & Vergütungsmodelle.",
            produkt = "Muster-Arbeitsverträge mit integrierter MTK-Vergütungsklausel.",
            rohstoff = "Arbeitszeitgesetz, Tarifverträge & Notariatsstatuten.",
            erfinder = "Sichert die rechtliche Konformität mit dem Super-Axiom.",
            partner = "Stellt Verträge rechtssicher auf Adapter-Ebene aus.",
            kunde = "Profitiert von gesicherten Standards und klaren Leistungsansprüchen.",
            lernen = "Vertiefung in § 36 BeurkG und Pfandrecht nach § 1274 BGB.",
            finanzen = "Abrechnung rein über Ausgabenposten der Adapter-Hülle."
          )
        ),
        Subcategory(
          id = "1.1.3",
          name = "Arbeitsschutz & Betriebliche Gesundheit",
          perspectives = PerspectiveText(
            service = "Ergonomie-Audits, Stressprävention & Sicherheitsbegehungen.",
            produkt = "Betriebliche Gesundheitsleitfäden & Schutzausrüstungs-Sets.",
            rohstoff = "Präventionsrichtlinien der Berufsgenossenschaften.",
            erfinder = "Fördert resiliente Arbeitsumgebungen im Ökosystem.",
            partner = "Führt jährliche Audits in allen Arbeitsstudios durch.",
            kunde = "Erlebt gesunde und wertschätzende Arbeitsbedingungen.",
            lernen = "Zertifikatskurs: 80 Praxisstunden Sicherheitsbeauftragter.",
            finanzen = "Ausgabenüberschuss durch Investition in Arbeitsmittel."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Arbeit & Beschäftigung",
        kategorie = "Arbeit und Beschäftigung",
        logbuchPrefix = "LQ-AB",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Adapter-Inhaber", "Notar (§ 36 BeurkG)", "BMBF-Prüfer"),
        lqNachweis = listOf("Modul 1-4 Arbeitsmarktökonomie", "Aktivitäts-Logbuch Band 1-3", "300 dokumentierte Vermittlungen"),
        fsNachweis = listOf("Pfandurkunde nach § 1274 BGB", "Nachweis über 0% Steuerlast-Struktur", "Cube 3b Belegsatz"),
        registerFormat = "NEC-MVZ-AB-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Arbeit & Beschäftigung"),
      bauplan = defaultBauplan("Arbeit & Beschäftigung"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 3, community = 3, city = 3, federal = 2, country = 2, continental = 1, global = 1)
    ),

    // 2. Außenpolitik und internationale Beziehungen
    Category(
      id = 2,
      name = "Außenpolitik und internationale Beziehungen",
      kurzname = "Außenpolitik",
      rechtsform = "Stiftung",
      ebeneKreis = 2,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "2.1.1",
          name = "Diplomatische Beziehungen & Friedensförderung",
          perspectives = PerspectiveText(
            service = "Moderation internationaler Kooperationsgespräche & Friedensdialoge.",
            produkt = "Multilaterale Absichtserklärungen & Kooperationsabkommen.",
            rohstoff = "Völkerrecht, UN-Charta & humanitäre Konventionen.",
            erfinder = "Stellt neutrale, nicht-staatliche Verhandlungskanäle bereit.",
            partner = "Koordiniert ausländische Kontaktstellen im zweiten Kreis.",
            kunde = "Gewinnt verlässliche Rahmenbedingungen im Ausland.",
            lernen = "Interkulturelle Kompetenz & Völkerrecht im Logbuch verzeichnet.",
            finanzen = "Stiftungsgebundene Finanzierung ohne Parteipolitik."
          )
        ),
        Subcategory(
          id = "2.1.2",
          name = "Internationale Kultur- & Bildungsabkommen",
          perspectives = PerspectiveText(
            service = "Austauschprogramme für Forscher und Studierende im 3+1-Pfad.",
            produkt = "Akkreditierte Bildungsäquivalenzen & bilaterale Stipendien.",
            rohstoff = "Bildungsstandards & internationale Sprachzertifikate.",
            erfinder = "Sichert die weltweite Gültigkeit des Master of Quantum.",
            partner = "Betreibt internationale Gaststudios im Wohnzentrum.",
            kunde = "Erwirbt global anerkannte Qualifikationsnachweise.",
            lernen = "Auslandssemester mit 400 protokollierten Studien-Stunden.",
            finanzen = "Gleichzeitige Speisung des kontinentalen Gebührenflusses."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Internationale Beziehungen",
        kategorie = "Außenpolitik und internationale Beziehungen",
        logbuchPrefix = "LQ-AP",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Stiftungsrat", "Notar", "BMF"),
        lqNachweis = listOf("Internationales Verhandlungsdiplom", "Forschungsarbeit zu dezentraler Diplomatie", "Logbuch Auslandspraxis"),
        fsNachweis = listOf("Auditierung grenzüberschreitender Geldflüsse", "MTK-Liquiditätsnachweis Stufe 4"),
        registerFormat = "MOQ-AP-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Außenpolitik"),
      bauplan = defaultBauplan("Außenpolitik"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 1, familial = 1, community = 2, city = 2, federal = 2, country = 3, continental = 3, global = 3)
    ),

    // 3. Außenwirtschaft
    Category(
      id = 3,
      name = "Außenwirtschaft",
      kurzname = "Außenwirtschaft",
      rechtsform = "GbR",
      ebeneKreis = 2,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "3.1.1",
          name = "Export- & Import-Abwicklung",
          perspectives = PerspectiveText(
            service = "Zolltechnische Begleitung & logistische Routenoptimierung.",
            produkt = "Rechtssichere Außenhandelsverträge mit MTK-Valuta.",
            rohstoff = "Welthandelsgüter, Frachtpapiere & Ursprungszeugnisse.",
            erfinder = "Bietet Schutz vor Währungsschwankungen durch MTK-Single-Währung.",
            partner = "Verbindet lokale Erzeuger mit internationalen Partnern.",
            kunde = "Kauft und verkauft ohne teure Devisenumrechnungsgebühren.",
            lernen = "Zollrecht & Incoterms im Logbuch: 250 Praxisstunden.",
            finanzen = "Mathematischer Gebührenfluss bei jeder Außenhandelstransaktion."
          )
        ),
        Subcategory(
          id = "3.1.2",
          name = "Direktinvestitionen & Handelsabkommen",
          perspectives = PerspectiveText(
            service = "Standortanalysen & Absicherung ausländischer Investitionen.",
            produkt = "Investment-Memoranden mit besicherten Pfandrechten (§ 1274 BGB).",
            rohstoff = "Kapital, Produktionsanlagen & Grundstücke im Ausland.",
            erfinder = "Überwacht die Einhaltung des Super-Axioms gegen Ausbeutung.",
            partner = "Baut Wohnzentren mit Partnernetzwerken vor Ort auf.",
            kunde = "Investiert nachhaltig mit garantierter Risikominimierung.",
            lernen = "Analyse internationaler Handelsverträge in der '1 Studie'.",
            finanzen = "Einbindung in Cube 3c Treuhandgarantien."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Außenwirtschaft",
        kategorie = "Außenwirtschaft",
        logbuchPrefix = "LQ-AW",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Adapter", "Notar (§ 36 BeurkG)", "BMF"),
        lqNachweis = listOf("Handelsfachwirt-Äquivalent", "Logbuch Außenhandel 500 Transaktionen"),
        fsNachweis = listOf("Zoll- und Steuerfreistellungsbescheid", "Cube 3d Garantie-Einlage"),
        registerFormat = "NEC-MVZ-AW-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Außenwirtschaft"),
      bauplan = defaultBauplan("Außenwirtschaft"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 2, familial = 1, community = 2, city = 2, federal = 3, country = 3, continental = 3, global = 3)
    ),

    // 4. Bildung und Erziehung
    Category(
      id = 4,
      name = "Bildung und Erziehung",
      kurzname = "Bildung",
      rechtsform = "eGbR",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "4.1.1",
          name = "Die '1 Studie' & Didaktik",
          perspectives = PerspectiveText(
            service = "Pädagogische Begleitung beim Aufbau der individuellen Studie.",
            produkt = "Gedruckte und gebundene Studienwerke mit Urkundenanhang.",
            rohstoff = "Wissenschaftliche Literatur, Primärquellen & Erkenntnisse.",
            erfinder = "Stiftet Bildungsgrundlagen als universelles Allgemeingut.",
            partner = "Dozentur und Begutachtung der studentischen Fortschritte.",
            kunde = "Erlangt fundiertes Wissen abseits staatlicher Einheitslehrpläne.",
            lernen = "Kern des 3+1-Pfads: Wöchentliche strukturierte Dokumentation.",
            finanzen = "Kostenlose Bereitstellung über Bildungsstiftung."
          )
        ),
        Subcategory(
          id = "4.1.2",
          name = "Akademische Qualifikation & Titelvergabe",
          perspectives = PerspectiveText(
            service = "Prüfungsabnahme, Notariatsprüfung & Kolloquien.",
            produkt = "Offizielle Urkunden: NEC-MVZ und Master of Quantum.",
            rohstoff = "Urkunden-Sicherheitspapier, Prägesiegel & Registerakten.",
            erfinder = "Verleiht die Titelurkunde in feierlichem Staatsakt.",
            partner = "Führt die Vorprüfungen gewissenhaft durch.",
            kunde = "Erreicht lebenslang gültigen Qualifikationstitel.",
            lernen = "Vollendung von 4.800 Studien- und Forschungsstunden.",
            finanzen = "Notarielle Feststellung nach § 36 BeurkG und GNotKG."
          )
        ),
        Subcategory(
          id = "4.1.3",
          name = "Frühkindliche & Familiäre Bildung",
          perspectives = PerspectiveText(
            service = "Förderung von Naturpädagogik und freiem Forscherdrang.",
            produkt = "Entwicklungsorientierte Lernmaterialien aus Naturstoffen.",
            rohstoff = "Holz, Pflanzen, elementare Spiel- und Forscherwerkzeuge.",
            erfinder = "Sichert generationenübergreifende Bildungsräume im Wohnzentrum.",
            partner = "Betreibt Wald- und Parkkindergärten im zentralen Park.",
            kunde = "Eltern erfahren Entlastung und partnerschaftliche Begleitung.",
            lernen = "Elternbildungs-Zertifikat mit 100 Stunden Praxisbegleitung.",
            finanzen = "Vollständig beitragsfrei für alle Ökosystem-Mitglieder."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Bildung & Erziehungswissenschaft",
        kategorie = "Bildung und Erziehung",
        logbuchPrefix = "LQ-BE",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Akademie-Präsident", "Notar", "BMBF"),
        lqNachweis = listOf("Vollständige '1 Studie' (4 Bände)", "Didaktik-Zertifikat", "Logbuch 4 Jahre Lehrpraxis"),
        fsNachweis = listOf("Stiftungsprüfbericht", "Nachweis der beitragsfreien Mittelverwendung"),
        registerFormat = "MOQ-BE-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Bildung & Erziehung"),
      bauplan = defaultBauplan("Bildung & Erziehung"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 3, community = 3, city = 3, federal = 3, country = 3, continental = 2, global = 2)
    ),

    // 5. Bundestag
    Category(
      id = 5,
      name = "Bundestag",
      kurzname = "Bundestag",
      rechtsform = "geGbR",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "5.1.1",
          name = "Parlamentarische Transparenz & Gesetzesanalyse",
          perspectives = PerspectiveText(
            service = "Laufende Analyse parlamentarischer Drucksachen und Gesetzesinitiativen.",
            produkt = "Bürgerfreundliche Gesetzesbriefings & Transparenzberichte.",
            rohstoff = "Plenarprotokolle, Drucksachen & Sachverständigengutachten.",
            erfinder = "Ermöglicht neutrale Bürgeraufklärung ohne Lobbyeinfluss.",
            partner = "Publiziert Berichte im regionalen Info-Hub.",
            kunde = "Versteht komplexe Gesetzesvorhaben vor deren Beschluss.",
            lernen = "Parlamentsrecht & Staatsorganisationsrecht im Logbuch: 200 h.",
            finanzen = "Unabhängige Finanzierung über Stiftungsmittel."
          )
        ),
        Subcategory(
          id = "5.1.2",
          name = "Petitions- & Bürgerbeteiligungswesen",
          perspectives = PerspectiveText(
            service = "Unterstützung von Bürgern bei Petitionen und Anhörungen.",
            produkt = "Rechtssichere Eingaben und Stellungnahmen an Fachausschüsse.",
            rohstoff = "Bürgereingaben, Beschwerden & empirische Daten.",
            erfinder = "Stärkt die Basisdemokratie durch strukturierte Kanäle.",
            partner = "Organisiert Bürgerwerkstätten im Wohnzentrum.",
            kunde = "Verschafft eigenen Anliegen Gehör im Parlament.",
            lernen = "Petitionsrecht nach Art. 17 GG in der Praxis: 150 h.",
            finanzen = "Reine Ausgabenübernahme im Adapter-Betrieb."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Parlamentarische Ordnung",
        kategorie = "Bundestag",
        logbuchPrefix = "LQ-BT",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Stiftungsvorstand", "Notar", "BMF"),
        lqNachweis = listOf("Staatsrechtliches Grundmodul", "Begleitung von 50 Bürgerpetitionen", "Logbuch"),
        fsNachweis = listOf("Auditierung der Unabhängigkeit", "Cube 3b Belegsatz"),
        registerFormat = "NEC-MVZ-BT-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Bundestag"),
      bauplan = defaultBauplan("Bundestag"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 1, familial = 2, community = 3, city = 3, federal = 3, country = 3, continental = 2, global = 1)
    ),

    // 6. Deutsche Einheit
    Category(
      id = 6,
      name = "Deutsche Einheit",
      kurzname = "Einheit",
      rechtsform = "GbR",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF", "BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "6.1.1",
          name = "Regionale Angleichung & Strukturförderung",
          perspectives = PerspectiveText(
            service = "Förderung von Wirtschaftsclustern in strukturschwachen Regionen.",
            produkt = "Regionale Transformations-Roadmaps & Förderkonzepte.",
            rohstoff = "Brachflächen, historische Bausubstanz & lokale Handwerkstraditionen.",
            erfinder = "Lenkt MTK-Währung gezielt in unterversorgte Gebiete.",
            partner = "Gründet Job-Zentren in Ost- und West-Strukturregionen.",
            kunde = "Findet hochwertige Arbeitsplätze vor der eigenen Haustür.",
            lernen = "Regionalökonomie & Wiedervereinigungsgeschichte: 180 h.",
            finanzen = "Sonderförderung über Stiftungskonten ohne Fremdschulden."
          )
        ),
        Subcategory(
          id = "6.1.2",
          name = "Innerdeutsche Kultur- & Geschichtsbegegnung",
          perspectives = PerspectiveText(
            service = "Zeitzeugengespräche, Jugendbegegnungen & Gedenkstättenarbeit.",
            produkt = "Dokumentarreihen, Wanderausstellungen & Oral-History-Archive.",
            rohstoff = "Audioaufnahmen, Zeitzeugendokumente & historische Fotos.",
            erfinder = "Bewahrt das gemeinsame Erbe für künftige Generationen.",
            partner = "Veranstaltet Dialogtage in den Wohnzentren.",
            kunde = "Überwindet bestehende mentale Grenzen und Vorurteile.",
            lernen = "Historisch-politische Bildung im Logbuch verankert: 120 h.",
            finanzen = "Vollständig durch MTK-Gebührenstrom getragen."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Innerdeutsche Kohäsion",
        kategorie = "Deutsche Einheit",
        logbuchPrefix = "LQ-DE",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Kuratorium", "Notar (§ 36 BeurkG)", "BMBF"),
        lqNachweis = listOf("Abschlussarbeit zur Regionalökonomie", "Logbuch der Transformationsprojekte"),
        fsNachweis = listOf("Fördergeldfreier Investitionsnachweis", "Cube 3a Eigentumsgarantie"),
        registerFormat = "NEC-MVZ-DE-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Deutsche Einheit"),
      bauplan = defaultBauplan("Deutsche Einheit"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 2, familial = 2, community = 3, city = 3, federal = 3, country = 3, continental = 1, global = 1)
    ),

    // 7. Energie
    Category(
      id = 7,
      name = "Energie",
      kurzname = "Energie",
      rechtsform = "©",
      ebeneKreis = 2,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "7.1.1",
          name = "Dezentrale Erneuerbare Energien & Mikronetze",
          perspectives = PerspectiveText(
            service = "Planung, Errichtung und Wartung von Photovoltaik- und Windparks.",
            produkt = "Schlüsselfertige autarke Energie-Zellen für Wohnzentren.",
            rohstoff = "Sonne, Wind, Erdwärme & Halbleiterelemente.",
            erfinder = "Stellt Währung für die Anschaffung von Hochleistungstechnik.",
            partner = "Betreibt die Netze im genossenschaftlichen Adapter-Verbund.",
            kunde = "Bezieht dauerhaft günstige, CO2-freie Energie.",
            lernen = "Energietechnik-Zertifikat mit 350 Stunden Praxismontage.",
            finanzen = "Einspeisevergütung speist direkt den lokalen MTK-Liquiditätspool."
          )
        ),
        Subcategory(
          id = "7.1.2",
          name = "Speichertechnologie & Wasserstoffwirtschaft",
          perspectives = PerspectiveText(
            service = "Entwicklung stationärer Batteriespeicher & Elektrolyseanlagen.",
            produkt = "Modulare Speichercontainer für saisonale Energiesouveränität.",
            rohstoff = "Eisen-Phosphat, Natrium-Ionen & grüner Wasserstoff.",
            erfinder = "Sichert die absolute Energieautarkie der Wohnzentren ab.",
            partner = "Wartet die elektrochemischen Speicher im Studio-Verbund.",
            kunde = "Ist gegen überregionale Blackouts vollkommen geschützt.",
            lernen = "Gefahrgut- und Hochvolt-Ausbildung im Logbuch: 200 h.",
            finanzen = "Investitionsausgaben mindern Steuerlast auf 0%."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Nachhaltige Energiesysteme",
        kategorie = "Energie",
        logbuchPrefix = "LQ-EN",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Adapter-Inhaber", "Notar", "BMF"),
        lqNachweis = listOf("Ingenieurmäßiges Gutachten zur Autarkie", "Logbuch 4 Jahre Netzbetrieb", "Sicherheitsprüfung"),
        fsNachweis = listOf("Pfandrechtliche Sicherung aller Speicheranlagen", "Cube 3c Freigabebestätigung"),
        registerFormat = "MOQ-EN-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Energie"),
      bauplan = defaultBauplan("Energie"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 3, community = 3, city = 3, federal = 3, country = 3, continental = 2, global = 2)
    ),

    // 8. Entwicklungspolitik
    Category(
      id = 8,
      name = "Entwicklungspolitik",
      kurzname = "Entwicklung",
      rechtsform = "Stiftung",
      ebeneKreis = 2,
      notarRelevant = true,
      ministerien = listOf("BMF", "BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "8.1.1",
          name = "Hilfe zur Selbsthilfe & Dezentrale Technologien",
          perspectives = PerspectiveText(
            service = "Transfer von Open-Source-Hardware und agrartechnischem Wissen.",
            produkt = "Robuste Brunnenbau-Kits, Solarkocher & modulare Werkstätten.",
            rohstoff = "Lokale Lehm-, Holz- und Recyclingmaterialien vor Ort.",
            erfinder = "Beendet postkoloniale Schuldenmechanismen durch 0-Kosten-Minting.",
            partner = "Bildet einheimische Trainer im 3+1-Pfad aus.",
            kunde = "Wird vom Bittsteller zum unabhängigen Produzenten.",
            lernen = "Entwicklungsökonomie & partizipative Methoden im Logbuch: 300 h.",
            finanzen = "Direkte Währungszuweisung ohne Korruptionsschnittstellen."
          )
        ),
        Subcategory(
          id = "8.1.2",
          name = "Fairer Handel & Direkte Wertschöpfungspartnerschaften",
          perspectives = PerspectiveText(
            service = "Aufbau lückenlos nachverfolgbarer Lieferketten direkt zu Kooperativen.",
            produkt = "Bio-zertifizierte Spezialitätenkaffees, Kakao und Kunsthandwerk.",
            rohstoff = "Kaffee, Kakao, Naturfasern & Edelhölzer.",
            erfinder = "Sichert feste Mindestpreise in unmanipulierbarer MTK-Währung.",
            partner = "Übernimmt Import und Verteilung in den Wohnzentren.",
            kunde = "Genießt ethisch einwandfreie Produkte mit Herkunftsnachweis.",
            lernen = "Fair-Trade-Audits & Logistikplanung im Studienbuch: 150 h.",
            finanzen = "Abrechnung über Cube 3b Auslagenübernahme."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Globale Entwicklung",
        kategorie = "Entwicklungspolitik",
        logbuchPrefix = "LQ-EP",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Stiftungsbeirat", "Notar (§ 36 BeurkG)", "BMBF"),
        lqNachweis = listOf("Projektleitung in 3 Kooperativen", "Logbuch Vor-Ort-Einsätze 1.500 h", "Kolloquium"),
        fsNachweis = listOf("Audit über direkte Mittelabflüsse", "Zertifikat der Korruptionsfreiheit"),
        registerFormat = "NEC-MVZ-EP-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Entwicklungspolitik"),
      bauplan = defaultBauplan("Entwicklungspolitik"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 2, familial = 1, community = 2, city = 2, federal = 2, country = 2, continental = 3, global = 3)
    ),

    // 9. Europapolitik und Europäische Union
    Category(
      id = 9,
      name = "Europapolitik und Europäische Union",
      kurzname = "Europa",
      rechtsform = "eGbR",
      ebeneKreis = 2,
      notarRelevant = true,
      ministerien = listOf("BMF"),
      subcategories = listOf(
        Subcategory(
          id = "9.1.1",
          name = "Harmonisierung dezentraler Rechtsstandards",
          perspectives = PerspectiveText(
            service = "Rechtsgutachten zur Vereinbarkeit von Notariatsakten mit EU-Recht.",
            produkt = "Vergleichende Gesetzessammlungen & Europäische Musterurkunden.",
            rohstoff = "EU-Richtlinien, Verordnungen & Rechtsprechung des EuGH.",
            erfinder = "Sichert die Gültigkeit des Super-Axioms im gesamten Binnenmarkt.",
            partner = "Koppelt Notariate in Frankreich, Italien und Polen an.",
            kunde = "Rechtssichere Freizügigkeit und Vermögenssicherung in der EU.",
            lernen = "Europarecht & internationales Privatrecht im Logbuch: 250 h.",
            finanzen = "Stiftungsfinanzierung über europäische MTK-Liquiditätshubs."
          )
        ),
        Subcategory(
          id = "9.1.2",
          name = "Kontinentale Wohnzentren-Netzwerke",
          perspectives = PerspectiveText(
            service = "Koordination grenzüberschreitender Wohnzentren entlang von Europakorridoren.",
            produkt = "Transnationale Kooperationsverträge zwischen Adapter-Unternehmen.",
            rohstoff = "Grenzüberschreitende Infrastrukturen & Logistikachsen.",
            erfinder = "Schafft ein einheitliches Währungsband ohne Euro-Inflation.",
            partner = "Tauscht Best Practices und Personal modular aus.",
            kunde = "Wohn- und Arbeitsrecht an allen europäischen Standorten.",
            lernen = "Sprachen & europäische Kulturgeschichte: 300 h.",
            finanzen = "Flächendeckende Speisung des kontinentalen Gebührenflusses."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "Master of Quantum für Europäische Integration",
        kategorie = "Europapolitik und Europäische Union",
        logbuchPrefix = "LQ-EU",
        praxisstunden = 4800,
        signaturFelder = listOf("Erfinder", "Europa-Kuratorium", "Notar", "BMF"),
        lqNachweis = listOf("Europarechtliche Fachprüfung", "Erfolgreiche Etablierung eines EU-Wohnzentrums", "Logbuch"),
        fsNachweis = listOf("Nachweis der Einhaltung aller EU-Geldwäscherichtlinien", "Cube 3d Garantie"),
        registerFormat = "MOQ-EU-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Europapolitik"),
      bauplan = defaultBauplan("Europapolitik"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 2, familial = 1, community = 2, city = 2, federal = 2, country = 3, continental = 3, global = 2)
    ),

    // 10. Gesellschaftspolitik und soziale Gruppen
    Category(
      id = 10,
      name = "Gesellschaftspolitik und soziale Gruppen",
      kurzname = "Gesellschaft",
      rechtsform = "©",
      ebeneKreis = 1,
      notarRelevant = true,
      ministerien = listOf("BMF", "BMBF"),
      subcategories = listOf(
        Subcategory(
          id = "10.1.1",
          name = "Generationsdialog & Seniorenintegration",
          perspectives = PerspectiveText(
            service = "Organisation von Mentoring-Programmen zwischen Senioren und Jugend.",
            produkt = "Erfahrungsbibliotheken, Hörbiografien & gemeinsame Werkstätten.",
            rohstoff = "Lebensweisheit, handwerkliche Fertigkeiten & Zeit.",
            erfinder = "Stellt Würde und Wertschätzung ins Zentrum der Gemeinschaft.",
            partner = "Etabliert Mehrgenerationenhäuser im Wohnzentrum.",
            kunde = "Findet Anschluss, Rat und lebendige soziale Einbindung.",
            lernen = "Gerontologie & Biografiearbeit im Aktivitäts-Logbuch: 160 h.",
            finanzen = "Vollständig beitragsfrei für alle Teilnehmer."
          )
        ),
        Subcategory(
          id = "10.1.2",
          name = "Inklusion & Barrierefreiheit",
          perspectives = PerspectiveText(
            service = "Barrierefreie Gestaltung aller physischen und digitalen Zugänge.",
            produkt = "Braille-Urkunden, Leichte Sprache Versionen & rollstuhlgerechte Studios.",
            rohstoff = "Assistenztechnologien, Braille-Drucker & Akustikmodule.",
            erfinder = "Garantiert die uneingeschränkte Teilhabe aller Menschen.",
            partner = "Führt Zugänglichkeits-Prüfungen in allen Räumen durch.",
            kunde = "Nimmt ohne jede Benachteiligung am Systemleben teil.",
            lernen = "Inklusionspädagogik & Gebärdensprachkurse: 200 h.",
            finanzen = "Ausgabenüberschuss für bauliche Barrierefreiheit."
          )
        )
      ),
      urkundenmuster = Urkundenmuster(
        titel = "NEC Management Verwalter für Gesellschaftliche Kohäsion",
        kategorie = "Gesellschaftspolitik und soziale Gruppen",
        logbuchPrefix = "LQ-GS",
        praxisstunden = 3000,
        signaturFelder = listOf("Erfinder", "Sozialrat", "Notar (§ 36 BeurkG)", "BMBF"),
        lqNachweis = listOf("Inklusionskonzept für ein Wohnzentrum", "Logbuch 1.000 Stunden Sozialdienst", "Fachkolloquium"),
        fsNachweis = listOf("Nachweis der barrierefreien Mittelverwendung", "Cube 3b Belege"),
        registerFormat = "NEC-MVZ-GS-[Jahr]-[Nr.]"
      ),
      organigramm = defaultOrganigramm("Gesellschaftspolitik"),
      bauplan = defaultBauplan("Gesellschaftspolitik"),
      referralKette = defaultReferral(),
      kapitalfluss = defaultKapitalfluss(),
      arena = ArenaRatings(personal = 3, familial = 3, community = 3, city = 3, federal = 2, country = 2, continental = 1, global = 1)
    )
  )
}
