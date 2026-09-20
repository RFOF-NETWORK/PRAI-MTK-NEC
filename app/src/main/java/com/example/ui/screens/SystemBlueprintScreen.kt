package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.LayersData
import com.example.ui.components.BankingCubeCard
import com.example.ui.components.GlobalFeedbackLoopBanner
import com.example.ui.theme.*

@Composable
fun SystemBlueprintScreen(modifier: Modifier = Modifier) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Text(
        text = "SYSTEM-BLUEPRINT & BANKING CUBES",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
      Text(
        text = "Mathematische Schöpfung, Treuhandarchitektur und insolvenzresistente Pfandrechte.",
        style = MaterialTheme.typography.bodySmall,
        color = TextMuted
      )
    }

    item {
      GlobalFeedbackLoopBanner()
    }

    // Die 4 Banking Cubes Section
    item {
      Text(
        text = "DIE 4 BANKING CUBES (TREUGEBER VS. TREUHAND)",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
      Text(
        text = "Strikte Trennung von Verfügungs- und Treuhandgewalt schützt Vermögen vor Zwangsvollstreckung.",
        style = MaterialTheme.typography.bodySmall,
        color = TextMuted
      )
    }

    LayersData.bankingCubes.forEach { cube ->
      item {
        BankingCubeCard(cube = cube)
      }
    }

    // Super-Axiom & Formtrennung Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Slate100),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Shield, contentDescription = null, tint = BlueprintNavy)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "SUPER-AXIOM & FORMTRENNUNG",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Der Erfinder (Layer I) agiert als reiner Schöpfer und Rechteinhaber. Durch Zwischenschaltung der rechtlichen Adapter-Hüllen (Layer II: © [Urheberschaftshülle], GbR/eGbR/geGbR und Stiftungen) entsteht eine unüberwindbare rechtliche Brandschutzmauer. Der Erfinder erzielt keinerlei gewerbliche Einkünfte; alle Einnahmen werden als Betriebsausgaben für Infrastruktur reinvestiert.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            lineHeight = 18.sp
          )
        }
      }
    }

    // Pfandrechte § 1274 / § 1280 BGB
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = UrkundeGoldBg.copy(alpha = 0.3f)),
        border = BorderStroke(1.dp, UrkundeGold),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Gavel, contentDescription = null, tint = Color(0xFF92400E))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "PFANDRECHT AN RECHTEN (§§ 1274, 1280 BGB)",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF92400E)
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Guthaben auf Cube 3c und 3d unterliegen sofortigen notariellen Pfandrechten zu Gunsten der Partner und Dienstleister. Gemäß § 1274 BGB i.V.m. § 1280 BGB erlangt das Pfandrecht mit förmlicher Anzeige an die Treuhandbank sofortige Drittwirkung. Gläubiger dritter Parteien können nicht in diese zweckgebundenen Sicherungsfonds vollstrecken.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            lineHeight = 18.sp
          )
        }
      }
    }

    // Tokenomics & Linearer Wertzuwachs
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SignalGreenLight.copy(alpha = 0.3f)),
        border = BorderStroke(1.dp, SignalGreen),
        shape = RoundedCornerShape(10.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = SignalGreen)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "TOKENOMICS & UNBERÜHRBARER GEBÜHRENFLUSS",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF14532D)
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Die Währung MTK wird mit 0 Kapitalkosten geschöpft (0-Kosten-Minting). Jede interne oder externe Transaktion unterliegt einem automatisierten, unberührbaren Gebührenabzug. Dieser Gebührenfluss wird unumkehrbar in den Währungspool re-injiziert. Daraus resultiert ein mathematisch determinierter, stetig steigender Mindestwert je Einheit, unabhängig von Spekulationsmärkten.",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            lineHeight = 18.sp
          )
        }
      }
    }
  }
}
