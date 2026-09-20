package com.example.data

import com.example.model.ArenaLevel
import com.example.model.Category
import kotlin.math.roundToInt

object CategoriesRepository {
  val allCategories: List<Category> by lazy {
    CategoriesDataPart1.categories +
    CategoriesDataPart2.categories +
    CategoriesDataPart3.categories
  }

  fun getCategoryById(id: Int): Category? {
    return allCategories.find { it.id == id }
  }

  enum class MatchType {
    ID, NAME, RECHTSFORM, SUBCATEGORY, PERSPECTIVE
  }

  data class GlobalSearchResult(
    val category: Category,
    val matchType: MatchType,
    val matchedPerspective: String? = null,
    val matchedSnippet: String? = null,
    val subcategoryName: String? = null
  )

  fun searchCategories(
    query: String = "",
    rechtsformFilter: String? = null,
    kreisFilter: Int? = null,
    minStars: Int? = null,
    selectedArenaLevel: ArenaLevel? = null
  ): List<Category> {
    val clean = query.trim()
    return allCategories.filter { cat ->
      val matchesQuery = if (clean.isBlank()) true else {
        cat.id.toString() == clean ||
        clean == "#${cat.id}" ||
        cat.id.toString().startsWith(clean) ||
        cat.name.contains(clean, ignoreCase = true) ||
        cat.kurzname.contains(clean, ignoreCase = true) ||
        cat.rechtsform.contains(clean, ignoreCase = true) ||
        cat.subcategories.any { sub ->
          sub.name.contains(clean, ignoreCase = true) ||
          sub.perspectives.service.contains(clean, ignoreCase = true) ||
          sub.perspectives.produkt.contains(clean, ignoreCase = true) ||
          sub.perspectives.rohstoff.contains(clean, ignoreCase = true) ||
          sub.perspectives.erfinder.contains(clean, ignoreCase = true) ||
          sub.perspectives.partner.contains(clean, ignoreCase = true) ||
          sub.perspectives.kunde.contains(clean, ignoreCase = true) ||
          sub.perspectives.lernen.contains(clean, ignoreCase = true) ||
          sub.perspectives.finanzen.contains(clean, ignoreCase = true)
        }
      }
      val matchesRechtsform = rechtsformFilter == null || cat.rechtsform.equals(rechtsformFilter, ignoreCase = true)
      val matchesKreis = kreisFilter == null || cat.ebeneKreis == kreisFilter
      val matchesStars = if (minStars == null) true else {
        if (selectedArenaLevel != null) {
          cat.arena.getRating(selectedArenaLevel) >= minStars
        } else {
          cat.arena.average >= (minStars - 0.25f)
        }
      }

      matchesQuery && matchesRechtsform && matchesKreis && matchesStars
    }
  }

  fun searchGlobal(query: String): List<GlobalSearchResult> {
    val clean = query.trim()
    if (clean.isBlank()) {
      return allCategories.map { GlobalSearchResult(it, MatchType.NAME) }
    }

    val results = mutableListOf<GlobalSearchResult>()
    for (cat in allCategories) {
      if (cat.id.toString() == clean || clean == "#${cat.id}") {
        results.add(GlobalSearchResult(cat, MatchType.ID, matchedSnippet = "ID: #${cat.id} - ${cat.name}"))
        continue
      }
      if (cat.name.contains(clean, ignoreCase = true) || cat.kurzname.contains(clean, ignoreCase = true)) {
        results.add(GlobalSearchResult(cat, MatchType.NAME, matchedSnippet = cat.name))
        continue
      }
      if (cat.rechtsform.equals(clean, ignoreCase = true)) {
        results.add(GlobalSearchResult(cat, MatchType.RECHTSFORM, matchedSnippet = "Rechtsform: ${cat.rechtsform}"))
        continue
      }

      var perspectiveMatched = false
      for (sub in cat.subcategories) {
        if (sub.name.contains(clean, ignoreCase = true)) {
          results.add(
            GlobalSearchResult(
              category = cat,
              matchType = MatchType.SUBCATEGORY,
              subcategoryName = sub.name,
              matchedSnippet = sub.name
            )
          )
          perspectiveMatched = true
          break
        }

        val perspectives = listOf(
          "Service" to sub.perspectives.service,
          "Produkt" to sub.perspectives.produkt,
          "Rohstoff" to sub.perspectives.rohstoff,
          "Erfinder" to sub.perspectives.erfinder,
          "Partner" to sub.perspectives.partner,
          "Kunde" to sub.perspectives.kunde,
          "Lernen (L&Q)" to sub.perspectives.lernen,
          "Finanzen (F&S)" to sub.perspectives.finanzen
        )

        for ((name, text) in perspectives) {
          if (text.contains(clean, ignoreCase = true)) {
            val idx = text.indexOf(clean, ignoreCase = true)
            val start = (idx - 25).coerceAtLeast(0)
            val end = (idx + clean.length + 35).coerceAtMost(text.length)
            val snippet = (if (start > 0) "…" else "") + text.substring(start, end).trim() + (if (end < text.length) "…" else "")

            results.add(
              GlobalSearchResult(
                category = cat,
                matchType = MatchType.PERSPECTIVE,
                matchedPerspective = name,
                matchedSnippet = snippet,
                subcategoryName = sub.name
              )
            )
            perspectiveMatched = true
            break
          }
        }
        if (perspectiveMatched) break
      }
    }
    return results
  }

  data class HealthReport(
    val totalCategories: Int,
    val totalSubcategories: Int,
    val hasAll28: Boolean,
    val hasAll8Perspectives: Boolean,
    val hasAllUrkunden: Boolean,
    val hasAllOrganigramme: Boolean,
    val hasAllBauplaene: Boolean,
    val status: String // "GREEN", "YELLOW", "RED"
  )

  fun validateHealth(): HealthReport {
    val totalCats = allCategories.size
    val totalSubs = allCategories.sumOf { it.subcategories.size }
    val all28 = totalCats == 28
    val allPerspectives = allCategories.all { cat ->
      cat.subcategories.isNotEmpty() && cat.subcategories.all { sub ->
        sub.perspectives.service.isNotBlank() &&
        sub.perspectives.produkt.isNotBlank() &&
        sub.perspectives.rohstoff.isNotBlank() &&
        sub.perspectives.erfinder.isNotBlank() &&
        sub.perspectives.partner.isNotBlank() &&
        sub.perspectives.kunde.isNotBlank() &&
        sub.perspectives.lernen.isNotBlank() &&
        sub.perspectives.finanzen.isNotBlank()
      }
    }
    val allUrkunden = allCategories.all { it.urkundenmuster.titel.isNotBlank() && it.urkundenmuster.praxisstunden > 0 }
    val allOrgs = allCategories.all { it.organigramm.label.isNotBlank() && it.organigramm.children.isNotEmpty() }
    val allBau = allCategories.all { it.bauplan.size >= 6 }

    val status = if (all28 && allPerspectives && allUrkunden && allOrgs && allBau) "GREEN" else "YELLOW"

    return HealthReport(
      totalCategories = totalCats,
      totalSubcategories = totalSubs,
      hasAll28 = all28,
      hasAll8Perspectives = allPerspectives,
      hasAllUrkunden = allUrkunden,
      hasAllOrganigramme = allOrgs,
      hasAllBauplaene = allBau,
      status = status
    )
  }
}
