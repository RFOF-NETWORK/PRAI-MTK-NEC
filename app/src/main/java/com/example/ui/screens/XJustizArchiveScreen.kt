package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class NotarUrkunde(
  val urkundenNummer: String,
  val titel: String,
  val beteiligte: String,
  val datum: String,
  val paragraf: String,
  val sha256Hash: String,
  val status: String,
  val xjustizCode: String
)

data class GrundbuchEintrag(
  val blattNummer: String,
  val gemarkung: String,
  val flurstueck: String,
  val eigentuemer: String,
  val flaeche: String,
  val lasten: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XJustizArchiveScreen(
  onBack: () -> Unit = {},
  onNavigateToWebPortal: () -> Unit = {},
  onNavigateToDex: () -> Unit = {},
  onNavigateToRAppCenter: () -> Unit = {},
  executionMode: com.example.model.ExecutionMode = com.example.model.ExecutionMode.MAIN_REAL,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current

  BackHandler(enabled = true) {
    onBack()
  }

  var selectedTab by remember { mutableIntStateOf(0) }
  val tabTitles = listOf("📜 Urkundenbuch (§36)", "🏛️ Grundbuch", "💻 XJustiz-XML", "🔒 Beweissicherung", "🔗 Verzahnung")

  var urkundenList by remember {
    mutableStateOf(
      listOf(
        NotarUrkunde(
          urkundenNummer = "URK-NR. 1892/2026",
          titel = "Souveräne Systemkonstitution & GbR Satzung",
          beteiligte = "RFOF-NETWORK Gemeinschaft & Sovereign Notariat",
          datum = "06.10.2026 · 12:00:00 UTC",
          paragraf = "§ 36 BeurkG (Vollbeweis der Verhandlung)",
          sha256Hash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
          status = "RECHTSKRÄFTIG BEGLAUBIGT",
          xjustizCode = "XJ-2026-NOTAR-001"
        ),
        NotarUrkunde(
          urkundenNummer = "URK-NR. 1893/2026",
          titel = "Eigentumsübertragung Wohnzentrum alpha-1",
          beteiligte = "Treuhandstiftung & Kreisvertreter",
          datum = "05.10.2026 · 16:45:12 UTC",
          paragraf = "§ 311b Abs. 1 BGB i.V.m. § 36 BeurkG",
          sha256Hash = "8f434346648f6b96df89dda901c5176b10a6d83961dd3c1ac88b59b2dc327aa4",
          status = "GRUNDBUCH EINGEREICHT",
          xjustizCode = "XJ-2026-GRUND-004"
        ),
        NotarUrkunde(
          urkundenNummer = "URK-NR. 1894/2026",
          titel = "Bonding-Curve Treuhand-Depotfreigabe ZON DEX",
          beteiligte = "ZON Universal DEX Engine & Treuhandregister",
          datum = "06.10.2026 · 08:30:00 UTC",
          paragraf = "§ 54a BeurkG (Treuhandauftrag)",
          sha256Hash = "a4c28f11776c59b964d8db12e5c8e9b60b2e88a09e02c5c93c44a86d8b9e6e44",
          status = "VOLLZOGEN",
          xjustizCode = "XJ-2026-TREU-012"
        )
      )
    )
  }

  val grundbuchList = remember {
    listOf(
      GrundbuchEintrag("Blatt 4812", "Sovereign Valley", "Flur 4, Flurstück 102/1", "RFOF-NETWORK Stiftung (§ 36 BeurkG)", "14,500 m²", "Keine Vorlasten (Abteilung II/III lastenfrei)"),
      GrundbuchEintrag("Blatt 4813", "Campus Autonomie", "Flur 2, Flurstück 88/3", "PRAI Autonomous Wohnzentrum", "8,200 m²", "Wegerecht für Kreisgemeinschaft eingetragen"),
      GrundbuchEintrag("Blatt 4814", "ZON Rechenzentrum", "Flur 9, Flurstück 14/2", "Kollektiv eGbR Infrastruktur", "22,000 m²", "Dienstbarkeit Leitungsrecht gesichert")
    )
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundLight)
      .testTag("xjustiz_archive_screen")
  ) {
    // -----------------------------------------------------------------
    // TOP HEADER
    // -----------------------------------------------------------------
    Surface(
      color = BlueprintNavy,
      tonalElevation = 4.dp,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
              onClick = onBack,
              modifier = Modifier
                .size(36.dp)
                .testTag("xjustiz_back_btn")
            ) {
              Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Zurück",
                tint = Color.White
              )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "XJUSTIZ NOTAR & ARCHIV",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Black,
                  color = Color.White,
                  fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                  color = Color(0xFF4F46E5),
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = "EXTENSION 2",
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 8.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                  )
                }
              }
              Text(
                text = "§ 36 BeurkG Beweissicherung · com.rfof.xjustiz.notariat",
                style = MaterialTheme.typography.bodySmall,
                color = UrkundeGold,
                fontSize = 11.sp
              )
            }
          }

          Surface(
            color = if (executionMode == com.example.model.ExecutionMode.TEST_DEMO) SignalGreen.copy(alpha = 0.2f) else UrkundeGold.copy(alpha = 0.2f),
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, if (executionMode == com.example.model.ExecutionMode.TEST_DEMO) SignalGreen else UrkundeGold)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(7.dp)
                  .background(if (executionMode == com.example.model.ExecutionMode.TEST_DEMO) SignalGreen else UrkundeGold, CircleShape)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = if (executionMode == com.example.model.ExecutionMode.TEST_DEMO) "TEST (DEMO)" else "MAIN (REAL)",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = if (executionMode == com.example.model.ExecutionMode.TEST_DEMO) SignalGreen else UrkundeGold
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Interconnection Toolbar (Verzahnungs-Buttons)
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "VERZAHNUNG:",
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = UrkundeGold
          )

          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FilledTonalButton(
              onClick = onNavigateToWebPortal,
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = SignalBlue,
                contentColor = Color.White
              ),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("xjustiz_to_primary_rapp_btn")
            ) {
              Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(11.dp))
              Spacer(modifier = Modifier.width(3.dp))
              Text("Primär-rApp (Web App)", fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            FilledTonalButton(
              onClick = onNavigateToDex,
              colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = UrkundeGoldDark,
                contentColor = Color.White
              ),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("xjustiz_to_dex_btn")
            ) {
              Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(11.dp))
              Spacer(modifier = Modifier.width(3.dp))
              Text("ZON DEX & Launchpad", fontSize = 9.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
              onClick = onNavigateToRAppCenter,
              colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
              border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
              contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.testTag("xjustiz_to_rapp_center_btn")
            ) {
              Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(11.dp))
              Spacer(modifier = Modifier.width(2.dp))
              Text("Center", fontSize = 9.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Navigation Tabs
        val tabScrollState = rememberScrollState()
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(tabScrollState),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          tabTitles.forEachIndexed { index, title ->
            val isSelected = selectedTab == index
            FilterChip(
              selected = isSelected,
              onClick = { selectedTab = index },
              label = { Text(title, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = Color(0xFF4F46E5),
                selectedLabelColor = Color.White,
                containerColor = BlueprintSurface,
                labelColor = Color(0xFFCBD5E1)
              ),
              modifier = Modifier.testTag("xjustiz_tab_$index")
            )
          }
        }
      }
    }

    // -----------------------------------------------------------------
    // TAB CONTENTS
    // -----------------------------------------------------------------
    when (selectedTab) {
      0 -> XJustizUrkundenView(
        urkunden = urkundenList,
        onInspect = { urkunde ->
          clipboardManager.setText(AnnotatedString(urkunde.sha256Hash))
          Toast.makeText(context, "Urkunden-Hash für ${urkunde.urkundenNummer} kopiert!", Toast.LENGTH_SHORT).show()
        }
      )

      1 -> XJustizGrundbuchView(
        eintraege = grundbuchList,
        onExportAuszug = { blatt ->
          Toast.makeText(context, "Amtlicher Grundbuchauszug für $blatt zertifiziert generiert!", Toast.LENGTH_SHORT).show()
        }
      )

      2 -> XJustizXmlExportView(
        urkunden = urkundenList,
        onCopyXml = { xml ->
          clipboardManager.setText(AnnotatedString(xml))
          Toast.makeText(context, "XJustiz XML 3.4 in Zwischenablage kopiert!", Toast.LENGTH_SHORT).show()
        }
      )

      3 -> XJustizProofSecurityView(
        urkunden = urkundenList
      )

      4 -> XJustizInterconnectionView(
        onNavigateToWeb = onNavigateToWebPortal,
        onNavigateToDex = onNavigateToDex,
        onNavigateToRAppCenter = onNavigateToRAppCenter
      )
    }
  }
}

