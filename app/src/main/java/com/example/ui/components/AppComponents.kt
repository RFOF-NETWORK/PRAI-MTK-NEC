package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun StarRatingBar(rating: Int, maxRating: Int = 3, modifier: Modifier = Modifier) {
  Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    for (i in 1..maxRating) {
      Text(
        text = if (i <= rating) "★" else "☆",
        color = if (i <= rating) Color(0xFFEAB308) else Color(0xFF94A3B8),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
      )
    }
  }
}

@Composable
fun SignalCouplingCard(
  coupling: SignalCoupling,
  isHinwegActive: Boolean = true,
  onToggle: () -> Unit = {}
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
      .testTag("signal_coupling_${coupling.id}"),
    colors = CardDefaults.cardColors(containerColor = Slate100),
    border = BorderStroke(1.dp, BlueprintBorder),
    shape = RoundedCornerShape(10.dp)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Sync,
            contentDescription = "Signal Coupling",
            tint = SignalBlue,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = coupling.name,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
        }
        FilterChip(
          selected = isHinwegActive,
          onClick = onToggle,
          label = {
            Text(
              if (isHinwegActive) "Hinweg / Set" else "Rückweg / Reset",
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = if (isHinwegActive) SignalGreenLight else SignalBlueLight,
            selectedLabelColor = if (isHinwegActive) SignalGreen else SignalBlue
          )
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // Hinweg (Grün)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(SignalGreenLight.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
          .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .background(SignalGreen, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = coupling.hinwegSet,
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF14532D),
          fontSize = 12.sp
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      // Rückweg (Blau)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(SignalBlueLight.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
          .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .background(SignalBlue, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = coupling.rueckwegReset,
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF1E40AF),
          fontSize = 12.sp
        )
      }
    }
  }
}

@Composable
fun GlobalFeedbackLoopBanner(modifier: Modifier = Modifier) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("global_feedback_loop_card"),
    colors = CardDefaults.cardColors(containerColor = FeedbackRedLight),
    border = BorderStroke(1.5.dp, FeedbackRed.copy(alpha = 0.6f)),
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier.padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .background(FeedbackRed, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.AllInclusive,
          contentDescription = "Global Feedback Loop",
          tint = Color.White,
          modifier = Modifier.size(24.dp)
        )
      }
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = "GLOBAL FEEDBACK LOOP (Roter Bogen)",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF991B1B)
        )
        Text(
          text = "Unendliche, unumkehrbare Rückkopplung: Gebühren & Werte aus Layer IV fließen ohne Abzug zurück in Layer I (Erfinder / MTK-Supply).",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFF7F1D1D)
        )
      }
    }
  }
}

@Composable
fun UrkundeCard(
  urkundenmuster: Urkundenmuster,
  kategorieId: Int,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .testTag("urkunde_card"),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDF7)),
    border = BorderStroke(2.dp, UrkundeGold),
    shape = RoundedCornerShape(8.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Urkunden-Kopf
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "AMTLICHES URKUNDENMUSTER",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.ExtraBold,
            color = UrkundeGold,
            letterSpacing = 1.sp
          )
          Text(
            text = "PRAI / MTK / NEC ÖKOSYSTEM",
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted
          )
        }
        Box(
          modifier = Modifier
            .size(36.dp)
            .background(UrkundeWax, CircleShape)
            .border(2.dp, UrkundeGold, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "§36",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          )
        }
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 10.dp),
        thickness = 1.dp,
        color = UrkundeGold.copy(alpha = 0.4f)
      )

      // Titel
      Text(
        text = urkundenmuster.titel,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy,
        fontFamily = FontFamily.Serif
      )
      Text(
        text = "Fachkategorie $kategorieId: ${urkundenmuster.kategorie}",
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        fontWeight = FontWeight.SemiBold
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Register & Stunden
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(UrkundeGoldBg.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
          .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text(text = "Registerformat:", fontSize = 11.sp, color = TextMuted)
          Text(
            text = urkundenmuster.registerFormat,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = BlueprintNavy
          )
        }
        Column(horizontalAlignment = Alignment.End) {
          Text(text = "Praxisleistung:", fontSize = 11.sp, color = TextMuted)
          Text(
            text = "${urkundenmuster.praxisstunden} Stunden",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF92400E)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // L&Q Nachweis
      Text(
        text = "L&Q-Nachweis (Lernen & Qualifikation):",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
      urkundenmuster.lqNachweis.forEach { item ->
        Text(
          text = "• $item",
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary,
          fontSize = 12.sp
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // F&S Nachweis
      Text(
        text = "F&S-Nachweis (Finanzen & Systemstruktur):",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
      urkundenmuster.fsNachweis.forEach { item ->
        Text(
          text = "• $item",
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary,
          fontSize = 12.sp
        )
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Signaturfelder
      Text(
        text = "Beglaubigung & Signaturfelder:",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = TextMuted
      )
      Spacer(modifier = Modifier.height(6.dp))
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        urkundenmuster.signaturFelder.take(3).forEach { sig ->
          Column(
            modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray)
            )
            Text(
              text = sig,
              fontSize = 9.sp,
              color = TextMuted,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(top = 2.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
fun OrganigrammTree(node: OrganigrammNode, level: Int = 0) {
  val indent = (level * 14).dp
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = indent, top = 4.dp, bottom = 4.dp)
  ) {
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(
        containerColor = when (level) {
          0 -> BlueprintNavy
          1 -> Slate800
          else -> Slate100
        }
      ),
      shape = RoundedCornerShape(8.dp),
      border = BorderStroke(1.dp, BlueprintBorder)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = node.label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (level < 2) Color.White else BlueprintNavy
          )
          if (node.role.isNotBlank()) {
            Text(
              text = node.role,
              style = MaterialTheme.typography.labelSmall,
              color = if (level < 2) Color(0xFF94A3B8) else TextSecondary
            )
          }
        }
        if (node.children.isNotEmpty()) {
          Text(
            text = "${node.children.size} Zweige",
            fontSize = 11.sp,
            color = if (level < 2) Color(0xFF38BDF8) else SignalBlue
          )
        }
      }
    }

    node.children.forEach { child ->
      OrganigrammTree(node = child, level = level + 1)
    }
  }
}

@Composable
fun BauplanGrid(raeume: List<BauplanRaum>, modifier: Modifier = Modifier) {
  Column(modifier = modifier.fillMaxWidth()) {
    Text(
      text = "Standardisierter Bauplan: Job-Zentrum & Akademie",
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.Bold,
      color = BlueprintNavy,
      modifier = Modifier.padding(bottom = 8.dp)
    )
    raeume.forEach { raum ->
      val (bgColor, tagColor) = when (raum.typ) {
        "studie" -> Color(0xFFEFF6FF) to SignalBlue
        "akademie" -> Color(0xFFF0FDF4) to SignalGreen
        "adapter" -> Color(0xFFFEF3C7) to UrkundeGold
        "park" -> Color(0xFFDCFCE7) to Color(0xFF15803D)
        "service" -> Color(0xFFF3E8FF) to Color(0xFF7E22CE)
        else -> Slate100 to TextSecondary
      }
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 3.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, tagColor.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(8.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = raum.name,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
            Text(
              text = raum.funktion,
              style = MaterialTheme.typography.bodySmall,
              color = TextSecondary,
              fontSize = 12.sp
            )
          }
          Badge(
            containerColor = tagColor,
            contentColor = Color.White,
            modifier = Modifier.padding(start = 8.dp)
          ) {
            Text(text = raum.flaeche, fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
