package com.example.model

enum class ArenaLevel(val key: String, val label: String, val description: String) {
  PERSONAL("personal", "Personal", "Individuelle Kompetenz, Selbstführung & ethische Fundierung"),
  FAMILIAL("familial", "Familial", "Generationsübergreifende Werte, familiäre Fürsorge & Stabilität"),
  COMMUNITY("community", "Community", "Lokale Nachbarschaft, Arbeitskreise & direkte Kooperationen"),
  CITY("city", "City", "Städtische Netzwerke, urbane Infrastrukturen & lokale Hubs"),
  FEDERAL("federal", "Federal", "Bundesland-Ebene, regionale Wirtschaftscluster & Kammern"),
  COUNTRY("country", "Country", "Nationale Gesamtwirtschaft, BMF/BMBF-Standards & Rechtssicherheit"),
  CONTINENTAL("continental", "Continental", "Europäische Union, grenzüberschreitende Verbünde & Standards"),
  GLOBAL("global", "Global", "Weltweite Skalierung, internationale Partnerschaften & Systemresilienz")
}

data class ArenaRatings(
  val personal: Int = 3,
  val familial: Int = 2,
  val community: Int = 3,
  val city: Int = 3,
  val federal: Int = 2,
  val country: Int = 3,
  val continental: Int = 2,
  val global: Int = 1
) {
  fun getRating(level: ArenaLevel): Int = when (level) {
    ArenaLevel.PERSONAL -> personal
    ArenaLevel.FAMILIAL -> familial
    ArenaLevel.COMMUNITY -> community
    ArenaLevel.CITY -> city
    ArenaLevel.FEDERAL -> federal
    ArenaLevel.COUNTRY -> country
    ArenaLevel.CONTINENTAL -> continental
    ArenaLevel.GLOBAL -> global
  }
  val average: Float get() = (personal + familial + community + city + federal + country + continental + global) / 8.0f
}

data class PerspectiveText(
  val service: String,
  val produkt: String,
  val rohstoff: String,
  val erfinder: String,
  val partner: String,
  val kunde: String,
  val lernen: String,
  val finanzen: String
)

data class Subcategory(
  val id: String,
  val name: String,
  val perspectives: PerspectiveText
)

data class Urkundenmuster(
  val titel: String,
  val kategorie: String,
  val logbuchPrefix: String,
  val praxisstunden: Int,
  val signaturFelder: List<String>,
  val lqNachweis: List<String>,
  val fsNachweis: List<String>,
  val registerFormat: String
)

data class OrganigrammNode(
  val id: String,
  val label: String,
  val role: String = "",
  val children: List<OrganigrammNode> = emptyList()
)

data class BauplanRaum(
  val name: String,
  val typ: String, // "studie", "akademie", "adapter", "service", "produkt", "rohstoff", "park", "sonstiges"
  val flaeche: String,
  val funktion: String
)

data class ReferralStep(
  val role: String, // "E", "P", "K", "NEC"
  val title: String,
  val description: String,
  val colorHex: Long
)

data class KapitalflussStep(
  val von: String,
  val nach: String,
  val mechanismus: String,
  val colorHex: Long
)

data class Category(
  val id: Int,
  val name: String,
  val kurzname: String,
  val rechtsform: String, // "©", "GbR", "eGbR", "geGbR", "Stiftung" (Adapter-©)
  val ebeneKreis: Int, // 1 oder 2
  val notarRelevant: Boolean,
  val ministerien: List<String>,
  val subcategories: List<Subcategory>,
  val urkundenmuster: Urkundenmuster,
  val organigramm: OrganigrammNode,
  val bauplan: List<BauplanRaum>,
  val referralKette: List<ReferralStep>,
  val kapitalfluss: List<KapitalflussStep>,
  val arena: ArenaRatings
)

data class Layer(
  val id: String,
  val name: String,
  val untertitel: String,
  val superAxiomNotice: String,
  val bullets: List<String>,
  val details: String,
  val signalCouplingId: String? = null
)

data class SignalCoupling(
  val id: String,
  val name: String,
  val hinwegSet: String,
  val rueckwegReset: String,
  val details: String
)

data class TitleInfo(
  val id: String,
  val name: String,
  val zeitpunktJahre: Int,
  val aussteller: List<String>,
  val wirkung: String,
  val registerFormat: String,
  val lqVoraussetzung: List<String>,
  val fsVoraussetzung: List<String>
)

data class BankingCube(
  val id: String,
  val name: String,
  val typ: String, // "Treugeber" / "Treuhand"
  val inhaber: String,
  val verfuegungsmacht: String,
  val zweck: String
)

data class GlossarItem(
  val begriff: String,
  val kategorie: String,
  val definition: String,
  val paragraph: String? = null
)

data class FaqItem(
  val frage: String,
  val antwort: String,
  val kategorie: String
)

data class WohnzentrumModul(
  val name: String,
  val standort: String,
  val typ: String,
  val beschreibung: String,
  val parkFlaeche: String,
  val kapazitaet: String
)
