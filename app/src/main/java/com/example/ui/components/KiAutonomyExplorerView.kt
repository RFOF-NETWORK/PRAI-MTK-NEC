package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ai.AutonomousKiEngine
import com.example.ai.FlashLoanArbitrageEvent
import com.example.ai.FractalHashLink
import com.example.ai.KiBlockLiveActivity
import com.example.ui.theme.*

@Composable
fun KiAutonomyExplorerView(
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val engineStatus by AutonomousKiEngine.engineStatus.collectAsState()
  val activityStream by AutonomousKiEngine.liveActivityStream.collectAsState()
  val flashLoans by AutonomousKiEngine.flashLoanHistory.collectAsState()
  val fractalLinks by AutonomousKiEngine.fractalLinks.collectAsState()

  var selectedSubTab by remember { mutableStateOf(0) }
  val subTabs = listOf("Live-Fluss & Blöcke", "Flash-Loan Arbitrage (KI)", "Fraktal-Hash Links")

  var selectedFractalLink by remember { mutableStateOf<FractalHashLink?>(null) }
  var selectedFlashLoan by remember { mutableStateOf<FlashLoanArbitrageEvent?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundLight)
      .testTag("ki_autonomy_explorer_view")
  ) {
    // Top KI Status & Price-Evolution Banner
    Surface(
      color = Color(0xFF1E1135),
      modifier = Modifier.fillMaxWidth()
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
                .size(34.dp)
                .background(Color(0xFF8B5CF6).copy(alpha = 0.25f), CircleShape)
                .border(1.5.dp, Color(0xFFA855F7), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFA855F7), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "PRAI GUARDIAN KI-ENGINE",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
              Text(
                text = "Vollautonomes Mining • Flash Loans • Gebühren-Liquidierung",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFD8B4FE),
                fontSize = 10.sp
              )
            }
          }

          // Trigger Action Button
          Button(
            onClick = {
              AutonomousKiEngine.triggerAutonomousCycle()
              Toast.makeText(context, "⚡ KI-Zyklus manuell forciert! Blöcke & Arbitrage aktualisiert.", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA855F7)),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
            modifier = Modifier.testTag("trigger_ki_cycle_button")
          ) {
            Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(15.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Zyklus starten", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Price & Liquidity Cards Row (Showing independent price rise!)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // MTK Card
          Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2D1B4E)),
            border = BorderStroke(1.dp, UrkundeGoldDark.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("MTK SOVEREIGN", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UrkundeGold)
                Text("📈 Auto-Steigend", fontSize = 9.sp, color = SignalGreen)
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "${String.format("%.3f", engineStatus.mtkCurrentPriceEur)} €",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
              Text(
                text = "Pool: ${String.format("%,.0f", engineStatus.mtkLiquidityEur)} €",
                fontSize = 9.sp,
                color = Color(0xFFCBD5E1)
              )
              Text(
                text = "Eingespeist: +${String.format("%,.0f", engineStatus.totalLiquidatedToMtkEur)} €",
                fontSize = 9.sp,
                color = SignalGreen,
                fontWeight = FontWeight.Bold
              )
            }
          }

          // ZON Card
          Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B3330)),
            border = BorderStroke(1.dp, SignalGreen.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.weight(1f)
          ) {
            Column(modifier = Modifier.padding(10.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text("ZON BONDING 50/50", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SignalGreen)
                Text("⚡ 0-Supply Start", fontSize = 9.sp, color = Color(0xFF6EE7B7))
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "${String.format("%.3f", engineStatus.zonCurrentPriceEur)} €",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
              Text(
                text = "Reserve: ${String.format("%,.0f", engineStatus.zonLiquidityEur)} €",
                fontSize = 9.sp,
                color = Color(0xFFCBD5E1)
              )
              Text(
                text = "Eingespeist: +${String.format("%,.0f", engineStatus.totalLiquidatedToZonEur)} €",
                fontSize = 9.sp,
                color = SignalGreen,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Explanation Pill
        Surface(
          color = Color(0xFF261842),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "🛡️ Die KI führt Flash Loans & Fraktal-Schürfungen völlig unabhängig von Kundenorders durch. Alle Gewinne & Gebühren fließen zu 100% in die MTK- & ZON-Liquiditätsreserven – der Kurs steigt mathematisch garantiert.",
            fontSize = 9.sp,
            color = Color(0xFFE9D5FF),
            lineHeight = 13.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
          )
        }
      }
    }

    // Sub-Tabs
    TabRow(
      selectedTabIndex = selectedSubTab,
      containerColor = Color.White,
      contentColor = BlueprintNavy
    ) {
      subTabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedSubTab == index,
          onClick = { selectedSubTab = index },
          text = {
            Text(
              text = title,
              fontSize = 11.sp,
              fontWeight = if (selectedSubTab == index) FontWeight.Bold else FontWeight.Normal
            )
          }
        )
      }
    }

    // Tab Contents
    when (selectedSubTab) {
      0 -> KiLiveActivityStreamContent(
        activities = activityStream,
        onInspectFractal = { selectedFractalLink = it }
      )
      1 -> FlashLoanArbitrageContent(
        flashLoans = flashLoans,
        onInspect = { selectedFlashLoan = it }
      )
      2 -> FractalLinksContent(
        links = fractalLinks,
        onInspect = { selectedFractalLink = it }
      )
    }
  }

  // Fractal Link Inspection Dialog
  selectedFractalLink?.let { link ->
    FractalLinkDetailDialog(
      link = link,
      onDismiss = { selectedFractalLink = null }
    )
  }

  // Flash Loan Detail Dialog
  selectedFlashLoan?.let { arb ->
    FlashLoanDetailDialog(
      arb = arb,
      onDismiss = { selectedFlashLoan = null }
    )
  }
}

