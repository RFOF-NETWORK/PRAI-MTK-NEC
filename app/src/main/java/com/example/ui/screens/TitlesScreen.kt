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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TitlesData
import com.example.model.TitleInfo
import com.example.ui.theme.*

@Composable
fun TitlesScreen(modifier: Modifier = Modifier) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text(
        text = "TITEL & 3+1-JAHRES-PFAD",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
      Text(
        text = "Strukturierte Berufsqualifikation von der Praxis bis zum Master of Quantum.",
        style = MaterialTheme.typography.bodySmall,
        color = TextMuted
      )
    }

    // 3+1 Timeline Visualizer
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BlueprintNavy),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "DER 3+1-JAHRES-QUALIFIKATIONSPFAD",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Spacer(modifier = Modifier.height(12.dp))

          TitlesData.timelineSteps.forEachIndexed { index, step ->
            val jahr = step["jahr"] ?: ""
            val titel = step["titel"] ?: ""
            val status = step["status"] ?: ""
            val desc = step["desc"] ?: ""

            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.Top
            ) {
              Box(
                modifier = Modifier
                  .size(28.dp)
                  .background(if (index == 3) UrkundeGold else SignalGreen, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = "${index + 1}",
                  color = Color.White,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column(modifier = Modifier.weight(1f)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = titel,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                  Text(
                    text = jahr,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF38BDF8)
                  )
                }
                Text(
                  text = status,
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.SemiBold,
                  color = if (index == 3) UrkundeGold else SignalGreenLight,
                  fontSize = 11.sp
                )
                Text(
                  text = desc,
                  style = MaterialTheme.typography.bodySmall,
                  color = Color(0xFFCBD5E1),
                  fontSize = 12.sp
                )
              }
            }
            if (index < TitlesData.timelineSteps.size - 1) {
              Box(
                modifier = Modifier
                  .padding(start = 13.dp)
                  .width(2.dp)
                  .height(10.dp)
                  .background(Color(0xFF475569))
              )
            }
          }
        }
      }
    }

    // Title 1: NEC Management Verwalter & Zertifizierer
    item {
      TitleDetailCard(TitlesData.titles[0])
    }

    // Title 2: Master of Quantum
    item {
      TitleDetailCard(TitlesData.titles[1])
    }
  }
}

@Composable
fun TitleDetailCard(title: TitleInfo, modifier: Modifier = Modifier) {
  val isMaster = title.zeitpunktJahre == 4
  val borderCol = if (isMaster) UrkundeGold else BlueprintNavy

  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.5.dp, borderCol),
    shape = RoundedCornerShape(10.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Badge(containerColor = if (isMaster) UrkundeGold else SignalBlue, contentColor = Color.White) {
          Text(if (isMaster) "MOQ" else "NEC-MVZ", fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
        Text(
          text = "Regelzeit: ${title.zeitpunktJahre} Jahre",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = BlueprintNavy
        )
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = title.name,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
      Text(
        text = title.wirkung,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        modifier = Modifier.padding(vertical = 4.dp)
      )

      HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BlueprintBorder)

      Text(
        text = "L&Q-VORAUSSETZUNGEN (LERNEN & QUALIFIKATION):",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = TextMuted
      )
      title.lqVoraussetzung.forEach { v ->
        Row(modifier = Modifier.padding(vertical = 2.dp)) {
          Text("✓ ", color = SignalGreen, fontWeight = FontWeight.Bold)
          Text(v, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
      }

      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = "F&S-VORAUSSETZUNGEN (FINANZEN & SYSTEMSTRUKTUR):",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = TextMuted
      )
      title.fsVoraussetzung.forEach { b ->
        Row(modifier = Modifier.padding(vertical = 2.dp)) {
          Text("• ", color = SignalBlue, fontWeight = FontWeight.Bold)
          Text(b, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Registerformat: ${title.registerFormat}",
        style = MaterialTheme.typography.labelSmall,
        color = TextMuted,
        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
      )
    }
  }
}