// -------------------------------------------------------------
// TAB 0: URKUNDENBUCH
// -------------------------------------------------------------
@Composable
private fun XJustizUrkundenView(
  urkunden: List<NotarUrkunde>,
  onInspect: (NotarUrkunde) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, UrkundeGold)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Offizielles Urkundenbuch nach § 36 BeurkG",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = BlueprintNavy
              )
              Text(
                text = "Vollbeweis der Verhandlung, Identitätsprüfung & notarielle Zeugniskraft",
                fontSize = 10.sp,
                color = TextSecondary
              )
            }
            Box(
              modifier = Modifier
                .size(34.dp)
                .background(UrkundeGoldBg, CircleShape)
                .border(1.dp, UrkundeGold, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text("⚖️", fontSize = 16.sp)
            }
          }
        }
      }
    }

    items(urkunden) { item ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(item.urkundenNummer, fontWeight = FontWeight.Black, fontSize = 13.sp, color = UrkundeGoldDark)
              Text(item.titel, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BlueprintNavy)
            }
            Surface(color = SignalGreenLight, shape = RoundedCornerShape(4.dp)) {
              Text(
                item.status,
                color = SignalGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 9.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text("Beteiligte: ${item.beteiligte}", fontSize = 11.sp, color = TextMuted)
          Text("Rechtsgrundlage: ${item.paragraf}", fontSize = 11.sp, color = TextMuted)
          Text("Datum: ${item.datum}", fontSize = 10.sp, color = TextMuted)

          Spacer(modifier = Modifier.height(8.dp))
          Surface(
            color = BlueprintSurface,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "SHA-256: ${item.sha256Hash}",
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace,
              color = Color.White,
              modifier = Modifier.padding(6.dp)
            )
          }

          Spacer(modifier = Modifier.height(8.dp))
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            OutlinedButton(
              onClick = { onInspect(item) },
              shape = RoundedCornerShape(6.dp),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(12.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Hash kopieren", fontSize = 10.sp)
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 1: GRUNDBUCH & KATASTER
// -------------------------------------------------------------
@Composable
private fun XJustizGrundbuchView(
  eintraege: List<GrundbuchEintrag>,
  onExportAuszug: (String) -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    items(eintraege) { item ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("Grundbuchblatt: ${item.blattNummer}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = BlueprintNavy)
            Text(item.flaeche, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = SignalBlue)
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text("Gemarkung & Flurstück: ${item.gemarkung} · ${item.flurstueck}", fontSize = 11.sp, color = TextMuted)
          Text("Eigentümer: ${item.eigentuemer}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = BlueprintNavy)
          Text("Lasten/Beschränkungen: ${item.lasten}", fontSize = 11.sp, color = SignalGreen)

          Spacer(modifier = Modifier.height(10.dp))
          Button(
            onClick = { onExportAuszug(item.blattNummer) },
            colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Amtlichen Grundbuchauszug abrufen")
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 2: XJUSTIZ-XML EXPORT
// -------------------------------------------------------------
@Composable
private fun XJustizXmlExportView(
  urkunden: List<NotarUrkunde>,
  onCopyXml: (String) -> Unit
) {
  val generatedXml = remember(urkunden) {
    """
<?xml version="1.0" encoding="UTF-8"?>
<xjustiz:nachricht xmlns:xjustiz="http://www.xjustiz.de/3.4" version="3.4">
  <xjustiz:nachrichtenkopf>
    <xjustiz:erstellungszeitpunkt>2026-10-06T12:00:00Z</xjustiz:erstellungszeitpunkt>
    <xjustiz:absender>RFOF-NETWORK Notariat / PRAI MTK NEC</xjustiz:absender>
    <xjustiz:standardVersion>XJustiz 3.4 (§ 36 BeurkG)</xjustiz:standardVersion>
  </xjustiz:nachrichtenkopf>
  <xjustiz:fachdaten.notariat>
    <xjustiz:urkunde nummer="URK-NR. 1892/2026">
      <xjustiz:gegenstand>Souveräne Konstitution &amp; Satzung</xjustiz:gegenstand>
      <xjustiz:sha256>e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855</xjustiz:sha256>
      <xjustiz:status>RECHTSKRÄFTIG</xjustiz:status>
    </xjustiz:urkunde>
  </xjustiz:fachdaten.notariat>
</xjustiz:nachricht>
    """.trimIndent()
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BlueprintNavy)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text("XJustiz 3.4 XML-SCHEMA", fontWeight = FontWeight.Black, color = Color.White, fontSize = 13.sp)
            Surface(color = SignalGreenLight, shape = RoundedCornerShape(4.dp)) {
              Text("VALIDIERT", color = SignalGreen, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            color = Color.Black.copy(alpha = 0.4f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = generatedXml,
              fontFamily = FontFamily.Monospace,
              fontSize = 9.sp,
              color = UrkundeGold,
              modifier = Modifier.padding(10.dp)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))
          Button(
            onClick = { onCopyXml(generatedXml) },
            colors = ButtonDefaults.buttonColors(containerColor = UrkundeGold),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.width(6.dp))
            Text("XJustiz XML kopieren & exportieren", color = Color.Black, fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 3: BEWEISSICHERUNG
// -------------------------------------------------------------
@Composable
private fun XJustizProofSecurityView(
  urkunden: List<NotarUrkunde>
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, UrkundeGold)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Text("Kryptographische Notariats-Beweiskette", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = BlueprintNavy)
          Text(
            "Jede Urkunde wird unveränderlich mit SHA-256 Hash, RFC 3161 Zeitstempel und notarieller Signatur im System verankert.",
            fontSize = 11.sp,
            color = TextSecondary
          )
          Spacer(modifier = Modifier.height(10.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(color = SignalGreenLight, shape = RoundedCornerShape(6.dp)) {
              Text("100% UNVERFÄLSCHBAR", color = SignalGreen, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(6.dp))
            }
            Surface(color = SignalBlueLight, shape = RoundedCornerShape(6.dp)) {
              Text("§ 36 BEURKG VOLLBEWEIS", color = SignalBlue, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(6.dp))
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TAB 4: VERZAHNUNG
// -------------------------------------------------------------
@Composable
private fun XJustizInterconnectionView(
  onNavigateToWeb: () -> Unit,
  onNavigateToDex: () -> Unit,
  onNavigateToRAppCenter: () -> Unit
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.5.dp, UrkundeGold)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "🔗 SOUVERÄNE VERZAHNUNG DER 3 APPS",
            fontWeight = FontWeight.Black,
            fontSize = 14.sp,
            color = BlueprintNavy
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "XJustiz Notar & Grundbuch Archiv ist als rApp Extension 2 eng verzahnt mit der Primär-rApp 'PRAI / MTK / NEC' und der Extension 1 'ZON Universal DEX & Launchpad'.",
            fontSize = 11.sp,
            color = TextSecondary,
            lineHeight = 16.sp
          )

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = onNavigateToWeb,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.Language, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Zur Primär-rApp wechseln (Web App Browser)")
          }

          Spacer(modifier = Modifier.height(8.dp))

          Button(
            onClick = onNavigateToDex,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = UrkundeGoldDark),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.SwapHoriz, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Zur Extension 1 wechseln (ZON Universal DEX)")
          }

          Spacer(modifier = Modifier.height(8.dp))

          OutlinedButton(
            onClick = onNavigateToRAppCenter,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.Storefront, contentDescription = null)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Zurück zum rApp Center (PlayStore)")
          }
        }
      }
    }
  }
}
