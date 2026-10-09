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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.auth.AuthManager
import com.example.auth.UserRole
import com.example.crypto.AESEncryption
import com.example.data.ExplorerRepository
import com.example.data.WalletRepository
import com.example.model.*
import com.example.ui.components.AdminSovereignExplorerView
import com.example.ui.components.KiAutonomyExplorerView
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

enum class ExplorerPerspective(val title: String, val subtitle: String, val iconEmoji: String) {
  USER("Nutzer-Sicht", "Eigene Transaktionen & Globaler Ledger", "👤"),
  KI("KI-Sicht (Autonom)", "Flash Loans, Mining & Gebühren-Liquidator", "🤖"),
  ADMIN("Admin-Sicht", "Sovereign Master, Genesis & Escrow Treasury", "👑")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlockchainExplorerScreen(
  onBack: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val currentUser by AuthManager.currentUser.collectAsState()
  val certificates by ExplorerRepository.certificates.collectAsState()
  val recentBlocks by ExplorerRepository.recentBlocks.collectAsState()
  val transferredCopies by ExplorerRepository.transferredCopies.collectAsState()
  val globalTransactions by ExplorerRepository.globalTransactions.collectAsState()
  val walletTransactions by WalletRepository.transactions.collectAsState()

  var selectedPerspective by remember { mutableStateOf(ExplorerPerspective.USER) }
  var showAdminAccessRestrictedDialog by remember { mutableStateOf(false) }

  // User perspective sub-tabs
  var selectedUserTab by remember { mutableStateOf(0) }
  val userTabs = listOf("Profil-Transaktionen", "Globaler Ledger (Blöcke & Tx)", "NEC-Urkunden")

  // Modals
  var selectedBlockForDetails by remember { mutableStateOf<BlockchainBlock?>(null) }
  var selectedTxForDetails by remember { mutableStateOf<WalletTransaction?>(null) }
  var activeCertForTransfer by remember { mutableStateOf<NecCertificate?>(null) }
  var isTransferringCopyOnly by remember { mutableStateOf(true) }
  var activeCertForInspection by remember { mutableStateOf<NecCertificate?>(null) }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(BackgroundLight)
      .testTag("blockchain_explorer_screen")
  ) {
    // -------------------------------------------------------------
    // TOP HEADER BANNER: 3-PERSPEKTIVEN SYSTEM MATRIX
    // -------------------------------------------------------------
    Surface(
      color = BlueprintNavy,
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 12.dp)
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
                text = "BLOCKCHAIN & SYSTEM EXPLORER",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Black
              )
              Text(
                text = "3-Säulen-Matrix: Nutzer • KI • Admin (§ 36 BeurkG)",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFCBD5E1),
                fontSize = 10.sp
              )
            }
          }

          Surface(
            color = if (AuthManager.isAdmin) UrkundeGoldDark else SignalBlue,
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = if (AuthManager.isAdmin) "ADMIN-SOUVERÄN" else "NUTZER-MODUS",
              color = Color.White,
              fontWeight = FontWeight.Bold,
              fontSize = 9.sp,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // 3-Segment Perspective Selector
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E293B), RoundedCornerShape(8.dp))
            .padding(3.dp),
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          ExplorerPerspective.values().forEach { perspective ->
            val isSelected = selectedPerspective == perspective
            val isRestricted = perspective == ExplorerPerspective.ADMIN && !AuthManager.isAdmin

            Surface(
              color = when {
                isSelected && perspective == ExplorerPerspective.ADMIN -> UrkundeGoldDark
                isSelected && perspective == ExplorerPerspective.KI -> Color(0xFFA855F7)
                isSelected -> SignalBlue
                else -> Color.Transparent
              },
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier
                .weight(1f)
                .clickable {
                  if (isRestricted) {
                    showAdminAccessRestrictedDialog = true
                  } else {
                    selectedPerspective = perspective
                  }
                }
                .testTag("perspective_btn_${perspective.name.lowercase()}")
            ) {
              Row(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(perspective.iconEmoji, fontSize = 11.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = when (perspective) {
                    ExplorerPerspective.USER -> "Nutzer"
                    ExplorerPerspective.KI -> "KI (Autonom)"
                    ExplorerPerspective.ADMIN -> "Admin"
                  },
                  fontSize = 11.sp,
                  fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                  color = if (isSelected) Color.White else if (isRestricted) Color(0xFF94A3B8) else Color(0xFFE2E8F0)
                )
                if (isRestricted) {
                  Spacer(modifier = Modifier.width(3.dp))
                  Icon(
                    Icons.Default.Lock,
                    contentDescription = "Gesperrt",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(11.dp)
                  )
                }
              }
            }
          }
        }
      }
    }

    // -------------------------------------------------------------
    // PERSPECTIVE CONTENT ROUTING
    // -------------------------------------------------------------
    when (selectedPerspective) {
      ExplorerPerspective.USER -> {
        // User Perspective with sub-tabs
        TabRow(
          selectedTabIndex = selectedUserTab,
          containerColor = Color.White,
          contentColor = BlueprintNavy
        ) {
          userTabs.forEachIndexed { index, title ->
            Tab(
              selected = selectedUserTab == index,
              onClick = { selectedUserTab = index },
              text = {
                Text(
                  text = title,
                  fontSize = 11.sp,
                  fontWeight = if (selectedUserTab == index) FontWeight.Bold else FontWeight.Normal,
                  maxLines = 1
                )
              }
            )
          }
        }

        when (selectedUserTab) {
          0 -> UserProfileTransactionsTab(
            walletTransactions = walletTransactions,
            transferredCopies = transferredCopies,
            currentUser = currentUser,
            onInspectTx = { selectedTxForDetails = it }
          )
          1 -> GlobalLedgerTab(
            blocks = recentBlocks,
            transactions = globalTransactions,
            onInspectBlock = { selectedBlockForDetails = it },
            onInspectTx = { selectedTxForDetails = it }
          )
          2 -> NecCertificatesTab(
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
        }
      }

      ExplorerPerspective.KI -> {
        // KI Perspective (Always viewable by both Users and Admin!)
        KiAutonomyExplorerView()
      }

      ExplorerPerspective.ADMIN -> {
        // Admin Sovereign Master View (Accessible only to RFOF-NETWORK Admin or KI)
        AdminSovereignExplorerView()
      }
    }
  }

  // -------------------------------------------------------------
  // DIALOGS & MODALS
  // -------------------------------------------------------------

  // Admin Access Restricted Dialog
  if (showAdminAccessRestrictedDialog) {
    Dialog(onDismissRequest = { showAdminAccessRestrictedDialog = false }) {
      Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        modifier = Modifier.fillMaxWidth().padding(16.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = UrkundeGoldDark, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "ZUGRIFFSBESCHRÄNKUNG",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Black,
              color = BlueprintNavy
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Gemäß § 36 BeurkG, XJustiz-Clearing und der System-Matrix ist die Admin-Sicht ausschließlich für das Administratorenkonto 'RFOF-NETWORK' reserviert.",
            fontSize = 11.sp,
            color = TextSecondary,
            lineHeight = 15.sp
          )

          Spacer(modifier = Modifier.height(6.dp))
          Surface(
            color = Slate50,
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(8.dp)) {
              Text("Aktueller Nutzer: ${currentUser.username}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
              Text("Rolle: ${currentUser.role.displayName}", fontSize = 10.sp, color = TextMuted)
              Text("Verfügbare Sichten: 👤 Nutzer-Sicht & 🤖 KI-Sicht", fontSize = 10.sp, color = SignalBlue)
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Button(
              onClick = { showAdminAccessRestrictedDialog = false },
              colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy)
            ) {
              Text("Schließen (Login über Kopfzeile)", fontSize = 11.sp)
            }
          }
        }
      }
    }
  }

  // Block Details Dialog
  selectedBlockForDetails?.let { block ->
    BlockDetailsDialog(
      block = block,
      onDismiss = { selectedBlockForDetails = null }
    )
  }

  // Transaction Details Dialog
  selectedTxForDetails?.let { tx ->
    TransactionDetailsDialog(
      tx = tx,
      onDismiss = { selectedTxForDetails = null }
    )
  }

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
// USER PROFILE TRANSACTIONS TAB (EIGENE TRANSAKTIONEN)
// -------------------------------------------------------------
@Composable
private fun UserProfileTransactionsTab(
  walletTransactions: List<WalletTransaction>,
  transferredCopies: List<ExplorerRepository.CopyTransferRecord>,
  currentUser: com.example.auth.UserProfile,
  onInspectTx: (WalletTransaction) -> Unit
) {
  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    // Identity Card
    item {
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
            Column {
              Text(
                text = "PROFIL: ${currentUser.username}",
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                color = BlueprintNavy
              )
              Text(
                text = "Organisation: ${currentUser.organization} • Typ: ${currentUser.userType.label}",
                fontSize = 10.sp,
                color = TextSecondary
              )
            }
            Surface(
              color = SignalBlueLight,
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = currentUser.role.badge,
                color = SignalBlue,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(Slate50, RoundedCornerShape(6.dp))
              .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("Eigene Wallet-Adresse", fontSize = 9.sp, color = TextMuted)
              Text(
                text = currentUser.walletAddress,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = BlueprintNavy,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
            IconButton(
              onClick = {
                clipboardManager.setText(AnnotatedString(currentUser.walletAddress))
                Toast.makeText(context, "Adresse kopiert!", Toast.LENGTH_SHORT).show()
              },
              modifier = Modifier.size(26.dp)
            ) {
              Icon(Icons.Default.ContentCopy, contentDescription = "Kopieren", modifier = Modifier.size(15.dp))
            }
          }
        }
      }
    }

    item {
      Text(
        text = "EIGENE TRANSAKTIONSHISTORIE & ABSCHRIFTEN",
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = BlueprintNavy
      )
    }

    if (walletTransactions.isEmpty() && transferredCopies.isEmpty()) {
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, BlueprintBorder),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "Keine Transaktionen auf diesem Konto verzeichnet.",
            fontSize = 11.sp,
            color = TextMuted,
            modifier = Modifier.padding(14.dp)
          )
        }
      }
    } else {
      items(walletTransactions) { tx ->
        Card(
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, BlueprintBorder),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onInspectTx(tx) }
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                  modifier = Modifier
                    .size(24.dp)
                    .background(tx.chain.color.copy(alpha = 0.15f), CircleShape),
                  contentAlignment = Alignment.Center
                ) {
                  Text(tx.chain.symbol.take(2), fontSize = 9.sp, fontWeight = FontWeight.Bold, color = tx.chain.color)
                }
                Text(
                  text = tx.type.label,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = BlueprintNavy
                )
              }

              Text(
                text = "${tx.amount} ${tx.chain.symbol}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = if (tx.type == TxType.DEPOSIT) SignalGreen else BlueprintNavy
              )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "TxHash: ${tx.txHash}", fontSize = 9.sp, color = TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis, fontFamily = FontFamily.Monospace)
            if (tx.note.isNotBlank()) {
              Text(text = tx.note, fontSize = 10.sp, color = TextSecondary)
            }
          }
        }
      }

      // Transferred copies
      items(transferredCopies) { record ->
        Card(
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = BorderStroke(1.dp, UrkundeGoldDark.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "Abschrift: ${record.certId}",
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = UrkundeGoldDark
              )
              Text("Beglaubigt (§ 36 BeurkG)", fontSize = 9.sp, color = SignalGreen, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text("Empfänger: ${record.recipientEmailOrAddress}", fontSize = 10.sp, color = TextSecondary)
            Text("Siegel-Hash: ${record.sealVerificationHash}", fontSize = 9.sp, color = TextMuted, maxLines = 1, overflow = TextOverflow.Ellipsis, fontFamily = FontFamily.Monospace)
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// GLOBAL LEDGER TAB (ALLE BLÖCKE & ALLE TRANSAKTIONEN)
// -------------------------------------------------------------
@Composable
private fun GlobalLedgerTab(
  blocks: List<BlockchainBlock>,
  transactions: List<WalletTransaction>,
  onInspectBlock: (BlockchainBlock) -> Unit,
  onInspectTx: (WalletTransaction) -> Unit
) {
  var selectedSubMode by remember { mutableStateOf(0) }
  val modes = listOf("Global Blöcke (${blocks.size})", "Global Transaktionen (${transactions.size})")

  Column(modifier = Modifier.fillMaxSize()) {
    // Mode Switcher
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      modes.forEachIndexed { idx, label ->
        FilterChip(
          selected = selectedSubMode == idx,
          onClick = { selectedSubMode = idx },
          label = { Text(label, fontSize = 10.sp) }
        )
      }
    }

    if (selectedSubMode == 0) {
      // Blocks List
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(blocks) { block ->
          Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BlueprintBorder),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onInspectBlock(block) }
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
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
                  text = "${block.txCount} Tx • ${String.format("%.1f", block.sizeKb)} KB",
                  fontSize = 10.sp,
                  color = SignalBlue,
                  fontWeight = FontWeight.Bold
                )
              }

              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Hash: ${block.hash}",
                fontSize = 9.sp,
                color = TextMuted,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "Miner / Validator: ${block.validatorOrMiner}",
                fontSize = 10.sp,
                color = TextSecondary
              )
              Text(
                text = "Liquidiert: ${block.reward}",
                fontSize = 9.sp,
                color = SignalGreen,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    } else {
      // Transactions List
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(transactions) { tx ->
          Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, BlueprintBorder),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onInspectTx(tx) }
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = tx.id,
                  fontWeight = FontWeight.Bold,
                  fontSize = 11.sp,
                  color = BlueprintNavy
                )
                Text(
                  text = "${tx.amount} ${tx.chain.symbol}",
                  fontWeight = FontWeight.Black,
                  fontSize = 12.sp,
                  color = SignalBlue
                )
              }
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "TxHash: ${tx.txHash}",
                fontSize = 9.sp,
                color = TextMuted,
                fontFamily = FontFamily.Monospace,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
              Text(
                text = "${tx.fromAddress.take(16)}... ➔ ${tx.toAddress.take(16)}...",
                fontSize = 9.sp,
                color = TextSecondary,
                fontFamily = FontFamily.Monospace
              )
              if (tx.note.isNotBlank()) {
                Text(text = tx.note, fontSize = 9.sp, color = UrkundeGoldDark)
              }
            }
          }
        }
      }
    }
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
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "PARALLELE SINGULARITÄT: 28 FACHKATEGORIEN",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = BlueprintNavy
          )
          Text(
            text = "Echtzeit-Abgleich: Blockchain-NFT & XJustiz-Datenbank",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            fontSize = 10.sp
          )
        }

        FilterChip(
          selected = filterOnlyUnlocked,
          onClick = { filterOnlyUnlocked = !filterOnlyUnlocked },
          label = { Text("Nur Freigeschaltete", fontSize = 10.sp) }
        )
      }
    }

    items(filtered) { cert ->
      val isUnlocked = ExplorerRepository.isCertificateUnlocked(cert)

      Card(
        colors = CardDefaults.cardColors(
          containerColor = if (isUnlocked) Color.White else Color(0xFFF1F5F9)
        ),
        border = BorderStroke(
          width = 1.dp,
          color = if (isUnlocked) UrkundeGoldDark.copy(alpha = 0.5f) else BlueprintBorder
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Surface(
                  color = if (isUnlocked) UrkundeGoldBg else Slate50,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = cert.id,
                    color = if (isUnlocked) UrkundeGoldDark else TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }

                Text(
                  text = "Kat. #${cert.categoryId}",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = BlueprintNavy
                )
              }

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = cert.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isUnlocked) BlueprintNavy else TextMuted
              )
            }

            Surface(
              color = if (isUnlocked) SignalGreenLight else Color(0xFFE2E8F0),
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                text = if (isUnlocked) "Freigeschaltet" else "Gesperrt",
                color = if (isUnlocked) SignalGreen else TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Dual Identifiers (NFT + Database)
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
              onClick = { onInspect(cert) },
              shape = RoundedCornerShape(6.dp),
              contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
            ) {
              Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Details", fontSize = 10.sp)
            }

            if (isUnlocked) {
              Button(
                onClick = { onDownload(cert) },
                colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Download", fontSize = 10.sp)
              }

              Button(
                onClick = { onTransferCopy(cert) },
                colors = ButtonDefaults.buttonColors(containerColor = SignalBlue),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
              ) {
                Icon(Icons.Default.FileCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Kopie senden", fontSize = 10.sp)
              }

              IconButton(
                onClick = { onShareEmail(cert) },
                modifier = Modifier.size(32.dp)
              ) {
                Icon(Icons.Default.Email, contentDescription = "E-Mail", tint = SignalBlue, modifier = Modifier.size(18.dp))
              }
            }
          }
        }
      }
    }
  }
}

