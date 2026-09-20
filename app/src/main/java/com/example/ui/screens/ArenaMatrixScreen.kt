package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ArenasData
import com.example.data.CategoriesRepository
import com.example.model.ArenaLevel
import com.example.ui.components.StarRatingBar
import com.example.ui.theme.*

@Composable
fun ArenaMatrixScreen(
  onSelectCategory: (Int) -> Unit = {},
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("Matrix-Tabelle (28x8)", "Wettbewerbsarten (4)")

  var searchQuery by remember { mutableStateOf("") }
  var selectedRechtsform by remember { mutableStateOf<String?>(null) }
  var minStarRating by remember { mutableStateOf<Int?>(null) }
  var selectedLevel by remember { mutableStateOf<ArenaLevel?>(null) }

  val filteredCategories = remember(searchQuery, selectedRechtsform, minStarRating, selectedLevel) {
    CategoriesRepository.searchCategories(
      query = searchQuery,
      rechtsformFilter = selectedRechtsform,
      minStars = minStarRating,
      selectedArenaLevel = selectedLevel
    )
  }

  val rechtsformOptions = listOf("©", "GbR", "eGbR", "geGbR", "Stiftung")
  val starOptions = listOf(
    null to "Alle ★",
    2 to "★ 2+",
    3 to "★ 3"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "ARENA-MATRIX & WETTKÄMPFE",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      color = BlueprintNavy
    )
    Text(
      text = "8-stufige Vertikalitätsmatrix & spielerische Leistungsvergleiche.",
      style = MaterialTheme.typography.bodySmall,
      color = TextMuted,
      modifier = Modifier.padding(bottom = 12.dp)
    )

    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = Color.White,
      contentColor = BlueprintNavy
    ) {
      tabs.forEachIndexed { idx, label ->
        Tab(
          selected = selectedTab == idx,
          onClick = { selectedTab = idx },
          text = { Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    if (selectedTab == 0) {
      // Filter Controls for Matrix Table
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.White, RoundedCornerShape(8.dp))
          .border(1.dp, BlueprintBorder, RoundedCornerShape(8.dp))
          .padding(10.dp)
      ) {
        // Search Input
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
          placeholder = { Text("Kategorie oder Sicht filtern...", fontSize = 12.sp) },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Suche", modifier = Modifier.size(18.dp)) },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Clear, contentDescription = "Löschen", modifier = Modifier.size(16.dp))
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(8.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SignalBlue,
            unfocusedBorderColor = BlueprintBorder
          )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Rechtsform Filter Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Rechtsform:", fontSize = 11.sp, color = TextMuted)
          FilterChip(
            selected = selectedRechtsform == null,
            onClick = { selectedRechtsform = null },
            label = { Text("Alle Formen", fontSize = 10.sp) }
          )
          rechtsformOptions.forEach { rf ->
            val isSelected = selectedRechtsform == rf
            FilterChip(
              selected = isSelected,
              onClick = { selectedRechtsform = if (isSelected) null else rf },
              label = {
                Text(
                  text = rf,
                  fontSize = 10.sp,
                  fontWeight = if (rf == "©" || isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = if (rf == "©") UrkundeGoldBg else SignalBlueLight,
                selectedLabelColor = if (rf == "©") UrkundeGoldDark else SignalBlue
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Star Rating & Level Filter Row
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text("Mindest-Sterne:", fontSize = 11.sp, color = TextMuted)
          starOptions.forEach { (stars, label) ->
            val isSelected = minStarRating == stars
            FilterChip(
              selected = isSelected,
              onClick = { minStarRating = if (isSelected) null else stars },
              label = { Text(label, fontSize = 10.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = UrkundeGoldBg,
                selectedLabelColor = UrkundeGoldDark
              )
            )
          }

          if (selectedRechtsform != null || minStarRating != null || searchQuery.isNotEmpty()) {
            TextButton(
              onClick = {
                searchQuery = ""
                selectedRechtsform = null
                minStarRating = null
                selectedLevel = null
              },
              contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
            ) {
              Text("Zurücksetzen", fontSize = 10.sp, color = SignalBlue)
            }
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${filteredCategories.size} von 28 Kategorien angezeigt",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontSize = 10.sp
          )
          Text(
            text = "Tipp: Auf Zeile tippen für Detailansicht",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            fontSize = 10.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Matrix Table
      val scrollState = rememberScrollState()
      Box(
        modifier = Modifier
          .fillMaxSize()
          .horizontalScroll(scrollState)
      ) {
        Column(modifier = Modifier.width(980.dp)) {
          // Table Header
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(BlueprintNavy, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
              .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Nr. & Kategorie", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.width(200.dp))
            ArenaLevel.entries.forEach { lvl ->
              Text(lvl.label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.width(95.dp))
            }
          }

          // Table Rows
          if (filteredCategories.isEmpty()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
              contentAlignment = Alignment.Center
            ) {
              Text("Keine Kategorien mit diesen Filterkriterien gefunden.", color = TextMuted, fontSize = 12.sp)
            }
          } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
              items(filteredCategories, key = { it.id }) { cat ->
                val rfColor = when (cat.rechtsform) {
                  "©" -> UrkundeGold
                  "GbR" -> SignalBlue
                  "eGbR" -> SignalGreen
                  "geGbR" -> Color(0xFF8B5CF6)
                  else -> Color(0xFF64748B)
                }

                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .background(if (cat.id % 2 == 0) Slate50 else Color.White)
                    .clickable { onSelectCategory(cat.id) }
                    .padding(vertical = 6.dp, horizontal = 8.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(
                    modifier = Modifier.width(200.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                  ) {
                    Text(
                      text = "${cat.id}. ${cat.kurzname}",
                      style = MaterialTheme.typography.bodySmall,
                      fontWeight = FontWeight.Bold,
                      color = BlueprintNavy,
                      modifier = Modifier.weight(1f)
                    )
                    Surface(
                      color = rfColor.copy(alpha = 0.15f),
                      shape = RoundedCornerShape(4.dp)
                    ) {
                      Text(
                        text = cat.rechtsform,
                        color = rfColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                      )
                    }
                  }
                  Box(modifier = Modifier.width(95.dp)) { StarRatingBar(cat.arena.personal) }
                  Box(modifier = Modifier.width(95.dp)) { StarRatingBar(cat.arena.familial) }
                  Box(modifier = Modifier.width(95.dp)) { StarRatingBar(cat.arena.community) }
                  Box(modifier = Modifier.width(95.dp)) { StarRatingBar(cat.arena.city) }
                  Box(modifier = Modifier.width(95.dp)) { StarRatingBar(cat.arena.federal) }
                  Box(modifier = Modifier.width(95.dp)) { StarRatingBar(cat.arena.country) }
                  Box(modifier = Modifier.width(95.dp)) { StarRatingBar(cat.arena.continental) }
                  Box(modifier = Modifier.width(95.dp)) { StarRatingBar(cat.arena.global) }
                }
                HorizontalDivider(thickness = 0.5.dp, color = BlueprintBorder)
              }
            }
          }
        }
      }
    } else {
      // 4 Competition types
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(bottom = 32.dp)
      ) {
        items(ArenasData.wettbewerbsarten) { comp ->
          val titel = comp["titel"] ?: ""
          val ziel = comp["ziel"] ?: ""
          val beschreibung = comp["beschreibung"] ?: ""
          val symbol = comp["symbol"] ?: "🏆"

          Card(
            modifier = Modifier.fillMaxWidth(),
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(text = symbol, fontSize = 20.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = titel,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = BlueprintNavy
                  )
                }
                Badge(containerColor = SignalGreenLight, contentColor = SignalGreen) {
                  Text(ziel, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
              }

              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = beschreibung,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 18.sp
              )
            }
          }
        }
      }
    }
  }
}
