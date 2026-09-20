package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.auth.AuthManager
import com.example.crypto.AESEncryption
import com.example.data.ExplorerRepository
import com.example.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockchainExplorerScreen(
  onBack: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val certificates by ExplorerRepository.certificates.collectAsState()
  val recentBlocks by ExplorerRepository.recentBlocks.collectAsState()
  val transferredCopies by ExplorerRepository.transferredCopies.collectAsState()
  val currentUser by AuthManager.currentUser.collectAsState()

  var selectedTab by remember { mutableStateOf(0) }
  val tabs = listOf("NEC-Urkunden & Zertifikate", "Blockchain-Blöcke", "Transaktionen & Kopien")

  // Transfer Dialog State
  var activeCertForTransfer by remember { mutableStateOf<NecCertificate?>(null) }
  var isTransferringCopyOnly by remember { mutableStateOf(true) }

  // Inspect Certificate Dialog
  var activeCertForInspection by remember { mutableStateOf<NecCertificate?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundLight)
      .testTag("blockchain_explorer_screen")
  ) {
    // Header Banner
    Surface(
      color = BlueprintNavy,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 14.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(34.dp)
                .background(UrkundeGoldBg, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Explore, contentDescription = null, tint = UrkundeGoldDark, modifier = Modifier.size(20.dp))
            }
            Column {
              Text(
                text = "BLOCKCHAIN & NEC EXPLORER",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Black
              )
              Text(
                text = "MTK Sovereign Ledger • Dual-Existenz mit XJustiz",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFCBD5E1),
                fontSize = 11.sp
              )
            }
          }

          Surface(
            color = Color(0xFF1E293B),
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = "BLOCK #4.102.918",
              color = SignalGreen,
              fontWeight = FontWeight.Bold,
              fontSize = 10.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "NEC ist keine Währung, sondern ein nicht-extrahierbares Zertifikat und NFT (Urkunde), das parallel auf der Blockchain und in der notariellen Datenbank synchron koexistiert.",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFFE2E8F0),
          fontSize = 11.sp,
          lineHeight = 15.sp
        )
      }
    }

    // Tab Navigation
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = Color.White,
      contentColor = BlueprintNavy
    ) {
      tabs.forEachIndexed { index, title ->
        Tab(
          selected = selectedTab == index,
          onClick = { selectedTab = index },
          text = {
            Text(
              text = title,
              fontSize = 11.sp,
              fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
            )
          }
        )
      }
    }

    when (selectedTab) {
      0 -> NecCertificatesTab(
        certificates = certificates,
        onInspect = { activeCertForInspection = it },
        onTransferCopy = { cert ->
          activeCertForTransfer = cert
          isTransferringCopyOnly = true
        },
        onTransferOriginal = { cert ->
          activeCertForTransfer = cert
          isTransferringCopyOnly = false
        },
        onDownload = { cert ->
          val hash = AESEncryption.signDocumentSeal(cert.id, cert.notarialSealHash, currentUser.walletAddress)
          clipboardManager.setText(AnnotatedString("DOKUMENT-SIEGEL: $hash\nID: ${cert.id}\nTITEL: ${cert.title}"))
          Toast.makeText(context, "Dokument ${cert.id} mit AES-Siegel verifiziert & gesichert!", Toast.LENGTH_LONG).show()
        },
        onShareEmail = { cert ->
          val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Beglaubigte Abschrift: ${cert.title} (${cert.id})")
            putExtra(
              Intent.EXTRA_TEXT,
              "BEGLAUBIGTE ABSCHRIFT NACH § 36 BeurkG\n\n" +
                "Zertifikats-ID: ${cert.id}\n" +
                "Titel: ${cert.title}\n" +
                "Fachgebiet: Kategorie #${cert.categoryId} (${cert.categoryName})\n" +
                "MTK NFT Token-ID: ${cert.nftTokenId}\n" +
                "XJustiz DB Record: ${cert.databaseRecordId}\n" +
                "Notariats-Siegel-Hash: ${cert.notarialSealHash}\n" +
                "Aussteller: ${cert.issuer}\n" +
                "Inhaber: ${currentUser.username} (${currentUser.walletAddress})\n\n" +
                "Diese Urkunde koexistiert parallel als unveränderliches NFT auf der Montalkanio Blockchain und in der Justiz-Datenbank."
            )
          }
          context.startActivity(Intent.createChooser(shareIntent, "Urkunde per E-Mail versenden"))
        }
      )
      1 -> BlocksTab(blocks = recentBlocks)
      2 -> TransactionsTab(transfers = transferredCopies)
    }
  }

  // --- MODALS ---

  // Inspection Dialog
  activeCertForInspection?.let { cert ->
    CertificateDetailDialog(
      cert = cert,
      onDismiss = { activeCertForInspection = null }
    )
  }

  // Transfer Dialog (Copy or Original)
  activeCertForTransfer?.let { cert ->
    TransferDocumentDialog(
      cert = cert,
      isCopyOnly = isTransferringCopyOnly,
      onDismiss = { activeCertForTransfer = null },
      onConfirmTransfer = { recipient ->
        if (isTransferringCopyOnly) {
          val sealHash = ExplorerRepository.transferDocumentCopy(cert.id, recipient)
          Toast.makeText(context, "Beglaubigte Abschrift an $recipient übermittelt! Siegel: ${sealHash.take(12)}...", Toast.LENGTH_LONG).show()
        } else {
          val success = ExplorerRepository.transferDocumentOriginal(cert.id, recipient)
          if (success) {
            Toast.makeText(context, "Original-Urkunde an $recipient übertragen!", Toast.LENGTH_SHORT).show()
          } else {
            Toast.makeText(context, "Übertragung nicht gestattet!", Toast.LENGTH_SHORT).show()
          }
        }
        activeCertForTransfer = null
      }
    )
  }
}

