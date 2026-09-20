package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.LayersData
import com.example.model.Layer
import com.example.ui.components.GlobalFeedbackLoopBanner
import com.example.ui.components.SignalCouplingCard
import com.example.ui.theme.*

@Composable
fun LayerExplorerScreen(
  onBack: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var selectedLayerId by remember { mutableStateOf("I") }
  var sc1Hinweg by remember { mutableStateOf(true) }
  var sc2Hinweg by remember { mutableStateOf(true) }
  var sc3Hinweg by remember { mutableStateOf(true) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(onClick = onBack) {
          Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
        }
        Text(
          text = "LAYER-EXPLORER (I – IV)",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold,
          color = BlueprintNavy
        )
      }
      Text(
        text = "Interaktive Architektur, Signal Couplings & unberührbarer Gebührenfluss.",
        style = MaterialTheme.typography.bodySmall,
        color = TextMuted,
        modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
      )
    }

    // Global Red Feedback Loop Banner
    item {
      GlobalFeedbackLoopBanner(modifier = Modifier.padding(bottom = 12.dp))
    }

    // Layer Selector Chips
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        LayersData.layers.forEach { layer ->
          val isSelected = layer.id == selectedLayerId
          FilterChip(
            selected = isSelected,
            onClick = { selectedLayerId = layer.id },
            label = { Text("Layer ${layer.id}", fontWeight = FontWeight.Bold) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = BlueprintNavy,
              selectedLabelColor = Color.White
            ),
            modifier = Modifier.weight(1f)
          )
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Selected Layer Detailed Card
    val currentLayer = LayersData.layers.firstOrNull { it.id == selectedLayerId } ?: LayersData.layers[0]
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("layer_detail_expanded"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, BlueprintNavy),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .background(BlueprintNavy, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = currentLayer.id,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp
              )
            }
            Badge(containerColor = SignalGreenLight, contentColor = SignalGreen) {
              Text("AKTIV", fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = currentLayer.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Text(
            text = currentLayer.untertitel,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = SignalBlue
          )

          HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = BlueprintBorder)

          Text(
            text = "SUPER-AXIOM & LEITPRINZIP:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextMuted
          )
          Text(
            text = currentLayer.superAxiomNotice,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = BlueprintNavy,
            modifier = Modifier.padding(vertical = 4.dp)
          )

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "KERNFUNKTIONEN & REGELN:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextMuted
          )
          currentLayer.bullets.forEach { bullet ->
            Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.Top) {
              Text("• ", color = SignalBlue, fontWeight = FontWeight.Bold)
              Text(text = bullet, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "SYSTEMDETAILS:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = TextMuted
          )
          Text(
            text = currentLayer.details,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            lineHeight = 18.sp
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }

    // Signal Couplings Section
    item {
      Text(
        text = "SIGNAL COUPLINGS (BIDIREKTIONAL)",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
      Text(
        text = "Koppeln die Ebenen mit Hinweg/Set (grün) und Rückweg/Reset (blau).",
        style = MaterialTheme.typography.bodySmall,
        color = TextMuted,
        modifier = Modifier.padding(bottom = 8.dp)
      )
    }

    item {
      SignalCouplingCard(
        coupling = LayersData.signalCouplings[0],
        isHinwegActive = sc1Hinweg,
        onToggle = { sc1Hinweg = !sc1Hinweg }
      )
    }
    item {
      SignalCouplingCard(
        coupling = LayersData.signalCouplings[1],
        isHinwegActive = sc2Hinweg,
        onToggle = { sc2Hinweg = !sc2Hinweg }
      )
    }
    item {
      SignalCouplingCard(
        coupling = LayersData.signalCouplings[2],
        isHinwegActive = sc3Hinweg,
        onToggle = { sc3Hinweg = !sc3Hinweg }
      )
      Spacer(modifier = Modifier.height(16.dp))
    }

    // Right-Docked Block: Kunden / Nehmer Detailed Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("kunden_nehmer_block"),
        colors = CardDefaults.cardColors(containerColor = Slate100),
        border = BorderStroke(1.5.dp, BlueprintNavy),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Group, contentDescription = null, tint = BlueprintNavy, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = LayersData.rightDockedKundenBlock["titel"] ?: "KUNDEN / NEHMER",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = LayersData.rightDockedKundenBlock["subtitel"] ?: "",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = SignalBlue
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "• ${LayersData.rightDockedKundenBlock["kontakt"]}",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
          Text(
            text = "• ${LayersData.rightDockedKundenBlock["zertifikat"]}",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
          Text(
            text = "• ${LayersData.rightDockedKundenBlock["bruecken"]}",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = LayersData.rightDockedKundenBlock["prinzip"] ?: "",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
          )
        }
      }
    }
  }
}
