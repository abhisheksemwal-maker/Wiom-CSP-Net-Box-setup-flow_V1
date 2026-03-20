const https = require('https');
const http = require('http');
const fs = require('fs');
const path = require('path');

const outDir = 'C:/Users/Abhishek Semwal/wiom-apk/screens-all';

const files = [
  ['01_199-4938_task-list.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/7fff579d-7311-46e8-8471-3933d8f0fb4a'],
  ['02_200-4961_task-detail.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/2324c7de-0070-4ee0-944d-aadcc55dffbf'],
  ['03_200-5040_screen-14351.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/4cabd3d3-c6c8-455e-88fb-e6e19fca0f24'],
  ['04_200-5119_dialog-arrival.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/152b6080-6486-4a60-8881-11cbeef0ad7e'],
  ['05_200-5143_transfer-info.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/227bbd3d-03e6-4a14-aac6-6ce6f4c006e5'],
  ['06_200-5201_transfer-info2.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/dfa81a74-801c-456b-93e7-4a7348057330'],
  ['07_200-5259_dialog-3pin.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/476fdefd-1224-477e-b2e9-359998823aae'],
  ['08_200-5286_dialog3.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/8c5ca50a-0fa9-405f-9543-94db3554c465'],
  ['09_200-5313_revised-install1.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/408be139-5cc9-4a50-814f-8e4f00a9e2c2'],
  ['10_200-5385_screen-14352.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/2be357b9-8bf7-481f-ae5a-de18aab8aebe'],
  ['11_200-5440_screen-14353.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/7abe17a1-6c46-4833-b342-636c5b9a0423'],
  ['12_200-5497_revised-install2.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/28145cd3-10f0-4996-aaf6-41ff1c59c60c'],
  ['13_200-5565_revised-install3.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/c168f517-1295-49f8-925b-3150e97a1893'],
  ['14_200-5633_revised-install4.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/75872815-58cb-4349-8fd3-01efa3f7edd9'],
  ['15_200-5701_revised-install5.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/09497386-a054-43c4-960b-ef8e4ac96496'],
  ['16_200-5769_revised-install6.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/beca3a19-a8ed-4a9b-8ddAe0-bcadb00d9e01'],
  ['17_200-5853_aadhaar391.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/3f6c4638-730e-49d5-b279-c7a704daebfa'],
  ['18_200-5924_screen-14354.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/d8048aab-3746-4c6c-8c9c-97c91a11edd9'],
  ['19_200-6032_revised-install7.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/dcc74df2-88b6-489a-9036-4c9733402247'],
  ['20_200-6144_revised-install8.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/6c08a784-c219-4fa4-bdea-6e1ade5a3b79'],
  ['21_200-6256_revised-install9.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/f8e2b833-4255-445b-b99d-9d6f9a88fc8a'],
  ['22_200-6365_revised-install10.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/569fe10a-ca74-4f83-900e-70df87225ae9'],
  ['23_200-6474_aadhaar392.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/55edb9df-6093-4d87-a7d6-abd8522ed842'],
  ['24_200-6556_aadhaar393.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/4073122f-ec50-4e72-af55-d887c6de07f8'],
  ['25_200-6631_screen-14355.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/af0f9929-115d-4c52-853c-6486d8a3c947'],
  ['26_200-6687_loading1.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/a4f7c30e-fe40-459a-95ae-60ab2a1a8b5b'],
  ['27_200-6691_screen-14356.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/4b292e62-1f2f-4b54-ae73-137905b77974'],
  ['28_200-6748_screen-14357.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/8c9fe94a-d1d3-4e7d-84e7-aafb29af8031'],
  ['29_200-6814_screen-14358.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/82e0e3f7-9e73-40fa-b4ec-09fd85933e26'],
  ['30_200-6879_aadhaar394.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/d75abd77-5d4a-4626-a43a-689c678d6aa5'],
  ['31_200-6954_aadhaar395.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/d7f7e86a-ba66-46f6-a301-1c4fa750c1ce'],
  ['32_200-7036_aadhaar396.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/44ab8a69-5aab-4681-81f3-62fc6471cc17'],
  ['33_200-7118_aadhaar397.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/b5f7b6cd-0fe6-491f-b8c9-fbd6abf6141e'],
  ['34_200-7193_ack1.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/703b093f-4643-46ef-97f0-fa4cbb3f65a3'],
  ['35_200-7204_ack2.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/3eb2e76f-463d-429d-b783-b607ef5bad99'],
  ['36_200-7214_loading2.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/343c4bc5-4404-494e-b1dc-8f23509c8cb9'],
  ['37_200-7218_revised-install11.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/0d6cb57d-30bb-42d1-802f-17d871f0ce05'],
  ['38_200-7327_revised-install12.png', 'https://figma-alpha-api.s3.us-west-2.amazonaws.com/images/1850b062-d779-4b82-9ba4-0b437e267935'],
];

function download(url, filepath) {
  return new Promise((resolve) => {
    const mod = url.startsWith('https') ? https : http;
    const req = mod.get(url, (res) => {
      if (res.statusCode >= 300 && res.statusCode < 400 && res.headers.location) {
        return download(res.headers.location, filepath).then(resolve);
      }
      if (res.statusCode !== 200) {
        res.resume();
        resolve({ ok: false, code: res.statusCode });
        return;
      }
      const file = fs.createWriteStream(filepath);
      res.pipe(file);
      file.on('finish', () => {
        file.close();
        const size = fs.statSync(filepath).size;
        resolve({ ok: true, size });
      });
    });
    req.on('error', (e) => {
      try { fs.unlinkSync(filepath); } catch(_) {}
      resolve({ ok: false, code: e.message });
    });
    req.setTimeout(30000, () => {
      req.destroy();
      try { fs.unlinkSync(filepath); } catch(_) {}
      resolve({ ok: false, code: 'TIMEOUT' });
    });
  });
}

async function main() {
  const results = [];
  for (let i = 0; i < files.length; i += 6) {
    const batch = files.slice(i, i + 6);
    const batchResults = await Promise.all(batch.map(([name, url]) => {
      const fp = path.join(outDir, name);
      return download(url, fp).then(r => ({ name, ...r }));
    }));
    results.push(...batchResults);
  }

  let succeeded = 0, failed = 0, totalSize = 0;
  const failures = [];
  for (const r of results) {
    if (r.ok) {
      succeeded++;
      totalSize += r.size;
      console.log('OK  ' + r.name + ' (' + (r.size/1024).toFixed(1) + ' KB)');
    } else {
      failed++;
      failures.push(r.name + ' -> ' + r.code);
      console.log('FAIL ' + r.name + ' -> ' + r.code);
    }
  }
  console.log('\n--- SUMMARY ---');
  console.log('Succeeded: ' + succeeded + ' / ' + files.length);
  console.log('Failed: ' + failed);
  console.log('Total size: ' + (totalSize/1024/1024).toFixed(2) + ' MB');
  if (failures.length) {
    console.log('\nFailed files:');
    failures.forEach(f => console.log('  ' + f));
  }
}

main();
