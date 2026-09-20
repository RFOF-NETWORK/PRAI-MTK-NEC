package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.GlossarData
import com.example.model.FaqItem
import com.example.model.GlossarItem
import com.example.ui.theme.*

@Composable
fun GlossarFaqScreen(modifier: Modifier = Modifier) {
  var selectedTab by remember { mutableStateOf(0) }
  var searchQuery by remember { mutableStateOf("") }
  val tabs = listOf("Glossar (A–Z)", "Häufige Fragen (FAQ)")

  val filteredGlossar = remember(searchQuery) {
    if (searchQuery.isBlank()) GlossarData.items else {
      GlossarData.items.filter {
        it.begriff.contains(searchQuery, ignoreCase = true) ||
        it.definition.contains(searchQuery, ignoreCase = true)
      }
    }
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "SYSTEM-GLOSSAR & FAQ",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      color = BlueprintNavy
    )
    Text(
      text = "Fachbegriffe, rechtliche Fundamente & Antworten auf zentrale Fragen.",
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

    Spacer(modifier = Modifier.height(12.dp))

    if (selectedTab == 0) {
      // Glossar View
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Begriff oder Definition suchen...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Clear, contentDescription = null)
            }
          }
        },
        singleLine = true,
        shape = RoundedCornerShape(10.dp)
      )

      Spacer(modifier = Modifier.height(10.dp))

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(filteredGlossar) { entry ->
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BlueprintBorder),
            shape = RoundedCornerShape(8.dp)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = entry.begriff,
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  color = BlueprintNavy
                )
                Badge(containerColor = Slate100, contentColor = SignalBlue) {
                  Text(entry.kategorie, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = entry.definition,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                lineHeight = 17.sp
              )
            }
          }
        }
      }
    } else {
      // FAQ View
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(GlossarData.faqs) { faq ->
          FaqCard(faq)
        }
      }
    }
  }
}

@Composable
fun FaqCard(faq: FaqItem) {
  var expanded by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { expanded = !expanded },
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, BlueprintBorder),
    shape = RoundedCornerShape(8.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = faq.frage,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold,
          color = BlueprintNavy,
          modifier = Modifier.weight(1f)
        )
        Icon(
          imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
          contentDescription = null,
          tint = SignalBlue
        )
      }

      AnimatedVisibility(visible = expanded) {
        Column(modifier = Modifier.padding(top = 10.dp)) {
          HorizontalDivider(color = BlueprintBorder)
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = faq.antwort,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            lineHeight = 18.sp
          )
        }
      }
    }
  }
}
