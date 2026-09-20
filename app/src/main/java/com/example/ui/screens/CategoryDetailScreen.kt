package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun CategoryDetailScreen(
  categoryId: Int,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val category = CategoriesRepository.getCategoryById(categoryId)

  if (category == null) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("Kategorie $categoryId nicht gefunden.")
    }
    return
  }

  var selectedTabIndex by remember { mutableStateOf(0) }
  val tabs = listOf(
    "8 Sichten",
    "Urkunde",
    "Organigramm",
    "Bauplan",
    "Referral/Kapital",
    "Arena",
    "Notizen"
  )

  var userNote by remember { mutableStateOf("") }
  var noteSaved by remember { mutableStateOf(false) }

  Column(modifier = modifier.fillMaxSize()) {
    // Header Bar
    Surface(
      color = BlueprintNavy,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Zurück", tint = Color.White)
          }
          Spacer(modifier = Modifier.width(4.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Kategorie ${category.id}: ${category.name}",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = "Rechtsform: ${category.rechtsform} • Kreis: ${category.ebeneKreis} • Ministerien: ${category.ministerien.joinToString(", ")}",
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFF94A3B8),
              fontSize = 11.sp
            )
          }
        }
      }
    }

    // Scrollable Tab Row
    ScrollableTabRow(
      selectedTabIndex = selectedTabIndex,
      edgePadding = 12.dp,
      containerColor = Color.White,
      contentColor = BlueprintNavy
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTabIndex == index,
          onClick = { selectedTabIndex = index },
          text = {
            Text(
              text = title,
              fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
              fontSize = 12.sp
            )
          }
        )
      }
    }

    // Tab Content
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp),
      contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
      when (selectedTabIndex) {
        // Tab 0: 8-Sichten Matrix
        0 -> {
          item {
            PerspectiveTableView(subcategories = category.subcategories)
          }
        }

        // Tab 1: Urkundenmuster
        1 -> {
          item {
            UrkundeCard(urkundenmuster = category.urkundenmuster, kategorieId = category.id)
          }
        }

        // Tab 2: Organigramm
        2 -> {
          item {
            Text(
              text = "Hierarchisches Organigramm",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy,
              modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
              text = "Erfinder an der Spitze als Urheber & Schöpfer, gefolgt von Partner-Adaptern und operativen Einheiten.",
              style = MaterialTheme.typography.bodySmall,
              color = TextMuted,
              modifier = Modifier.padding(bottom = 12.dp)
            )
            OrganigrammTree(node = category.organigramm)
          }
        }

        // Tab 3: Bauplan Job-Zentrum
        3 -> {
          item {
            BauplanGrid(raeume = category.bauplan)
          }
        }

        // Tab 4: Referral & Kapitalfluss
        4 -> {
          item {
            ReferralAndCapitalFlowDiagram(
              referralKette = category.referralKette,
              kapitalfluss = category.kapitalfluss
            )
          }
        }

        // Tab 5: Arena-Zuordnung
        5 -> {
          item {
            Text(
              text = "8-Stufige Arena-Klassifikation",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy,
              modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
              text = "Abdeckung von der persönlichen Ebene bis zur globalen Sphäre (0 bis 3 Sterne).",
              style = MaterialTheme.typography.bodySmall,
              color = TextMuted,
              modifier = Modifier.padding(bottom = 12.dp)
            )

            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              border = BorderStroke(1.dp, BlueprintBorder),
              shape = RoundedCornerShape(10.dp)
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                ArenaRow("1. Persönliche Ebene", category.arena.personal)
                ArenaRow("2. Familiäre Ebene", category.arena.familial)
                ArenaRow("3. Nachbarschaft & Community", category.arena.community)
                ArenaRow("4. Stadt & Kommune", category.arena.city)
                ArenaRow("5. Bundesland & Region", category.arena.federal)
                ArenaRow("6. Nation / Bund", category.arena.country)
                ArenaRow("7. Kontinent (Europa)", category.arena.continental)
                ArenaRow("8. Globale Sphäre", category.arena.global)
              }
            }
          }
        }

        // Tab 6: Notizen
        6 -> {
          item {
            Text(
              text = "Eigene Notizen & Nachweise",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy,
              modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
              text = "Erfassen Sie persönliche Logbuchnotizen oder Beobachtungen zu Kategorie ${category.id}.",
              style = MaterialTheme.typography.bodySmall,
              color = TextMuted,
              modifier = Modifier.padding(bottom = 10.dp)
            )

            OutlinedTextField(
              value = userNote,
              onValueChange = {
                userNote = it
                noteSaved = false
              },
              modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .testTag("category_note_input"),
              placeholder = { Text("Logbucheintrag, Aktenzeichen, Notizen...") },
              shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Button(
                onClick = { noteSaved = true },
                colors = ButtonDefaults.buttonColors(containerColor = SignalBlue)
              ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Notiz speichern")
              }

              if (noteSaved) {
                Text(
                  text = "✓ Lokal gespeichert",
                  color = SignalGreen,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun ArenaRow(title: String, rating: Int) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(text = title, style = MaterialTheme.typography.bodyMedium, color = BlueprintNavy)
    StarRatingBar(rating = rating, maxRating = 3)
  }
  HorizontalDivider(thickness = 0.5.dp, color = Slate100)
}
