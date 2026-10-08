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
import androidx.compose.material.icons.outlined.*
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
import com.example.data.LayersData
import com.example.ui.components.GlobalFeedbackLoopBanner
import com.example.ui.components.SignalCouplingCard
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
  onNavigateToLayers: () -> Unit,
  onNavigateToCategories: () -> Unit,
  onNavigateToCategoryDetail: (Int) -> Unit,
  onNavigateToArenas: () -> Unit,
  onNavigateToTitles: () -> Unit,
  onNavigateToBlueprint: () -> Unit,
  onNavigateToWohnzentren: () -> Unit,
  onOpenSearch: () -> Unit = {},
  onNavigateToWallet: () -> Unit = {},
  onNavigateToExplorer: () -> Unit = {},
  onNavigateToProfile: () -> Unit = {},
  onNavigateToWebPortal: () -> Unit = {},
  onNavigateToRAppCenter: () -> Unit = {},
  onOpenTrading: () -> Unit = {},
  onOpenAuth: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  var sc1Hinweg by remember { mutableStateOf(true) }
  var sc2Hinweg by remember { mutableStateOf(true) }
  var sc3Hinweg by remember { mutableStateOf(true) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
  ) {
    // Header Banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("dashboard_header_card"),
        colors = CardDefaults.cardColors(containerColor = BlueprintNavy),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "PRAI / MTK / NEC",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 1.sp
              )
              Text(
                text = "Dezentrales Wirtschafts- & Qualifikationsökosystem",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8)
              )
            }
            Box(
              modifier = Modifier
                .background(SignalGreen, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Text("AUTARK", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))
          Text(
            text = "Selbsttragendes System mit 4 Schichten, 28 Fachkategorien, 8 Perspektiven, Scheckverbriefung (§ 36 BeurkG) und unberührbarem Gebührenrückfluss.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFE2E8F0),
            fontSize = 13.sp
          )
        }
      }
      Spacer(modifier = Modifier.height(12.dp))
    }

    // Dual Web & GitHub Ecosystem Banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToRAppCenter() }
          .testTag("dashboard_dual_portal_banner"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, UrkundeGold),
        shape = RoundedCornerShape(12.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                color = UrkundeGoldBg,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.size(34.dp)
              ) {
                Box(contentAlignment = Alignment.Center) {
                  Icon(Icons.Default.Language, contentDescription = null, tint = UrkundeGoldDark, modifier = Modifier.size(20.dp))
                }
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Web App ⮂ Android App ⮂ GitHub",
                  fontWeight = FontWeight.Black,
                  fontSize = 13.sp,
                  color = BlueprintNavy
                )
                Text(
                  text = "Simultan: Browser-Web-App & Native Android-App v8.0",
                  fontSize = 11.sp,
                  color = TextSecondary
                )
              }
            }

            Surface(
              color = SignalGreen.copy(alpha = 0.15f),
              shape = RoundedCornerShape(12.dp)
            ) {
              Text(
                text = "DUAL LIVE",
                color = SignalGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = onNavigateToRAppCenter,
              colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("rApp Center", fontSize = 11.sp, maxLines = 1)
            }

            OutlinedButton(
              onClick = onNavigateToWebPortal,
              border = BorderStroke(1.dp, BlueprintNavy),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Language, contentDescription = null, tint = BlueprintNavy, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Web App (Live)", color = BlueprintNavy, fontSize = 11.sp, maxLines = 1)
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(12.dp))
    }

    // Global Search Trigger Bar
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onOpenSearch() }
          .testTag("dashboard_global_search_bar"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
          ) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "Suche",
              tint = BlueprintNavy
            )
            Column {
              Text(
                text = "Globale Suche (28 Kategorien & 8 Perspektiven)",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = BlueprintNavy,
                fontSize = 13.sp
              )
              Text(
                text = "Name, ID (#1–28), Rechtsform (©, GbR, Stiftung) oder Fachsichten...",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                fontSize = 11.sp
              )
            }
          }
          Surface(
            color = SignalBlueLight,
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "ÖFFNEN",
              color = SignalBlue,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Key Metrics Strip
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        MetricCard(
          title = "28",
          subtitle = "Fachkategorien",
          icon = Icons.Default.Category,
          color = SignalBlue,
          modifier = Modifier.weight(1f).clickable { onNavigateToCategories() }
        )
        MetricCard(
          title = "4 Layer",
          subtitle = "Kybernetik",
          icon = Icons.Default.Layers,
          color = BlueprintNavy,
          modifier = Modifier.weight(1f).clickable { onNavigateToLayers() }
        )
        MetricCard(
          title = "8 Arenen",
          subtitle = "Ebenen",
          icon = Icons.Default.WorkspacePremium,
          color = UrkundeGold,
          modifier = Modifier.weight(1f).clickable { onNavigateToArenas() }
        )
        MetricCard(
          title = "3+1",
          subtitle = "Jahres-Pfad",
          icon = Icons.Default.School,
          color = SignalGreen,
          modifier = Modifier.weight(1f).clickable { onNavigateToTitles() }
        )
      }
      Spacer(modifier = Modifier.height(14.dp))
    }

    // Web3 & Escrow Hub + Blockchain & NEC Explorer Cards
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Card(
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToWallet() }
            .testTag("dashboard_web3_wallet_card"),
          colors = CardDefaults.cardColors(containerColor = BlueprintNavy),
          shape = RoundedCornerShape(10.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(30.dp)
                  .background(UrkundeGoldBg, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = UrkundeGoldDark, modifier = Modifier.size(16.dp))
              }
              Surface(color = Color(0xFF1E293B), shape = RoundedCornerShape(4.dp)) {
                Text("W3CONNECT", fontSize = 8.sp, color = UrkundeGold, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
              }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Treuhand & Wallet", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("BTC • ETH • TON • MTK", color = Color(0xFF94A3B8), fontSize = 10.sp)
          }
        }

        Card(
          modifier = Modifier
            .weight(1f)
            .clickable { onNavigateToExplorer() }
            .testTag("dashboard_nec_explorer_card"),
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
              Box(
                modifier = Modifier
                  .size(30.dp)
                  .background(SignalBlueLight, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.Explore, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(16.dp))
              }
              Surface(color = SignalGreenLight, shape = RoundedCornerShape(4.dp)) {
                Text("DUAL-NFT", fontSize = 8.sp, color = SignalGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
              }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Blockchain & NEC", color = BlueprintNavy, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text("Urkunden & Nachweise", color = TextMuted, fontSize = 10.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = onOpenTrading,
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(vertical = 6.dp, horizontal = 8.dp)
        ) {
          Icon(Icons.Default.SwapHoriz, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Trading / Swap", fontSize = 11.sp, color = SignalBlue, fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
          onClick = onNavigateToProfile,
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(vertical = 6.dp, horizontal = 8.dp)
        ) {
          Icon(Icons.Default.Folder, contentDescription = null, tint = UrkundeGoldDark, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Code-Repos (©/GbR)", fontSize = 11.sp, color = BlueprintNavy, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // Global Feedback Loop Arc Indicator
    item {
      GlobalFeedbackLoopBanner()
      Spacer(modifier = Modifier.height(16.dp))
    }

    // Interactive 4 Layers Stack
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "GLOBAL ARCHITECTURE (LAYER I – IV)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = BlueprintNavy
        )
        TextButton(onClick = onNavigateToLayers) {
          Text("Explorer öffnen", fontSize = 13.sp, color = SignalBlue)
        }
      }
      Spacer(modifier = Modifier.height(8.dp))
    }

    // LAYER I
    item {
      LayerOverviewCard(
        layer = LayersData.layers[0],
        color = Color(0xFF1E3A8A),
        badge = "SUPER-AXIOM",
        onClick = onNavigateToLayers
      )
    }

    // SIGNAL COUPLING 1
    item {
      SignalCouplingCard(
        coupling = LayersData.signalCouplings[0],
        isHinwegActive = sc1Hinweg,
        onToggle = { sc1Hinweg = !sc1Hinweg }
      )
    }

    // LAYER II
    item {
      LayerOverviewCard(
        layer = LayersData.layers[1],
        color = Color(0xFF0F766E),
        badge = "0% STEUERLAST",
        onClick = onNavigateToLayers
      )
    }

    // SIGNAL COUPLING 2
    item {
      SignalCouplingCard(
        coupling = LayersData.signalCouplings[1],
        isHinwegActive = sc2Hinweg,
        onToggle = { sc2Hinweg = !sc2Hinweg }
      )
    }

    // LAYER III
    item {
      LayerOverviewCard(
        layer = LayersData.layers[2],
        color = Color(0xFF7C3AED),
        badge = "§ 36 BeurkG",
        onClick = onNavigateToLayers
      )
    }

    // SIGNAL COUPLING 3
    item {
      SignalCouplingCard(
        coupling = LayersData.signalCouplings[2],
        isHinwegActive = sc3Hinweg,
        onToggle = { sc3Hinweg = !sc3Hinweg }
      )
    }

    // LAYER IV
    item {
      LayerOverviewCard(
        layer = LayersData.layers[3],
        color = Color(0xFFB45309),
        badge = "MATHEMATISCH",
        onClick = onNavigateToLayers
      )
      Spacer(modifier = Modifier.height(16.dp))
    }

    // Right-docked block note
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToLayers() },
        colors = CardDefaults.cardColors(containerColor = Slate100),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(10.dp)
      ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = SignalBlue, modifier = Modifier.size(24.dp))
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "KUNDEN / NEHMER (Rechts-angedockt)",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
            Text(
              text = "Ausschließliche Initiierung über physische NEC-Zertifikate. Keinerlei Direktkontakt zum Erfinder.",
              style = MaterialTheme.typography.bodySmall,
              color = TextSecondary
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(20.dp))
    }

    // Featured Categories Preview
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "28 KATEGORIEN (SCHNELLZUGRIFF)",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = BlueprintNavy
        )
        TextButton(onClick = onNavigateToCategories) {
          Text("Alle 28 ansehen", fontSize = 13.sp, color = SignalBlue)
        }
      }
      Spacer(modifier = Modifier.height(6.dp))
    }

    val sampleCats = CategoriesRepository.allCategories.take(4)
    items(sampleCats.size) { idx ->
      val cat = sampleCats[idx]
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp)
          .clickable { onNavigateToCategoryDetail(cat.id) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(8.dp)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
              modifier = Modifier
                .size(30.dp)
                .background(BlueprintNavy, RoundedCornerShape(6.dp)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = "${cat.id}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = cat.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = BlueprintNavy
              )
              Text(
                text = "${cat.rechtsform} • Kreis ${cat.ebeneKreis} • ${cat.subcategories.size} Unterpunkte (8 Sichten)",
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                fontSize = 11.sp
              )
            }
          }
          Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
        }
      }
    }
  }
}

@Composable
private fun MetricCard(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, BlueprintBorder),
    shape = RoundedCornerShape(8.dp)
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
      Spacer(modifier = Modifier.height(4.dp))
      Text(text = title, fontWeight = FontWeight.Black, fontSize = 15.sp, color = BlueprintNavy)
      Text(text = subtitle, fontSize = 10.sp, color = TextMuted)
    }
  }
}

@Composable
private fun LayerOverviewCard(
  layer: com.example.model.Layer,
  color: Color,
  badge: String,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
      .clickable { onClick() }
      .testTag("layer_card_${layer.id}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
    shape = RoundedCornerShape(10.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .background(color, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Text(text = layer.id, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = layer.name,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
        }
        Badge(containerColor = color.copy(alpha = 0.15f), contentColor = color) {
          Text(badge, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = layer.untertitel,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = color
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = layer.superAxiomNotice,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        fontSize = 12.sp
      )
    }
  }
}
