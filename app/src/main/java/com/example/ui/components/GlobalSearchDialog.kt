package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.CategoriesRepository
import com.example.model.Category
import com.example.ui.theme.*

@Composable
fun GlobalSearchDialog(
  onDismiss: () -> Unit,
  onSelectCategory: (Int) -> Unit
) {
  var query by remember { mutableStateOf("") }
  var selectedRechtsformFilter by remember { mutableStateOf<String?>(null) }
  val rechtsformOptions = listOf("Alle", "©", "GbR", "eGbR", "geGbR", "Stiftung")

  val searchResults = remember(query, selectedRechtsformFilter) {
    val results = CategoriesRepository.searchGlobal(query)
    if (selectedRechtsformFilter == null || selectedRechtsformFilter == "Alle") {
      results
    } else {
      results.filter { it.category.rechtsform.equals(selectedRechtsformFilter, ignoreCase = true) }
    }
  }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 12.dp, vertical = 24.dp)
        .testTag("global_search_dialog"),
      shape = RoundedCornerShape(16.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        // Dialog Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "GLOBALE SYSTEMSUCHE",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
            Text(
              text = "Kategorien, IDs (#1–28) & alle 8 Sichten durchsuchen",
              style = MaterialTheme.typography.bodySmall,
              color = TextMuted,
              fontSize = 11.sp
            )
          }
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_search_button")
          ) {
            Icon(Icons.Default.Clear, contentDescription = "Schließen", tint = TextMuted)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Input Field
        OutlinedTextField(
          value = query,
          onValueChange = { query = it },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("global_search_input_field"),
          placeholder = { Text("Suchbegriff, ID (#1), Perspektive (z.B. Rohstoff, Notar)...") },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Suchen", tint = BlueprintNavy) },
          trailingIcon = {
            if (query.isNotEmpty()) {
              IconButton(onClick = { query = "" }) {
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

        Spacer(modifier = Modifier.height(10.dp))

        // Quick Rechtsform Filter Chips
        LazyRow(
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          item {
            Text(
              text = "Rechtsform:",
              style = MaterialTheme.typography.labelSmall,
              color = TextMuted,
              modifier = Modifier.padding(end = 4.dp)
            )
          }
          items(rechtsformOptions) { rf ->
            val isSelected = (rf == "Alle" && selectedRechtsformFilter == null) || (rf == selectedRechtsformFilter)
            FilterChip(
              selected = isSelected,
              onClick = {
                selectedRechtsformFilter = if (rf == "Alle" || rf == selectedRechtsformFilter) null else rf
              },
              label = {
                Text(
                  text = rf,
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = if (rf == "©") UrkundeGoldBg else SignalBlueLight,
                selectedLabelColor = if (rf == "©") UrkundeGoldDark else SignalBlue
              )
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Search Stats & Quick Tags
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "${searchResults.size} Treffer gefunden",
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
          )

          if (query.isBlank()) {
            Text(
              text = "Tipp: 'Notar', 'Permakultur', '0%', '17'",
              style = MaterialTheme.typography.labelSmall,
              color = SignalBlue,
              fontSize = 10.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = BlueprintBorder, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(8.dp))

        // Results List
        if (searchResults.isEmpty()) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(top = 40.dp),
            contentAlignment = Alignment.TopCenter
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(12.dp))
              Text(
                text = "Keine Treffer für \"$query\"",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = BlueprintNavy
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Versuchen Sie eine ID (1-28), Rechtsform (©, GbR, Stiftung) oder Fachbegriffe.",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
              )
            }
          }
        } else {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
          ) {
            items(searchResults) { result ->
              SearchResultCard(
                result = result,
                searchQuery = query,
                onClick = {
                  onSelectCategory(result.category.id)
                  onDismiss()
                }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun SearchResultCard(
  result: CategoriesRepository.GlobalSearchResult,
  searchQuery: String,
  onClick: () -> Unit
) {
  val cat = result.category
  val rechtsformColor = when (cat.rechtsform) {
    "©" -> UrkundeGold
    "GbR" -> SignalBlue
    "eGbR" -> SignalGreen
    "geGbR" -> Color(0xFF8B5CF6) // Purple
    else -> Color(0xFF64748B) // Stiftung / Slate
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .testTag("search_result_item_${cat.id}"),
    colors = CardDefaults.cardColors(containerColor = Slate50),
    shape = RoundedCornerShape(10.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.weight(1f)
        ) {
          // ID Badge
          Surface(
            color = BlueprintNavy,
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "#${cat.id}",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          Spacer(modifier = Modifier.width(8.dp))

          Text(
            text = cat.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          // Rechtsform Badge
          Surface(
            color = rechtsformColor.copy(alpha = 0.15f),
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = cat.rechtsform,
              color = rechtsformColor,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
            )
          }

          // Arena Stars indicator
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .background(Color.White, RoundedCornerShape(4.dp))
              .padding(horizontal = 4.dp, vertical = 2.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = UrkundeGold,
              modifier = Modifier.size(12.dp)
            )
            Text(
              text = String.format("%.1f", cat.arena.average),
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
          }

          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Öffnen",
            tint = TextMuted,
            modifier = Modifier.size(14.dp)
          )
        }
      }

      // Matched Detail Snippet if perspective / subcategory match
      if (result.matchType == CategoriesRepository.MatchType.PERSPECTIVE && result.matchedPerspective != null) {
        Spacer(modifier = Modifier.height(6.dp))
        Surface(
          color = Color.White,
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(8.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Surface(
                color = SignalGreenLight,
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = result.matchedPerspective,
                  color = SignalGreen,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
              }
              if (result.subcategoryName != null) {
                Text(
                  text = result.subcategoryName,
                  style = MaterialTheme.typography.labelSmall,
                  color = TextMuted,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }
            if (result.matchedSnippet != null) {
              Spacer(modifier = Modifier.height(4.dp))
              HighlightedSnippet(
                text = result.matchedSnippet,
                query = searchQuery
              )
            }
          }
        }
      } else if (result.matchType == CategoriesRepository.MatchType.SUBCATEGORY && result.subcategoryName != null) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Unterkategorie: ${result.subcategoryName}",
          style = MaterialTheme.typography.bodySmall,
          color = SignalBlue,
          fontSize = 11.sp
        )
      } else {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Kurzname: ${cat.kurzname} • Ebene Kreis ${cat.ebeneKreis} • ${cat.ministerien.joinToString()}",
          style = MaterialTheme.typography.bodySmall,
          color = TextMuted,
          fontSize = 11.sp
        )
      }
    }
  }
}

@Composable
private fun HighlightedSnippet(
  text: String,
  query: String
) {
  if (query.isBlank()) {
    Text(
      text = text,
      style = MaterialTheme.typography.bodySmall,
      color = TextSecondary,
      fontSize = 11.sp,
      lineHeight = 15.sp
    )
    return
  }

  val annotatedString = buildAnnotatedString {
    var currentIndex = 0
    val lowerText = text.lowercase()
    val lowerQuery = query.lowercase().trim()

    while (currentIndex < text.length) {
      val foundIndex = lowerText.indexOf(lowerQuery, currentIndex)
      if (foundIndex == -1) {
        append(text.substring(currentIndex))
        break
      }

      if (foundIndex > currentIndex) {
        append(text.substring(currentIndex, foundIndex))
      }

      withStyle(
        SpanStyle(
          background = Color(0xFFFEF08A), // Light Yellow highlight
          color = BlueprintNavy,
          fontWeight = FontWeight.Bold
        )
      ) {
        append(text.substring(foundIndex, foundIndex + lowerQuery.length))
      }

      currentIndex = foundIndex + lowerQuery.length
    }
  }

  Text(
    text = annotatedString,
    style = MaterialTheme.typography.bodySmall,
    color = TextSecondary,
    fontSize = 11.sp,
    lineHeight = 15.sp
  )
}