// -------------------------------------------------------------
// LIVE STREAM TAB CONTENT
// -------------------------------------------------------------
@Composable
private fun KiLiveActivityStreamContent(
  activities: List<KiBlockLiveActivity>,
  onInspectFractal: (FractalHashLink) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "ECHTZEIT-BLOCKFLUSS DER AUTONOMEN KI",
          style = MaterialTheme.typography.labelSmall,
          fontWeight = FontWeight.Bold,
          color = BlueprintNavy
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(modifier = Modifier.size(8.dp).background(SignalGreen, CircleShape))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Live-Puls aktiv", fontSize = 10.sp, color = SignalGreen, fontWeight = FontWeight.Bold)
        }
      }
    }

    items(activities) { act ->
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, if (act.blockType == "FLASH_LOAN_ARBITRAGE") Color(0xFFA855F7).copy(alpha = 0.5f) else BlueprintBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .background(
                    if (act.blockType == "FLASH_LOAN_ARBITRAGE") Color(0xFFF3E8FF) else Color(0xFFE0F2FE),
                    CircleShape
                  ),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = if (act.blockType == "FLASH_LOAN_ARBITRAGE") "⚡" else "⛏️",
                  fontSize = 11.sp
                )
              }
              Text(
                text = "Block #${act.height}",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = BlueprintNavy
              )
            }

            Surface(
              color = SignalGreenLight,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = "+${String.format("%.2f", act.liquidatedAmountEur)} € Liquidiert",
                color = SignalGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text(text = act.title, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = BlueprintNavy)
          Spacer(modifier = Modifier.height(2.dp))
          Text(text = act.details, fontSize = 10.sp, color = TextSecondary, lineHeight = 14.sp)

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Fraktal-Hash: ${act.fractalLink.fractalHash.take(16)}...",
              fontSize = 9.sp,
              color = TextMuted,
              fontFamily = FontFamily.Monospace
            )
            TextButton(
              onClick = { onInspectFractal(act.fractalLink) },
              contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
              modifier = Modifier.height(24.dp)
            ) {
              Text("Fraktal-Link prüfen", fontSize = 9.sp, color = Color(0xFFA855F7), fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// FLASH LOAN ARBITRAGE CONTENT
// -------------------------------------------------------------
@Composable
private fun FlashLoanArbitrageContent(
  flashLoans: List<FlashLoanArbitrageEvent>,
  onInspect: (FlashLoanArbitrageEvent) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    item {
      Surface(
        color = Color(0xFFF3E8FF),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color(0xFFA855F7).copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(10.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Security, contentDescription = null, tint = Color(0xFFA855F7), modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("KI-EXKLUSIVES FLASH-LOAN PROTOKOLL", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color(0xFF581C87))
          }
          Spacer(modifier = Modifier.height(3.dp))
          Text(
            text = "Diese Funktion steht ausschließlich der autonomen KI zur Verfügung. Kunden und Admins können keine manuellen Flash Loans triggern, um Systemstabilität und risikofreie Arbitrage-Liquidierung zu sichern.",
            fontSize = 10.sp,
            color = Color(0xFF6B21A8),
            lineHeight = 14.sp
          )
        }
      }
    }

    items(flashLoans) { arb ->
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onInspect(arb) }
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = arb.id,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp,
              color = Color(0xFFA855F7)
            )
            Text(
              text = "Kredit: ${String.format("%,.0f", arb.borrowedAmountEur)} €",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = BlueprintNavy
            )
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text(text = "Route: ${arb.dexRoute}", fontSize = 10.sp, color = TextSecondary)

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text("Netto-Arbitrage", fontSize = 9.sp, color = TextMuted)
              Text("+${String.format("%.2f", arb.netProfitLiquidatedEur)} €", fontSize = 12.sp, fontWeight = FontWeight.Black, color = SignalGreen)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text("MTK / ZON Split (50/50)", fontSize = 9.sp, color = TextMuted)
              Text("je +${String.format("%.2f", arb.mtkPoolInjectionEur)} €", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = UrkundeGoldDark)
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// FRACTAL LINKS CONTENT
// -------------------------------------------------------------
@Composable
private fun FractalLinksContent(
  links: List<FractalHashLink>,
  onInspect: (FractalHashLink) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    item {
      Text(
        text = "FRAKTAL-HASHING KETTEN-VERKNÜPFUNG (JK-AUTOMATON)",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
    }

    items(links) { link ->
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onInspect(link) }
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = "Fraktal-Tiefe ${link.fractalDepth} (Block #${link.blockHeight})",
              fontWeight = FontWeight.Bold,
              fontSize = 11.sp,
              color = BlueprintNavy
            )
            Text(
              text = "Status: Notariell Verankert",
              fontSize = 9.sp,
              color = SignalGreen,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(4.dp))
          Text(text = "Koordinate: ${link.seedCoordinate}", fontSize = 10.sp, color = Color(0xFFA855F7), fontFamily = FontFamily.Monospace)
          Text(text = "Fraktal-Hash: ${link.fractalHash}", fontSize = 9.sp, color = TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis, fontFamily = FontFamily.Monospace)
          Text(text = "Notar-Root: ${link.notarialSealRoot.take(28)}... (§ 36 BeurkG)", fontSize = 9.sp, color = UrkundeGoldDark)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// FRACTAL LINK DETAIL DIALOG
// -------------------------------------------------------------
@Composable
private fun FractalLinkDetailDialog(
  link: FractalHashLink,
  onDismiss: () -> Unit
) {
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "FRAKTAL-HASHING LINK DETAILS",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Black,
            color = BlueprintNavy
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Schließen")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
          color = Slate50,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Block-Höhe: #${link.blockHeight}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
            Text("Fraktal-Tiefe: ${link.fractalDepth}", fontSize = 10.sp, color = TextSecondary)
            Text("Mandelbrot/Julia Seed: ${link.seedCoordinate}", fontSize = 10.sp, color = Color(0xFFA855F7), fontFamily = FontFamily.Monospace)
            Text("Parent Hash: ${link.parentHash}", fontSize = 9.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
            Text("Fraktal-Hash: ${link.fractalHash}", fontSize = 9.sp, color = SignalBlue, fontFamily = FontFamily.Monospace)
            Text("JK-Automaton Zustand: ${link.jkAutomatonState}", fontSize = 9.sp, color = UrkundeGoldDark, fontFamily = FontFamily.Monospace)
            Text("Notarieller Wurzel-Hash: ${link.notarialSealRoot} (§ 36 BeurkG)", fontSize = 9.sp, color = SignalGreen, fontFamily = FontFamily.Monospace)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = {
            clipboardManager.setText(AnnotatedString(link.fractalHash))
            Toast.makeText(context, "Fraktal-Hash in Zwischenablage kopiert!", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Vollständigen Fraktal-Hash kopieren", fontSize = 11.sp)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// FLASH LOAN DETAIL DIALOG
// -------------------------------------------------------------
@Composable
private fun FlashLoanDetailDialog(
  arb: FlashLoanArbitrageEvent,
  onDismiss: () -> Unit
) {
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "FLASH LOAN ARBITRAGE RECEIPT",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Black,
            color = Color(0xFFA855F7)
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Schließen")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
          color = Color(0xFFF3E8FF).copy(alpha = 0.5f),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("ID: ${arb.id} (Block #${arb.blockHeight})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
            Text("Kreditvolumen: ${String.format("%,.2f", arb.borrowedAmountEur)} €", fontSize = 10.sp, color = TextSecondary)
            Text("DEX-Route: ${arb.dexRoute}", fontSize = 10.sp, color = TextSecondary)
            Text("Bruttogewinn: +${String.format("%.2f", arb.grossProfitEur)} €", fontSize = 10.sp, color = SignalBlue)
            Text("Gas & Flash Fee: -${String.format("%.2f", arb.gasAndFlashFeeEur)} €", fontSize = 10.sp, color = TextMuted)
            Text("Netto liquidiert: +${String.format("%.2f", arb.netProfitLiquidatedEur)} €", fontSize = 11.sp, fontWeight = FontWeight.Black, color = SignalGreen)
            Divider(modifier = Modifier.padding(vertical = 4.dp))
            Text("MTK Einspeisung: +${String.format("%.2f", arb.mtkPoolInjectionEur)} € (Kurs: ${String.format("%.3f", arb.mtkPriceBeforeEur)} € ➔ ${String.format("%.3f", arb.mtkPriceAfterEur)} €)", fontSize = 10.sp, color = UrkundeGoldDark)
            Text("ZON Einspeisung: +${String.format("%.2f", arb.zonPoolInjectionEur)} € (Kurs: ${String.format("%.3f", arb.zonPriceBeforeEur)} € ➔ ${String.format("%.3f", arb.zonPriceAfterEur)} €)", fontSize = 10.sp, color = SignalGreen)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = {
            clipboardManager.setText(AnnotatedString(arb.txHash))
            Toast.makeText(context, "TxHash kopiert!", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFA855F7)),
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Transaktions-Hash kopieren", fontSize = 11.sp)
        }
      }
    }
  }
}
