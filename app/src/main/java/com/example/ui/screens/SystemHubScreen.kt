package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun SystemHubScreen(
  onNavigateToTitles: () -> Unit,
  onNavigateToBlueprint: () -> Unit,
  onNavigateToWohnzentren: () -> Unit,
  onNavigateToGlossar: () -> Unit,
  onNavigateToHealth: () -> Unit,
  onNavigateToWallet: () -> Unit = {},
  onNavigateToExplorer: () -> Unit = {},
  onNavigateToProfile: () -> Unit = {},
  onNavigateToWebPortal: () -> Unit = {},
  onOpenAuth: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text(
        text = "SYSTEM & ARCHITEKTUR",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
      Text(
        text = "Rechtsstrukturen, Siedlungsmodelle, Web3-Infrastruktur und Urkunden-Registry.",
        style = MaterialTheme.typography.bodySmall,
        color = TextMuted,
        modifier = Modifier.padding(bottom = 6.dp)
      )
    }

    item {
      HubItemCard(
        title = "Web App & GitHub Dual-Portal",
        subtitle = "rfof-network.github.io/PRAI-MTK-NEC, GitHub Repo v8.0 & gh CLI Klonbefehl",
        icon = Icons.Default.Language,
        iconTint = UrkundeGoldDark,
        onClick = onNavigateToWebPortal
      )
    }

    item {
      HubItemCard(
        title = "Profil, Repositories & VS Code Settings",
        subtitle = "Repositories veröffentlichen, Organisationen (©/GbR/eGbR/Stiftung), SSH/GPG & RFOF Pages",
        icon = Icons.Default.ManageAccounts,
        iconTint = SignalGreen,
        onClick = onNavigateToProfile
      )
    }

    item {
      HubItemCard(
        title = "Web3 Multi-Chain & Escrow-Wallet",
        subtitle = "Treuhand- und Treugeber-Wallet, BTC, ETH, TON, MTK, Staking & Mining",
        icon = Icons.Default.AccountBalanceWallet,
        iconTint = SignalBlue,
        onClick = onNavigateToWallet
      )
    }

    item {
      HubItemCard(
        title = "Blockchain & NEC-Zertifikate Explorer",
        subtitle = "Blöcke, Transaktionen & Urkunden-Download mit Beglaubigung nach § 36 BeurkG",
        icon = Icons.Default.Explore,
        iconTint = UrkundeGold,
        onClick = onNavigateToExplorer
      )
    }

    item {
      HubItemCard(
        title = "Authentifizierung & Rollenverwaltung",
        subtitle = "Admin (RFOF-NETWORK) vs. Nutzer (Google, Web3, Gast) Berechtigungsstatus",
        icon = Icons.Default.Security,
        iconTint = BlueprintNavy,
        onClick = onOpenAuth
      )
    }

    item {
      HubItemCard(
        title = "Titel & 3+1-Jahres-Pfad",
        subtitle = "NEC Management Verwalter (3 J.) & Master of Quantum (4 J.)",
        icon = Icons.Default.School,
        iconTint = SignalGreen,
        onClick = onNavigateToTitles
      )
    }

    item {
      HubItemCard(
        title = "System-Blueprint & Banking Cubes",
        subtitle = "Die 4 Banking Cubes (3a–3d), Super-Axiom & Pfandrechte nach §§ 1274, 1280 BGB",
        icon = Icons.Default.AccountBalance,
        iconTint = SignalBlue,
        onClick = onNavigateToBlueprint
      )
    }

    item {
      HubItemCard(
        title = "Wohnzentren & 2-Kreise-Modell",
        subtitle = "Physischer Bauplan mit zentralem Park & autarken Siedlungsmodulen",
        icon = Icons.Default.HomeWork,
        iconTint = UrkundeGold,
        onClick = onNavigateToWohnzentren
      )
    }

    item {
      HubItemCard(
        title = "Glossar & FAQ",
        subtitle = "Begriffsdefinitionen (A–Z) und Antworten zu zentralen Systemfragen",
        icon = Icons.Default.HelpOutline,
        iconTint = BlueprintNavy,
        onClick = onNavigateToGlossar
      )
    }

    item {
      HubItemCard(
        title = "Data Health Check & Export",
        subtitle = "Integritätsstatus (100% grün), JSON Export & Druckansicht",
        icon = Icons.Default.CheckCircleOutline,
        iconTint = SignalGreen,
        onClick = onNavigateToHealth
      )
    }
  }
}

@Composable
private fun HubItemCard(
  title: String,
  subtitle: String,
  icon: ImageVector,
  iconTint: Color,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, BlueprintBorder),
    shape = RoundedCornerShape(10.dp)
  ) {
    Row(
      modifier = Modifier.padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
        Box(
          modifier = Modifier
            .size(40.dp)
            .background(iconTint.copy(alpha = 0.12f), RoundedCornerShape(8.dp)),
          contentAlignment = Alignment.Center
        ) {
          Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = BlueprintNavy)
          Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 12.sp)
        }
      }
      Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
    }
  }
}
