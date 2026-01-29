import http from 'k6/http';
import { check, sleep } from 'k6';
import { Trend, Rate, Counter } from 'k6/metrics';

/* =======================
   Custom KPIs
======================= */
export const respTime = new Trend('response_time');
export const errorRate = new Rate('error_rate');
export const throughput = new Counter('throughput');

/* =======================
   Test Plan Configuration
======================= */
export const options = {
  scenarios: {

    // 🔹 Smoke Profile (1–5 users, short run)
    smoke: {
      executor: 'constant-vus',
      vus: 2,
      duration: '20s',
      tags: { profile: 'smoke' },
    },

    // 🔹 Load Profile (20–50 users)
    load: {
      executor: 'ramping-vus',
      startVUs: 5,
      stages: [
        { duration: '30s', target: 20 },
        { duration: '60s', target: 40 },
        { duration: '30s', target: 0 },
      ],
      tags: { profile: 'load' },
    },
  },

  thresholds: {
    http_req_duration: ['p(95)<1000'],   // ⏱ 95% < 1s
    http_req_failed: ['rate<0.01'],      // ❌ Error rate < 1%
    response_time: ['p(95)<1200'],
    error_rate: ['rate<0.01'],
  },
};

/* =======================
   Base Configuration
======================= */
const BASE_URL = 'https://dummyjson.com';

/* =======================
   Test Execution
======================= */
export default function () {

  // 1️⃣ Products List
  let res1 = http.get(`${BASE_URL}/products`);
  recordMetrics(res1);

  check(res1, {
    'Products status is 200': r => r.status === 200,
    'Products response < 1s': r => r.timings.duration < 1000,
  });

  // 2️⃣ Search Products
  let res2 = http.get(`${BASE_URL}/products/search?q=phone`);
  recordMetrics(res2);

  check(res2, {
    'Search status is 200': r => r.status === 200,
    'Search response < 1s': r => r.timings.duration < 1000,
  });

  // 3️⃣ Users List
  let res3 = http.get(`${BASE_URL}/users`);
  recordMetrics(res3);

  check(res3, {
    'Users status is 200': r => r.status === 200,
    'Users response < 1s': r => r.timings.duration < 1000,
  });

  sleep(1);
}

/* =======================
   Metrics Helper
======================= */
function recordMetrics(response) {
  respTime.add(response.timings.duration);
  throughput.add(1);
  errorRate.add(response.status !== 200);
}