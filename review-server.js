const http = require('http');
const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

const ASSETS = path.join(__dirname, 'app/src/main/assets');
const REVIEW_FILE = path.join(__dirname, 'design-review.json');
const PORT = 8080;

const MIME = {
  '.html': 'text/html', '.css': 'text/css', '.js': 'application/javascript',
  '.json': 'application/json', '.png': 'image/png', '.jpg': 'image/jpeg',
  '.svg': 'image/svg+xml', '.webp': 'image/webp', '.mp3': 'audio/mpeg',
  '.woff2': 'font/woff2'
};

const server = http.createServer((req, res) => {
  // CORS
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
  if (req.method === 'OPTIONS') { res.writeHead(200); res.end(); return; }

  // API: POST review
  if (req.method === 'POST' && req.url === '/api/review') {
    let body = '';
    req.on('data', chunk => body += chunk);
    req.on('end', () => {
      fs.writeFileSync(REVIEW_FILE, body);
      // Autosave with timestamp
      try { autosaveReview(JSON.parse(body)); } catch(e) {}
      console.log('\n=== REVIEW SUBMITTED ===');
      console.log('Saved to:', REVIEW_FILE);
      console.log('Tell Claude: "check the review"');
      console.log('========================\n');
      res.writeHead(200, {'Content-Type': 'application/json'});
      res.end(JSON.stringify({ status: 'saved', file: REVIEW_FILE }));
    });
    return;
  }

  // API: GET review status
  if (req.url === '/api/review') {
    if (fs.existsSync(REVIEW_FILE)) {
      const data = fs.readFileSync(REVIEW_FILE, 'utf8');
      res.writeHead(200, {'Content-Type': 'application/json'});
      res.end(data);
    } else {
      res.writeHead(404); res.end('{}');
    }
    return;
  }

  // API: signal reload (called by Claude after fixing)
  if (req.url === '/api/reload') {
    reloadFlag = true;
    res.writeHead(200, {'Content-Type': 'application/json'});
    res.end(JSON.stringify({ status: 'reload-flagged' }));
    return;
  }

  // API: check if reload needed (polled by review tool)
  if (req.url === '/api/check-reload') {
    res.writeHead(200, {'Content-Type': 'application/json'});
    res.end(JSON.stringify({ reload: reloadFlag }));
    if (reloadFlag) reloadFlag = false;
    return;
  }

  // API: build APK and deploy to device
  if (req.url === '/api/deploy' && req.method === 'POST') {
    res.writeHead(200, {'Content-Type': 'application/json'});
    try {
      const ROOT = __dirname;
      const JDK = 'C:/Program Files/Microsoft/jdk-17.0.18.8-hotspot/bin/java';
      const ADB = 'C:/Users/Abhishek Semwal/AppData/Local/Android/Sdk/platform-tools/adb.exe';
      const APK = path.join(ROOT, 'app/build/outputs/apk/debug/app-debug.apk');

      console.log('\n=== BUILDING APK ===');
      execSync(`"${JDK}" -classpath "gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain assembleDebug`, { cwd: ROOT, stdio: 'inherit' });

      // Check for connected device
      const devices = execSync(`"${ADB}" devices`).toString();
      const lines = devices.split('\n').filter(l => l.includes('\tdevice'));
      if (lines.length > 0) {
        const deviceId = lines[0].split('\t')[0];
        console.log('Deploying to:', deviceId);
        execSync(`"${ADB}" -s ${deviceId} install -r "${APK}"`, { stdio: 'inherit' });
        res.end(JSON.stringify({ status: 'deployed', device: deviceId }));
      } else {
        console.log('No device connected. APK saved at:', APK);
        res.end(JSON.stringify({ status: 'built', apk: APK, device: null }));
      }
    } catch(e) {
      console.error('Build/deploy error:', e.message);
      res.end(JSON.stringify({ status: 'error', message: e.message }));
    }
    return;
  }

  // Static file serving — strip query params
  const cleanUrl = req.url.split('?')[0];
  let filePath = path.join(ASSETS, cleanUrl === '/' ? 'index.html' : cleanUrl);
  const ext = path.extname(filePath);
  if (fs.existsSync(filePath) && fs.statSync(filePath).isFile()) {
    res.writeHead(200, {'Content-Type': MIME[ext] || 'application/octet-stream'});
    fs.createReadStream(filePath).pipe(res);
  } else {
    res.writeHead(404); res.end('Not found');
  }
});

let reloadFlag = false;
const BACKUP_DIR = path.join(__dirname, 'backups');
const REVIEW_HISTORY = path.join(BACKUP_DIR, 'review-history');

// Ensure backup dirs exist
if (!fs.existsSync(BACKUP_DIR)) fs.mkdirSync(BACKUP_DIR);
if (!fs.existsSync(REVIEW_HISTORY)) fs.mkdirSync(REVIEW_HISTORY);

// Autosave reviews with timestamp
function autosaveReview(data) {
  const ts = new Date().toISOString().replace(/[:.]/g, '-').slice(0, 19);
  const file = path.join(REVIEW_HISTORY, `review-${ts}.json`);
  fs.writeFileSync(file, JSON.stringify(data, null, 2));
  console.log('Review autosaved:', file);
}

// Versioned APK backup every 30 minutes
function backupAPK() {
  const APK = path.join(__dirname, 'app/build/outputs/apk/debug/app-debug.apk');
  if (!fs.existsSync(APK)) return;
  const ts = new Date().toISOString().replace(/[:.]/g, '-').slice(0, 16);
  const dest = path.join(BACKUP_DIR, `wiom-partner-${ts}.apk`);
  fs.copyFileSync(APK, dest);
  // Also copy the index.html for visual reference
  const htmlSrc = path.join(ASSETS, 'index.html');
  const htmlDest = path.join(BACKUP_DIR, `index-${ts}.html`);
  if (fs.existsSync(htmlSrc)) fs.copyFileSync(htmlSrc, htmlDest);
  console.log('APK backup:', dest);
}

// Run backup every 30 minutes
setInterval(backupAPK, 30 * 60 * 1000);

server.listen(PORT, () => {
  console.log(`Review server running at http://localhost:${PORT}/review.html`);
  console.log('Review loop: Submit in tool → Claude reads design-review.json → fixes → /api/reload → tool reloads');
  console.log('APK backups: every 30 min to', BACKUP_DIR);
});