// -------------------------------------------------------------
// NEC CERTIFICATES TAB (GAMIFICATION & DUAL SINGULARITY)
// -------------------------------------------------------------
@Composable
private fun NecCertificatesTab(
  certificates: List<NecCertificate>,
  onInspect: (NecCertificate) -> Unit,
  onTransferCopy: (NecCertificate) -> Unit,
  onTransferOriginal: (NecCertificate) -> Unit,
  onDownload: (NecCertificate) -> Unit,
  onShareEmail: (NecCertificate) -> Unit
) {
  var filterOnlyUnlocked by remember { mutableStateOf(false) }

  val filtered = remember(certificates, filterOnlyUnlocked) {
    if (filterOnlyUnlocked) {
      certificates.filter { ExplorerRepository.isCertificateUnlocked(it) }
    } else {
      certificates
    }
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "DOKUMENTEN- & URKUNDEN-REGISTRY",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Text(
            text = "Freischaltung basiert auf Gamifizierung & Aktivitäten",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted,
            fontSize = 10.sp
          )
        }

        FilterChip(
          selected = filterOnlyUnlocked,
          onClick = { filterOnlyUnlocked = !filterOnlyUnlocked },
          label = { Text("Nur erlangte Dokumente", fontSize = 10.sp) }
        )
      }
    }

    items(filtered) { cert ->
      val isUnlocked = ExplorerRepository.isCertificateUnlocked(cert)

      NecCertificateCard(
        cert = cert,
        isUnlocked = isUnlocked,
        onInspect = { onInspect(cert) },
        onTransferCopy = { onTransferCopy(cert) },
        onTransferOriginal = { onTransferOriginal(cert) },
        onDownload = { onDownload(cert) },
        onShareEmail = { onShareEmail(cert) }
      )
    }
  }
}

