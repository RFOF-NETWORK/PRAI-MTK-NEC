package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.example.data.KreiseData
import com.example.model.WohnzentrumModul
import com.example.ui.theme.*

@Composable
fun WohnzentrenScreen(modifier: Modifier = Modifier) {
  var selectedCircle by remember { mutableStateOf(1) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text(
        text = "WOHNZENTREN & 2-KREISE-MODELL",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
      Text(
        text = "Physische Bauplanung: Modularer Lebens- & Arbeitsraum mit zentralem Park.",
        style = MaterialTheme.typography.bodySmall,
        color = TextMuted
      )
    }

    // Kreis Switcher
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        FilterChip(
          selected = selectedCircle == 1,
          onClick = { selectedCircle = 1 },
          label = { Text("Kreis 1: Fundament (4 J.)", fontWeight = FontWeight.Bold) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = SignalBlue,
            selectedLabelColor = Color.White
          ),
          modifier = Modifier.weight(1f)
        )
        FilterChip(
          selected = selectedCircle == 2,
          onClick = { selectedCircle = 2 },
          label = { Text("Kreis 2: Skalierung", fontWeight = FontWeight.Bold) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = SignalGreen,
            selectedLabelColor = Color.White
          ),
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Info about selected circle
    val circleInfo = if (selectedCircle == 1) KreiseData.kreis1Info else KreiseData.kreis2Info
    val kernpunkte = circleInfo["kernpunkte"] as? List<*> ?: emptyList<Any>()

    item {
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
            Text(
              text = circleInfo["titel"] as? String ?: "",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy,
              modifier = Modifier.weight(1f)
            )
            Badge(containerColor = if (selectedCircle == 1) SignalBlue else SignalGreen, contentColor = Color.White) {
              Text(if (selectedCircle == 1) "Kreis 1" else "Kreis 2", fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = circleInfo["untertitel"] as? String ?: "",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = SignalBlue
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = circleInfo["status"] as? String ?: "",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
          )

          HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BlueprintBorder)

          Text(
            text = "KERNPRINZIPIEN & MEILENSTEINE:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          kernpunkte.forEach { kp ->
            Row(modifier = Modifier.padding(vertical = 2.dp)) {
              Text("• ", color = SignalGreen, fontWeight = FontWeight.Bold)
              Text(text = kp.toString(), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = circleInfo["philosophie"] as? String ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
          )
        }
      }
    }

    // Wohnzentren Locations
    item {
      Text(
        text = "PROTOTYPISCHE STANDORTE",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
      Text(
        text = "Autarke Siedlungskörper mit Job-Zentren, Akademien und Parkanlagen.",
        style = MaterialTheme.typography.bodySmall,
        color = TextMuted
      )
    }

    items(KreiseData.wohnzentren) { wz ->
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
              Icon(Icons.Default.LocationOn, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = wz.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = BlueprintNavy
              )
            }
            Badge(containerColor = Slate100, contentColor = BlueprintNavy) {
              Text(wz.standort, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(text = "Typ: ${wz.typ}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = BlueprintNavy)
          Text(text = "Kapazität: ${wz.kapazitaet}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = TextSecondary)
          Text(text = "Parkfläche: ${wz.parkFlaeche}", style = MaterialTheme.typography.bodySmall, color = SignalGreen)
          Spacer(modifier = Modifier.height(4.dp))
          Text(text = wz.beschreibung, style = MaterialTheme.typography.bodySmall, color = TextMuted, fontSize = 12.sp)
        }
      }
    }
  }
}