// -------------------------------------------------------------
// BLOCK DETAILS DIALOG
// -------------------------------------------------------------
@Composable
private fun BlockDetailsDialog(
  block: BlockchainBlock,
  onDismiss: () -> Unit
) {
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
              modifier = Modifier.size(24.dp).background(block.chain.color.copy(alpha = 0.2f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(block.chain.symbol.take(2), fontSize = 9.sp, fontWeight = FontWeight.Black, color = block.chain.color)
            }
            Text(
              text = "BLOCK #${block.height}",
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

        Surface(
          color = Slate50,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("Blockchain: ${block.chain.fullName}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
            Text("Hash: ${block.hash}", fontSize = 9.sp, color = SignalBlue, fontFamily = FontFamily.Monospace)
            Text("Transaktionen: ${block.txCount}", fontSize = 10.sp, color = TextSecondary)
            Text("Block-Größe: ${String.format("%.2f", block.sizeKb)} KB", fontSize = 10.sp, color = TextSecondary)
            Text("Validierer / Miner: ${block.validatorOrMiner}", fontSize = 10.sp, color = TextSecondary)
            Text("Gebühren-Liquidierung / Reward: ${block.reward}", fontSize = 10.sp, color = SignalGreen, fontWeight = FontWeight.Bold)
            Text("Sovereign License: ${block.extensionLicenseHash.take(24)}... (PRAI / MTK / NEC)", fontSize = 9.sp, color = UrkundeGoldDark)
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = {
            clipboardManager.setText(AnnotatedString(block.hash))
            Toast.makeText(context, "Block-Hash kopiert!", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Block-Hash kopieren", fontSize = 11.sp)
        }
      }
    }
  }
}

// -------------------------------------------------------------
// TRANSACTION DETAILS DIALOG
// -------------------------------------------------------------
@Composable
private fun TransactionDetailsDialog(
  tx: WalletTransaction,
  onDismiss: () -> Unit
) {
  val clipboardManager = LocalClipboardManager.current
  val context = LocalContext.current

  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(14.dp),
      color = Color.White,
      modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "TRANSAKTIONS-DETAILS",
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
          Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("Tx ID: ${tx.id}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlueprintNavy)
            Text("Netzwerk: ${tx.chain.fullName}", fontSize = 10.sp, color = TextSecondary)
            Text("Typ: ${tx.type.label}", fontSize = 10.sp, color = SignalBlue, fontWeight = FontWeight.Bold)
            Text("Betrag: ${tx.amount} ${tx.chain.symbol}", fontSize = 11.sp, fontWeight = FontWeight.Black, color = SignalGreen)
            Text("TxHash: ${tx.txHash}", fontSize = 9.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
            Text("Von: ${tx.fromAddress}", fontSize = 9.sp, color = TextSecondary, fontFamily = FontFamily.Monospace)
            Text("An: ${tx.toAddress}", fontSize = 9.sp, color = TextSecondary, fontFamily = FontFamily.Monospace)
            Text("Gebühr: ${tx.fee} ${tx.chain.symbol}", fontSize = 9.sp, color = TextMuted)
            Text("Status: Bestätigt (Sovereign Consensus)", fontSize = 10.sp, color = SignalGreen, fontWeight = FontWeight.Bold)
            if (tx.note.isNotBlank()) {
              Text("Vermerk: ${tx.note}", fontSize = 10.sp, color = UrkundeGoldDark)
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
          onClick = {
            clipboardManager.setText(AnnotatedString(tx.txHash))
            Toast.makeText(context, "Transaktions-Hash kopiert!", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = BlueprintNavy),
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("TxHash kopieren", fontSize = 11.sp)
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
      modifier = Modifier.fillMaxWidth().padding(16.dp)
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
      modifier = Modifier.fillMaxWidth().padding(16.dp)
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
