package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun PerspectiveTableView(subcategories: List<Subcategory>, modifier: Modifier = Modifier) {
  val horizontalScroll = rememberScrollState()

  Column(modifier = modifier.fillMaxWidth()) {
    Text(
      text = "8-Sichten-Matrix (Vollständige Perspektiven)",
      style = MaterialTheme.typography.titleSmall,
      fontWeight = FontWeight.Bold,
      color = BlueprintNavy,
      modifier = Modifier.padding(bottom = 6.dp)
    )
    Text(
      text = "Horizontal scrollbare Tabelle aller 8 Dimensionen je Unterkategorie.",
      style = MaterialTheme.typography.bodySmall,
      color = TextMuted,
      modifier = Modifier.padding(bottom = 10.dp)
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(horizontalScroll)
    ) {
      Column(modifier = Modifier.width(1350.dp)) {
        // Table Header
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(BlueprintNavy, RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .padding(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          HeaderCell("Unterkategorie", width = 160.dp)
          HeaderCell("1. Service", width = 150.dp)
          HeaderCell("2. Produkt", width = 150.dp)
          HeaderCell("3. Rohstoff", width = 140.dp)
          HeaderCell("4. Erfinder", width = 150.dp)
          HeaderCell("5. Partner", width = 150.dp)
          HeaderCell("6. Kunde", width = 150.dp)
          HeaderCell("7. L&Q", width = 150.dp)
          HeaderCell("8. F&S", width = 150.dp)
        }

        // Table Rows
        subcategories.forEachIndexed { index, sub ->
          val rowBg = if (index % 2 == 0) Color.White else Slate50
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(rowBg)
              .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.width(160.dp).padding(end = 8.dp)) {
              Text(
                text = sub.id,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = SignalBlue
              )
              Text(
                text = sub.name,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = BlueprintNavy
              )
            }

            BodyCell(sub.perspectives.service, width = 150.dp)
            BodyCell(sub.perspectives.produkt, width = 150.dp)
            BodyCell(sub.perspectives.rohstoff, width = 140.dp)
            BodyCell(sub.perspectives.erfinder, width = 150.dp)
            BodyCell(sub.perspectives.partner, width = 150.dp)
            BodyCell(sub.perspectives.kunde, width = 150.dp)
            BodyCell(sub.perspectives.lernen, width = 150.dp, isHighlight = true, highlightColor = SignalGreenLight)
            BodyCell(sub.perspectives.finanzen, width = 150.dp, isHighlight = true, highlightColor = SignalBlueLight)
          }
          HorizontalDivider(thickness = 0.5.dp, color = BlueprintBorder)
        }
      }
    }
  }
}

@Composable
private fun HeaderCell(title: String, width: androidx.compose.ui.unit.Dp) {
  Text(
    text = title,
    style = MaterialTheme.typography.labelMedium,
    fontWeight = FontWeight.Bold,
    color = Color.White,
    modifier = Modifier.width(width).padding(horizontal = 4.dp)
  )
}

@Composable
private fun BodyCell(
  text: String,
  width: androidx.compose.ui.unit.Dp,
  isHighlight: Boolean = false,
  highlightColor: Color = Color.Transparent
) {
  Box(
    modifier = Modifier
      .width(width)
      .padding(horizontal = 4.dp)
      .background(if (isHighlight) highlightColor.copy(alpha = 0.4f) else Color.Transparent, RoundedCornerShape(4.dp))
      .padding(4.dp)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.bodySmall,
      color = TextSecondary,
      fontSize = 11.sp,
      lineHeight = 15.sp
    )
  }
}

@Composable
fun ReferralAndCapitalFlowDiagram(
  referralKette: List<ReferralStep>,
  kapitalfluss: List<KapitalflussStep>,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.fillMaxWidth()) {
    // Referral Chain Card
    Card(
      modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
      colors = CardDefaults.cardColors(containerColor = Slate50),
      border = BorderStroke(1.dp, BlueprintBorder),
      shape = RoundedCornerShape(10.dp)
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Share, contentDescription = null, tint = SignalGreen, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Universelle Referral-Kette (E → P → K → NEC → E)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
        }
        Spacer(modifier = Modifier.height(10.dp))

        referralKette.forEachIndexed { idx, step ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(28.dp)
                .background(Color(step.colorHex), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(text = step.role, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(text = step.title, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
              Text(text = step.description, style = MaterialTheme.typography.labelSmall, color = TextMuted)
            }
            if (idx < referralKette.size - 1) {
              Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
            }
          }
        }
      }
    }

    // Capital Flow Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = Slate50),
      border = BorderStroke(1.dp, BlueprintBorder),
      shape = RoundedCornerShape(10.dp)
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.MonetizationOn, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Mathematisch garantierter Kapitalfluss",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
        }
        Spacer(modifier = Modifier.height(10.dp))

        kapitalfluss.forEach { step ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .background(Color.White, RoundedCornerShape(6.dp))
              .border(1.dp, BlueprintBorder, RoundedCornerShape(6.dp))
              .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "${step.von}  →  ${step.nach}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = BlueprintNavy
              )
              Text(
                text = step.mechanismus,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
              )
            }
            Box(
              modifier = Modifier
                .size(8.dp)
                .background(Color(step.colorHex), CircleShape)
            )
          }
        }
      }
    }
  }
}

@Composable
fun BankingCubeCard(cube: BankingCube, modifier: Modifier = Modifier) {
  val isTreugeber = cube.typ == "Treugeber"
  val headerColor = if (isTreugeber) Color(0xFF1E3A8A) else Color(0xFF065F46)
  val badgeColor = if (isTreugeber) SignalBlue else SignalGreen

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
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
          Box(
            modifier = Modifier
              .size(26.dp)
              .background(badgeColor, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
          ) {
            Text(text = cube.id, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = cube.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
        }
        Badge(containerColor = badgeColor.copy(alpha = 0.15f), contentColor = badgeColor) {
          Text(text = cube.typ, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Inhaber: ${cube.inhaber}",
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = TextSecondary
      )
      Text(
        text = "Verfügungsmacht: ${cube.verfuegungsmacht}",
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Zweck: ${cube.zweck}",
        style = MaterialTheme.typography.bodySmall,
        color = TextMuted,
        fontSize = 11.sp
      )
    }
  }
}
