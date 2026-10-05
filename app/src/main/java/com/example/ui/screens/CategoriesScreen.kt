package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.CategoriesRepository
import com.example.model.ArenaLevel
import com.example.model.Category
import com.example.ui.components.StarRatingBar
import com.example.ui.theme.*

@Composable
fun CategoriesScreen(
  onSelectCategory: (Int) -> Unit,
  modifier: Modifier = Modifier
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedRechtsform by remember { mutableStateOf<String?>(null) }
  var selectedKreis by remember { mutableStateOf<Int?>(null) }
  var minStarsFilter by remember { mutableStateOf<Int?>(null) }

  val filteredCategories = remember(searchQuery, selectedRechtsform, selectedKreis, minStarsFilter) {
    CategoriesRepository.searchCategories(
      query = searchQuery,
      rechtsformFilter = selectedRechtsform,
      kreisFilter = selectedKreis,
      minStars = minStarsFilter
    )
  }

  val rechtsformOptions = listOf("©", "GbR", "eGbR", "geGbR", "Stiftung")
  val starFilterOptions = listOf(
    null to "Alle ★",
    2 to "★ 2+",
    3 to "★ 3"
  )

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    // 1. Header Title
    item {
      Column {
        Text(
          text = "28 FACHKATEGORIEN",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = BlueprintNavy
        )
        Text(
          text = "Alle Hauptkategorien mit 8-Sichten-Matrix, Urkunden & Bauplänen.",
          style = MaterialTheme.typography.bodySmall,
          color = TextMuted,
          modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
        )
      }
    }

    // 2. Search Bar
    item {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("search_categories_input"),
        placeholder = { Text("Kategorie, ID (#1) oder Perspektive suchen...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Suche") },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Clear, contentDescription = "Löschen")
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = SignalBlue,
          unfocusedBorderColor = BlueprintBorder
        )
      )
    }

    // 3. Filter Chips Row 1: Rechtsform (Horizontally scrollable)
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        FilterChip(
          selected = selectedRechtsform == null,
          onClick = { selectedRechtsform = null },
          label = {
            Text(
              text = "Alle Formen",
              fontSize = 11.sp,
              maxLines = 1,
              softWrap = false
            )
          },
          modifier = Modifier.wrapContentWidth().testTag("filter_rechtsform_all")
        )
        rechtsformOptions.forEach { rf ->
          val isSelected = selectedRechtsform == rf
          FilterChip(
            selected = isSelected,
            onClick = { selectedRechtsform = if (isSelected) null else rf },
            label = {
              Text(
                text = rf,
                fontSize = 11.sp,
                maxLines = 1,
                softWrap = false,
                fontWeight = if (rf == "©" || isSelected) FontWeight.Bold else FontWeight.Normal
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = if (rf == "©") UrkundeGoldBg else SignalBlueLight,
              selectedLabelColor = if (rf == "©") UrkundeGoldDark else SignalBlue
            ),
            modifier = Modifier.wrapContentWidth().testTag("filter_rechtsform_$rf")
          )
        }
      }
    }

    // 4. Filter Chips Row 2: Arena Star Ratings & Kreis (Horizontally scrollable)
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Arena:",
          style = MaterialTheme.typography.labelSmall,
          color = TextMuted,
          fontSize = 11.sp,
          maxLines = 1,
          softWrap = false
        )

        starFilterOptions.forEach { (stars, label) ->
          val isSelected = minStarsFilter == stars
          FilterChip(
            selected = isSelected,
            onClick = { minStarsFilter = if (isSelected) null else stars },
            label = {
              Text(
                text = label,
                fontSize = 10.sp,
                maxLines = 1,
                softWrap = false
              )
            },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = UrkundeGoldBg,
              selectedLabelColor = UrkundeGoldDark
            ),
            modifier = Modifier.wrapContentWidth().testTag("filter_stars_${stars ?: 0}")
          )
        }

        Text(
          text = "|",
          color = BlueprintBorder,
          modifier = Modifier.padding(horizontal = 4.dp)
        )

        FilterChip(
          selected = selectedKreis == 1,
          onClick = { selectedKreis = if (selectedKreis == 1) null else 1 },
          label = {
            Text(
              text = "Kreis 1",
              fontSize = 10.sp,
              maxLines = 1,
              softWrap = false
            )
          },
          modifier = Modifier.wrapContentWidth()
        )
        FilterChip(
          selected = selectedKreis == 2,
          onClick = { selectedKreis = if (selectedKreis == 2) null else 2 },
          label = {
            Text(
              text = "Kreis 2",
              fontSize = 10.sp,
              maxLines = 1,
              softWrap = false
            )
          },
          modifier = Modifier.wrapContentWidth()
        )
      }
    }

    // 5. Categories Count Info & Reset
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "${filteredCategories.size} von 28 Kategorien gefunden",
          style = MaterialTheme.typography.labelSmall,
          color = TextMuted
        )

        if (selectedRechtsform != null || minStarsFilter != null || selectedKreis != null || searchQuery.isNotEmpty()) {
          TextButton(
            onClick = {
              selectedRechtsform = null
              selectedKreis = null
              minStarsFilter = null
              searchQuery = ""
            },
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
          ) {
            Text("Filter zurücksetzen", fontSize = 11.sp, color = SignalBlue)
          }
        }
      }
    }

    // 6. Categories List Items
    items(filteredCategories, key = { it.id }) { cat ->
      CategoryCardItem(category = cat, onClick = { onSelectCategory(cat.id) })
    }

    // Bottom spacing
    item {
      Spacer(modifier = Modifier.height(40.dp))
    }
  }
}

@Composable
fun CategoryCardItem(
  category: Category,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val (badgeBg, badgeTextColor) = when (category.rechtsform) {
    "©" -> UrkundeGoldBg to UrkundeGoldDark
    "GbR" -> SignalBlueLight to SignalBlue
    "eGbR" -> SignalGreenLight to SignalGreen
    "geGbR" -> Color(0xFFF3E8FF) to Color(0xFF7C3AED)
    else -> Color(0xFFF1F5F9) to Color(0xFF475569) // Stiftung
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .testTag("category_card_${category.id}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, BlueprintBorder),
    shape = RoundedCornerShape(10.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .background(BlueprintNavy, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "${category.id}",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = category.name,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
            Text(
              text = "${category.subcategories.size} Unterpunkte mit 8 Perspektiven",
              style = MaterialTheme.typography.bodySmall,
              color = TextMuted,
              fontSize = 11.sp
            )
          }
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
      }

      Spacer(modifier = Modifier.height(10.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
          Badge(containerColor = badgeBg, contentColor = badgeTextColor) {
            Text(category.rechtsform, fontWeight = FontWeight.Bold, fontSize = 10.sp)
          }
          Badge(containerColor = Slate100, contentColor = BlueprintNavy) {
            Text("Kreis ${category.ebeneKreis}", fontSize = 10.sp)
          }
          if (category.notarRelevant) {
            Badge(containerColor = UrkundeGoldBg, contentColor = UrkundeGold) {
              Text("§ 36 Notar", fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
          }
        }

        // Mini Arena Rating preview
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = UrkundeGold,
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(2.dp))
          Text(
            text = String.format("%.1f", category.arena.average),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Spacer(modifier = Modifier.width(6.dp))
          StarRatingBar(rating = category.arena.personal, maxRating = 3)
        }
      }
    }
  }
}
