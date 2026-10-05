/**
 * Alchemy Ethereum Mainnet Node RPC Demo
 * 
 * Works out-of-the-box with Node.js 18+ (uses native fetch)
 * Automatically loads ALCHEMY_API_KEY from .env
 */
const fs = require('fs');
const path = require('path');

// 1. Load .env file (supports both dotenv if installed, or built-in parser)
try {
  require('dotenv').config();
} catch (e) {
  // Fallback: built-in lightweight .env parser
  const envPath = path.resolve(process.cwd(), '.env');
  if (fs.existsSync(envPath)) {
    const lines = fs.readFileSync(envPath, 'utf8').split('\n');
    for (const line of lines) {
      const trimmed = line.trim();
      if (!trimmed || trimmed.startsWith('#')) continue;
      const eqIdx = trimmed.indexOf('=');
      if (eqIdx > 0) {
        const key = trimmed.slice(0, eqIdx).trim();
        const val = trimmed.slice(eqIdx + 1).trim().replace(/^["']|["']$/g, '');
        if (!process.env[key]) {
          process.env[key] = val;
        }
      }
    }
  }
}

const apiKey = process.env.ALCHEMY_API_KEY;

if (!apiKey || apiKey === 'your_alchemy_api_key_here' || apiKey === 'MY_ALCHEMY_API_KEY') {
  console.error('\n❌ ERROR: ALCHEMY_API_KEY is not set or still has the placeholder value!');
  console.error('👉 Please create a .env file in this directory:');
  console.error('   ALCHEMY_API_KEY=alch_tPsuUMHQ7cSfUOprWOv_F\n');
  process.exit(1);
}

const RPC_URL = `https://eth-mainnet.g.alchemy.com/v2/${apiKey}`;

/**
 * Generic helper to send a JSON-RPC 2.0 request to the Alchemy Node
 */
async function sendRpcRequest(method, params = [], id = 1) {
  const payload = {
    jsonrpc: '2.0',
    id: id,
    method: method,
    params: params
  };

  const response = await fetch(RPC_URL, {
    method: 'POST',
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(payload)
  });

  if (!response.ok) {
    throw new Error(`HTTP error ${response.status}: ${response.statusText}`);
  }

  return await response.json();
}

async function runDemo() {
  console.log('====================================================');
  console.log('🚀 Alchemy Ethereum Mainnet Node RPC Demo');
  console.log(`📡 Connected to: https://eth-mainnet.g.alchemy.com/v2/${apiKey.slice(0, 8)}...`);
  console.log('====================================================\n');

  try {
    // 1. eth_blockNumber: Latest block number on Ethereum
    console.log('1️⃣ Method: eth_blockNumber');
    console.log('   Payload: {"id":1,"jsonrpc":"2.0","method":"eth_blockNumber"}');
    const blockRes = await sendRpcRequest('eth_blockNumber', [], 1);
    const hexBlock = blockRes.result;
    const decimalBlock = parseInt(hexBlock, 16);
    console.log(`   ➔ Result Hex:     ${hexBlock}`);
    console.log(`   ➔ Decimal Block:  #${decimalBlock.toLocaleString('en-US')}\n`);

    // 2. eth_gasPrice: Current gas price on Ethereum
    console.log('2️⃣ Method: eth_gasPrice');
    console.log('   Payload: {"id":2,"jsonrpc":"2.0","method":"eth_gasPrice"}');
    const gasRes = await sendRpcRequest('eth_gasPrice', [], 2);
    const hexGas = gasRes.result;
    const gasWei = BigInt(hexGas);
    const gasGwei = Number(gasWei) / 1e9;
    console.log(`   ➔ Result Hex:     ${hexGas}`);
    console.log(`   ➔ Gas Price:      ${gasGwei.toFixed(2)} Gwei (${gasWei.toString()} Wei)\n`);

    // 3. eth_accounts: Client-side account listing
    console.log('3️⃣ Method: eth_accounts');
    console.log('   Payload: {"id":3,"jsonrpc":"2.0","method":"eth_accounts"}');
    const accountsRes = await sendRpcRequest('eth_accounts', [], 3);
    console.log(`   ➔ Result:         ${JSON.stringify(accountsRes.result)}`);
    console.log(`   ℹ️ Note:           Hosted nodes (Alchemy, Infura) return [] for security`);
    console.log(`                     because private keys are kept in your local wallet/signer.\n`);

    // 4. eth_getBalance: Query balance for vitalik.eth
    const vitalikAddress = '0xd8dA6BF26964aF9D7eEd9e03E53415D37aA96045';
    console.log(`4️⃣ Method: eth_getBalance for vitalik.eth (${vitalikAddress})`);
    console.log(`   Payload: {"id":4,"jsonrpc":"2.0","method":"eth_getBalance","params":["${vitalikAddress}","latest"]}`);
    const balanceRes = await sendRpcRequest('eth_getBalance', [vitalikAddress, 'latest'], 4);
    const balanceWei = BigInt(balanceRes.result);
    const balanceEth = Number(balanceWei / 10n**14n) / 10000;
    console.log(`   ➔ Balance Hex:    ${balanceRes.result}`);
    console.log(`   ➔ Balance ETH:    ~${balanceEth.toFixed(4)} ETH\n`);

    // 5. eth_getBlockByNumber: Details for the latest block
    console.log('5️⃣ Method: eth_getBlockByNumber (latest)');
    console.log('   Payload: {"id":5,"jsonrpc":"2.0","method":"eth_getBlockByNumber","params":["latest",false]}');
    const blockDetailRes = await sendRpcRequest('eth_getBlockByNumber', ['latest', false], 5);
    const blockData = blockDetailRes.result;
    const blockTime = new Date(parseInt(blockData.timestamp, 16) * 1000).toUTCString();
    console.log(`   ➔ Block Hash:     ${blockData.hash}`);
    console.log(`   ➔ Miner/Fee Recv: ${blockData.miner}`);
    console.log(`   ➔ Transactions:   ${blockData.transactions.length} txs in this block`);
    console.log(`   ➔ UTC Timestamp:  ${blockTime}\n`);

    console.log('====================================================');
    console.log('🎉 SUCCESS: All Alchemy Node RPCs answered successfully!');
    console.log('====================================================');
  } catch (err) {
    console.error('❌ Error executing Alchemy RPC:', err.message);
  }
}

runDemo();