@Composable
private fun NecCertificateCard(
  cert: NecCertificate,
  isUnlocked: Boolean,
  onInspect: () -> Unit,
  onTransferCopy: () -> Unit,
  onTransferOriginal: () -> Unit,
  onDownload: () -> Unit,
  onShareEmail: () -> Unit
) {
  val borderColor = if (isUnlocked) UrkundeGold else BlueprintBorder
  val cardBg = if (isUnlocked) Color.White else Slate50

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("nec_cert_card_${cert.id}"),
    colors = CardDefaults.cardColors(containerColor = cardBg),
    border = BorderStroke(1.dp, borderColor),
    shape = RoundedCornerShape(10.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Top Row: ID, Policy Badge & Status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Surface(
            color = BlueprintNavy,
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = cert.id,
              color = Color.White,
              fontWeight = FontWeight.Black,
              fontSize = 10.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          Surface(
            color = UrkundeGoldBg,
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = "NFT & URKUNDE",
              color = UrkundeGoldDark,
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp,
              modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
            )
          }
        }

        Surface(
          color = if (isUnlocked) SignalGreenLight else Color(0xFFF1F5F9),
          shape = RoundedCornerShape(4.dp)
        ) {
          Text(
            text = if (isUnlocked) "ERLANGT / FREIGESCHALTET" else "GESPERRT (Gamifizierung)",
            color = if (isUnlocked) SignalGreen else TextMuted,
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = cert.title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )

      Text(
        text = "Kategorie #${cert.categoryId}: ${cert.categoryName}",
        style = MaterialTheme.typography.bodySmall,
        color = SignalBlue,
        fontSize = 11.sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      // Dual Coexistence Badges
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color(0xFFF8FAFC), RoundedCornerShape(6.dp))
          .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text("MTK NFT Token", fontSize = 9.sp, color = TextMuted)
          Text(cert.nftTokenId.take(18) + "...", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
        }
        Column(horizontalAlignment = Alignment.End) {
          Text("XJustiz DB Record", fontSize = 9.sp, color = TextMuted)
          Text(cert.databaseRecordId, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = UrkundeGoldDark)
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "Transfer-Regel: ${cert.transferPolicy.label}",
        style = MaterialTheme.typography.labelSmall,
        color = TextSecondary,
        fontSize = 10.sp
      )

      if (!isUnlocked) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Voraussetzung: ${cert.unlockRequirement}",
          style = MaterialTheme.typography.bodySmall,
          color = UrkundeWax,
          fontSize = 10.sp
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Action Buttons Row (Only enabled if unlocked)
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedButton(
          onClick = onInspect,
          shape = RoundedCornerShape(6.dp),
          contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Details", fontSize = 10.sp)
        }

        if (isUnlocked) {
          Button(
            onClick = onDownload,
            colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Download", fontSize = 10.sp)
          }

          // Transfer verified copy button
          Button(
            onClick = onTransferCopy,
            colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.FileCopy, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Kopie senden", fontSize = 10.sp)
          }

          IconButton(
            onClick = onShareEmail,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(Icons.Default.Email, contentDescription = "E-Mail", tint = SignalBlue, modifier = Modifier.size(18.dp))
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// BLOCKS TAB
// -------------------------------------------------------------
@Composable
private fun BlocksTab(blocks: List<BlockchainBlock>) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text(
        text = "ECHTZEIT-BLÖCKE ÜBER ALLE NETZWERKE",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
    }

    items(blocks) { block ->
      Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BlueprintBorder),
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
                  .background(block.chain.color.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(block.chain.symbol.take(2), fontSize = 9.sp, fontWeight = FontWeight.Black, color = block.chain.color)
              }
              Text(
                text = "Block #${block.height}",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = BlueprintNavy
              )
            }

            Text(
              text = "${block.txCount} Transaktionen",
              fontSize = 11.sp,
              color = SignalBlue,
              fontWeight = FontWeight.Bold
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "Hash: ${block.hash}",
            fontSize = 10.sp,
            color = TextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = "Extension License: ${block.extensionLicenseHash.take(18)}... (PRAI / MTK / NEC)",
            fontSize = 9.sp,
            color = UrkundeGoldDark,
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "Validierer / Pool: ${block.validatorOrMiner}",
            fontSize = 10.sp,
            color = TextSecondary
          )
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TRANSACTIONS & COPIES TAB
// -------------------------------------------------------------
@Composable
private fun TransactionsTab(transfers: List<ExplorerRepository.CopyTransferRecord>) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    item {
      Text(
        text = "TRANSFERIERTE BEGLAUBIGTE KOPIEN & NACHWEISE",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
    }

    if (transfers.isEmpty()) {
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, BlueprintBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "Bisher wurden noch keine Kopien oder Nachweise transferiert. Wählen Sie im Reiter 'NEC-Urkunden' ein freigeschaltetes Dokument und tippen Sie auf 'Kopie senden'.",
            fontSize = 11.sp,
            color = TextMuted,
            modifier = Modifier.padding(14.dp)
          )
        }
      }
    } else {
      items(transfers) { record ->
        Card(
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, BlueprintBorder),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "${record.certId} ${if (record.isCopyOnly) "(Beglaubigte Kopie)" else "(Original-Transfer)"}",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = BlueprintNavy
              )
              Text(
                text = "Status: Verifiziert",
                color = SignalGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Empfänger: ${record.recipientEmailOrAddress}", fontSize = 11.sp, color = TextSecondary)
            Text(
              text = "Siegel-Hash: ${record.sealVerificationHash}",
              fontSize = 9.sp,
              color = TextMuted,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// CERTIFICATE DETAIL INSPECTION DIALOG
// -------------------------------------------------------------
@Composable
private fun CertificateDetailDialog(
  cert: NecCertificate,
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
      Column(modifier = Modifier.padding(18.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = cert.id,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Black,
              color = UrkundeGold
            )
            Text(
              text = cert.title,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Black,
              color = BlueprintNavy
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Schließen")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = cert.description,
          style = MaterialTheme.typography.bodySmall,
          color = TextSecondary,
          fontSize = 11.sp,
          lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
          color = Slate50,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("PARALLELE CO-EXISTENZ (SINGULARITÄT):", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
            Text("• MTK Blockchain NFT: ${cert.nftTokenId}", fontSize = 10.sp, color = SignalBlue)
            Text("• Justizdatenbank XJustiz: ${cert.databaseRecordId}", fontSize = 10.sp, color = UrkundeGoldDark)
            Text("• Notariatssiegel-Hash: ${cert.notarialSealHash.take(24)}...", fontSize = 10.sp, color = TextMuted)
            Text("• Aussteller: ${cert.issuer}", fontSize = 10.sp, color = TextSecondary)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = {
            clipboardManager.setText(AnnotatedString(cert.notarialSealHash))
            Toast.makeText(context, "Notar-Siegel kopiert", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Vollständigen Siegel-Hash kopieren", fontSize = 11.sp)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TRANSFER DOCUMENT DIALOG
// -------------------------------------------------------------
@Composable
private fun TransferDocumentDialog(
  cert: NecCertificate,
  isCopyOnly: Boolean,
  onDismiss: () -> Unit,
  onConfirmTransfer: (String) -> Unit
) {
  var recipientInput by remember { mutableStateOf("") }

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Text(
          text = if (isCopyOnly) "BEGLAUBIGTE KOPIE ÜBERTRAGEN" else "ORIGINAL-URKUNDE ÜBERTRAGEN",
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Black,
          color = BlueprintNavy
        )
        Text(
          text = cert.title,
          style = MaterialTheme.typography.bodySmall,
          color = SignalBlue,
          fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
          color = if (isCopyOnly) SignalBlueLight else UrkundeGoldBg,
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = if (isCopyOnly) {
              "Das Original-NFT verbleibt in Ihrem Besitz. Der Empfänger erhält eine kryptographisch beglaubigte Abschrift mit individuellem Zeitstempel und Hash als rechtsgültigen Nachweis."
            } else {
              "Achtung: Die Original-Urkunde wird vollständig auf die Empfänger-Adresse übertragen. Sie verlieren die Eigentumsrechte an diesem Dokument."
            },
            color = if (isCopyOnly) SignalBlue else UrkundeGoldDark,
            fontSize = 10.sp,
            lineHeight = 14.sp,
            modifier = Modifier.padding(8.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = recipientInput,
          onValueChange = { recipientInput = it },
          label = { Text("Empfänger (E-Mail oder Wallet-Adresse)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          TextButton(onClick = onDismiss) {
            Text("Abbrechen")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (recipientInput.isNotBlank()) {
                onConfirmTransfer(recipientInput)
              }
            },
            colors = ButtonDefaults.buttonColors(containerColor = if (isCopyOnly) SignalBlue else UrkundeWax)
          ) {
            Text(if (isCopyOnly) "Abschrift senden" else "Original übertragen")
          }
        }
      }
    }
  }
}
